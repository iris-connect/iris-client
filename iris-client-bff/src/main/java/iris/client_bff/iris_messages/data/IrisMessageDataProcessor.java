package iris.client_bff.iris_messages.data;

import iris.client_bff.iris_messages.IrisMessageTransfer;

public interface IrisMessageDataProcessor {
    String getDiscriminator();
    IrisMessageData convertToData(IrisMessageDataInsert irisMessageDataInsert) throws IrisMessageDataException;
    IrisMessageData convertToData(IrisMessageTransfer.DataAttachment dataAttachment) throws IrisMessageDataException;
    void importData(IrisMessageData irisMessageData) throws IrisMessageDataException;
}
