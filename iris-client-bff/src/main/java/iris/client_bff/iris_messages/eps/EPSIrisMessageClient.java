package iris.client_bff.iris_messages.eps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import iris.client_bff.config.RPCClientProperties;
import iris.client_bff.iris_messages.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Version;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EPSIrisMessageClient {

    private static final int READ_TIMEOUT = 12 * 1000;

	private static final Version MESSAGE_CLIENT_MIN_VERSION = new Version(0, 2, 4);

    private final JsonRpcHttpClient epsRpcClient;
    private final RPCClientProperties rpcClientProps;

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
            throw new IrisMessageException(t);
        }
    }

	private boolean isHealthDepartment(DirectoryEntry directoryEntry) {
		return 
				directoryEntry.groups() != null &&
				directoryEntry.groups().contains("health-departments");
	}

	private boolean isHealthDepartmentWithInterGaCommunication(DirectoryEntry directoryEntry) {
		if (!isHealthDepartment(directoryEntry)) return false;
		var methodName = directoryEntry.name + "._ping";
		try {
			Ping ping = epsRpcClient.invoke(methodName, null, Ping.class);
			String semver = ping.version.replaceAll("^v", "");
			Version version = Version.parse(semver);
			return version.isGreaterThanOrEqualTo(MESSAGE_CLIENT_MIN_VERSION);
		} catch (Throwable t) {
			return false;
		}
	}

    public void createIrisMessage(IrisMessage message) throws IrisMessageException {
        String methodName = message.getHdRecipient().getId() + ".createIrisMessage";
        Map<String, IrisMessageTransferDto> payload  = Map.of("irisMessage", IrisMessageTransferDto.fromEntity(message));
        int defaultReadTimeout = this.epsRpcClient.getReadTimeoutMillis();
        try {
            this.epsRpcClient.setReadTimeoutMillis(READ_TIMEOUT);
            this.epsRpcClient.invoke(methodName, payload);
        } catch (Throwable t) {
            throw new IrisMessageException(t);
        } finally {
            this.epsRpcClient.setReadTimeoutMillis(defaultReadTimeout);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Directory(@NotNull List<@Valid DirectoryEntry> entries) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record DirectoryEntry(@NotNull String name, Set<String> groups) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Ping(String version) {};

}
