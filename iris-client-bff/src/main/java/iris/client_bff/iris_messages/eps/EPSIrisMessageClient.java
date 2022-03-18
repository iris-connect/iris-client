package iris.client_bff.iris_messages.eps;

import iris.client_bff.config.RPCClientProperties;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import iris.client_bff.iris_messages.exceptions.IrisMessageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.util.Version;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class EPSIrisMessageClient {

    private static final int READ_TIMEOUT = 12 * 1000;
	  private static final Duration CACHING_TIME = Duration.ofMinutes(30);

	private static final Version MESSAGE_CLIENT_MIN_VERSION = new Version(0, 2, 4);

	  private static final Map<String, MapEntry> hdCache = new ConcurrentHashMap<>();

    private final JsonRpcHttpClient epsRpcClient;
    private final RPCClientProperties rpcClientProps;
	private final MessageSourceAccessor messages;

    public IrisMessageHdContact getOwnIrisMessageHdContact() {
        String ownId = rpcClientProps.getOwnEndpoint();
        return new IrisMessageHdContact(ownId, ownId, true);
    }

    public Optional<IrisMessageHdContact> findIrisMessageHdContactById(String contactId) throws IrisMessageException {
        List<IrisMessageHdContact> contacts = this.getIrisMessageHdContacts();
        return contacts.stream().filter(contact -> contact.getId().equals(contactId)).findFirst();
    }

    public List<IrisMessageHdContact> getIrisMessageHdContacts() throws IrisMessageException {
        var methodName = rpcClientProps.getOwnEndpoint() + "._directory";
        try {
            return epsRpcClient.invoke(methodName, null, Directory.class).entries().stream()
                    .filter(this::isHealthDepartmentWithInterGaCommunication)
                    .map(directoryEntry -> {
                        boolean isOwn = rpcClientProps.getOwnEndpoint().equals(directoryEntry.name);
                        return new IrisMessageHdContact(directoryEntry.name, directoryEntry.name, isOwn);
                    })
                    .sorted(Comparator.comparing(IrisMessageHdContact::getName, String.CASE_INSENSITIVE_ORDER))
                    .toList();
        } catch (Throwable t) {
            throw new IrisMessageException(messages.getMessage("iris_message.missing_hd_contacts"));
        }
    }

	private boolean isHealthDepartment(DirectoryEntry directoryEntry) {
		return 
				directoryEntry.groups() != null &&
				directoryEntry.groups().contains("health-departments");
	}

	private boolean isHealthDepartmentWithInterGaCommunication(DirectoryEntry directoryEntry) {

		if (!isHealthDepartment(directoryEntry))
			return false;

		return hdCache.compute(directoryEntry.name, (key, value) -> {

			if (value == null || value.validatedAt().isBefore(Instant.now().minus(CACHING_TIME))) {
				return new MapEntry(Instant.now(), checkIfEpsVersionGreatEnough(directoryEntry.name));
		}

			return value;
		}).valid();
	}

    public void createIrisMessage(IrisMessage message) throws IrisMessageException {
        String methodName = message.getHdRecipient().getId() + ".createIrisMessage";
        Map<String, IrisMessageTransferDto> payload  = Map.of("irisMessage", IrisMessageTransferDto.fromEntity(message));
        int defaultReadTimeout = this.epsRpcClient.getReadTimeoutMillis();
        try {
            this.epsRpcClient.setReadTimeoutMillis(READ_TIMEOUT);
            this.epsRpcClient.invoke(methodName, payload);
        } catch (Throwable t) {
            throw new IrisMessageException(messages.getMessage("iris_message.submission_error"));
        } finally {
            this.epsRpcClient.setReadTimeoutMillis(defaultReadTimeout);
        }
    }

	private boolean checkIfEpsVersionGreatEnough(String name) {

		var methodName = name + "._ping";

		try {
			Ping ping = epsRpcClient.invoke(methodName, null, Ping.class);
			String semver = ping.version.replaceAll("^v", "");
			Version version = Version.parse(semver);
			return version.isGreaterThanOrEqualTo(MESSAGE_CLIENT_MIN_VERSION);
		} catch (Throwable t) {
			
			log.warn("Can't ping hd client " + name);
			
			return false;
		}
	}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Directory(@NotNull List<@Valid DirectoryEntry> entries) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record DirectoryEntry(@NotNull String name, Set<String> groups) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Ping(String version) {};

	private record MapEntry(Instant validatedAt, boolean valid) {};
}
