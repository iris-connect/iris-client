package iris.client_bff.iris_messages.data;

import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public interface IrisMessageDataProcessor {
    String getDiscriminator();
    void validateInsert(String insert) throws ResponseStatusException;
    void validateImportSelection(String importSelection) throws ResponseStatusException;
    String getPayloadFromInsert(String insert) throws IrisMessageDataException;
    void importPayload(String payload) throws IrisMessageDataException;
    void importPayload(String payload, UUID importTargetId, String importSelection) throws IrisMessageDataException;
    Object getViewPayload(String payload) throws IrisMessageDataException;
    Object getImportSelectionViewPayload(String payload, UUID importTargetId) throws IrisMessageDataException;
}
