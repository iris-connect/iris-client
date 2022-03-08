package iris.client_bff.iris_messages.web;

import iris.client_bff.iris_messages.IrisMessageData;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
class IrisMessageDataAttachmentCountDto {
    private int total;
    private int imported;
    public static IrisMessageDataAttachmentCountDto fromEntity(List<IrisMessageData> dataList) {
        List<IrisMessageData> attachments = dataList == null ? new ArrayList<>() : dataList;
        return new IrisMessageDataAttachmentCountDto(
                attachments.size(),
                (int) attachments.stream().filter(IrisMessageData::isImported).count()
        );
    }
}
