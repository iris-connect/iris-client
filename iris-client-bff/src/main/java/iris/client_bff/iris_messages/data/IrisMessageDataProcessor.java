package iris.client_bff.iris_messages.data;

import org.springframework.web.server.ResponseStatusException;

public interface IrisMessageDataProcessor {
    String getDiscriminator();
    void validateInsert(String insert) throws ResponseStatusException;
    String payloadFromInsert(String insert) throws IrisMessageDataException;
    String payloadFromTransfer(String transfer) throws IrisMessageDataException;
    void importPayload(String payload) throws IrisMessageDataException;
    Object getViewPayload(String payload) throws IrisMessageDataException;
}
