package iris.client_bff.iris_messages.eps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import iris.client_bff.config.RPCClientConfig;
import iris.client_bff.iris_messages.IrisMessageException;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class EPSIrisMessageClient implements IrisMessageClient {

    private final JsonRpcHttpClient epsRpcClient;
    private final RPCClientConfig rpcClientConfig;

    @Override
    public IrisMessageHdContact getOwnIrisMessageHdContact() throws IrisMessageException {
        // @todo: get contact for own health department
        return this.findIrisMessageHdContactById("sender_dummy_hd");
    }

    @Override
    public IrisMessageHdContact findIrisMessageHdContactById(String contactId) throws IrisMessageException {
        // @todo: implement display name for fetch health-department in EPS and fetch the hd by id to get the name
        // this is a placeholder until the EPS supports display names for health departments
        return new IrisMessageHdContact(contactId, contactId);
    }

    @Override
    public List<IrisMessageHdContact> getIrisMessageHdContacts() throws IrisMessageException {
        var methodName = rpcClientConfig.getOwnEndpoint() + "._directory";
        try {
            return epsRpcClient.invoke(methodName, null, Directory.class).entries().stream()
                    .filter(directoryEntry -> directoryEntry.groups().contains("health-departments"))
                    .map(directoryEntry -> new IrisMessageHdContact(directoryEntry.name, directoryEntry.name))
                    .toList();
        } catch (Throwable t) {
            throw new IrisMessageException(t);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Directory(@NotNull List<@Valid DirectoryEntry> entries) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record DirectoryEntry(@NotNull String name, Set<String> groups) {}

}
