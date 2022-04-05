package iris.client_bff.iris_messages.eps;

import static java.time.Instant.*;
import static org.apache.commons.collections4.IterableUtils.*;

import iris.client_bff.config.RPCClientProperties;
import iris.client_bff.hd_search.HealthDepartment;
import iris.client_bff.hd_search.eps.EPSHdSearchClient;
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
import java.util.stream.Collectors;

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
	  private static final Duration OWN_CONTACT_CACHE_DURATION = Duration.ofDays(1);

	private static final Version MESSAGE_CLIENT_MIN_VERSION = new Version(0, 2, 4);

    private final JsonRpcHttpClient epsRpcClient;
    private final RPCClientProperties rpcClientProps;
    private final EPSHdSearchClient hdSearchClient;

		private IrisMessageHdContact ownContact;
		private Instant ownContactCreated;
	private final MessageSourceAccessor messages;

    public IrisMessageHdContact getOwnIrisMessageHdContact() {

			if (ownContact == null || ownContactCreated.isBefore(now().minus(OWN_CONTACT_CACHE_DURATION))) {

				var ownId = rpcClientProps.getOwnEndpoint();

				var name = hdSearchClient.getAllHds().stream()
						.filter(it -> it.getEpsName().equals(ownId))
						.map(HealthDepartment::getName)
						.findFirst()
						.orElse(ownId);

				ownContact = new IrisMessageHdContact(ownId, name, true);
				ownContactCreated = now();
			}

			return ownContact;
    }

    public Optional<IrisMessageHdContact> findIrisMessageHdContactById(String contactId) throws IrisMessageException {
        List<IrisMessageHdContact> contacts = this.getIrisMessageHdContacts();
        return contacts.stream().filter(contact -> contact.getId().equals(contactId)).findFirst();
    }

    public List<IrisMessageHdContact> getIrisMessageHdContacts() throws IrisMessageException {

				var healthDepartmentDatas = this.hdSearchClient.getAllHds().stream()
						.collect(Collectors
								.toMap(HealthDepartment::getEpsName, HealthDepartment::getName, (a, b) -> b));

				var ownEndpoint = rpcClientProps.getOwnEndpoint();
				var methodName = ownEndpoint + "._directory";

        try {

        	return epsRpcClient.invoke(methodName, null, Directory.class)
              .entries().parallelStream()
              .filter(this::isHealthDepartmentWithInterGaCommunication)
              .map(directoryEntry -> {

                var epsName = directoryEntry.name;
                var name = healthDepartmentDatas.get(epsName);
                var isOwn = ownEndpoint.equals(epsName);

                return new IrisMessageHdContact(epsName, name, isOwn);
              })
              .sorted(Comparator.comparing(IrisMessageHdContact::getName, String.CASE_INSENSITIVE_ORDER))
              .toList();
        } catch (Throwable t) {
            throw new IrisMessageException(messages.getMessage("iris_message.missing_hd_contacts"));
        }
    }

	private boolean isHealthDepartment(DirectoryEntry directoryEntry) {
		return contains(directoryEntry.groups(), "health-departments");
	}

	private boolean isHealthDepartmentWithInterGaCommunication(DirectoryEntry directoryEntry) {

		return isHealthDepartment(directoryEntry)
					&& checkIfEpsVersionGreatEnough(directoryEntry.name);
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
    record Ping(String version) {}
}
