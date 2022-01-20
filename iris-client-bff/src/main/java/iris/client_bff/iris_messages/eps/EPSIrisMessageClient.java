package iris.client_bff.iris_messages.eps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import iris.client_bff.config.RPCClientProperties;
import iris.client_bff.iris_messages.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EPSIrisMessageClient {

    private static final int READ_TIMEOUT = 12 * 1000;

    //@todo: replace DummyBridge with proper EPS call (remove next line)
    private final IrisMessageDummyBridge irisMessageDummyBridge;

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
                    .filter(directoryEntry -> directoryEntry.groups().contains("health-departments"))
                    .map(directoryEntry -> new IrisMessageHdContact(directoryEntry.name, directoryEntry.name))
                    .sorted(Comparator.comparing(IrisMessageHdContact::getName, String.CASE_INSENSITIVE_ORDER))
                    .toList();
        } catch (Throwable t) {
            throw new IrisMessageException(t);
        }
    }

    public void createIrisMessage(IrisMessage message) throws IrisMessageException {
        String methodName = message.getHdRecipient().getId() + ".handleIrisMessage";
        Map<String, IrisMessageTransfer> payload  = Map.of("irisMessage", IrisMessageTransfer.fromEntity(message));
        int defaultReadTimeout = this.epsRpcClient.getReadTimeoutMillis();
        try {

            // @todo: replace controller call (dummy code) with EPS client call
            // dummy code: start
            // for testing, we are bypassing the EPS client, simulate the json encoding / decoding and call the dummy bridge directly to receive our own message
            ObjectMapper objectMapper = new ObjectMapper();
            String messageJsonString = objectMapper.writeValueAsString(payload.get("irisMessage"));
            IrisMessageTransfer parsedMessage = objectMapper.readValue(messageJsonString, IrisMessageTransfer.class);
            this.irisMessageDummyBridge.createMessage(parsedMessage);
            // dummy code: end

            this.epsRpcClient.setReadTimeoutMillis(READ_TIMEOUT);
//            this.epsRpcClient.invoke(methodName, payload);
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

}
