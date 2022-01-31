package iris.client_bff.iris_messages.data;

import lombok.Data;

@Data
public class IrisMessageViewData {
    private String id;
    private String discriminator;
    private Object payload;
}
