package iris.client_bff.iris_messages.web;

import lombok.Data;

@Data
public class IrisMessageDataViewDataDto {
	private String id;
	private String discriminator;
	private Object payload;
}
