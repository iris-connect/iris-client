package iris.client_bff.iris_messages.web;

import iris.client_bff.core.validation.NoSignOfAttack;
import iris.client_bff.core.validation.NoSignOfAttack.MessageDataPayload;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageData;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import iris.client_bff.iris_messages.utils.RawJsonDeserializer;
import lombok.Data;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Data
public class IrisMessageInsertDto {

	@NotBlank
	@Size(max = IrisMessageHdContact.ID_MAX_LENGTH)
	@NoSignOfAttack
	private String hdRecipient;

	@NotBlank
	@Size(max = IrisMessage.SUBJECT_MAX_LENGTH)
	@NoSignOfAttack
	private String subject;

	@NotBlank
	@Size(max = IrisMessage.BODY_MAX_LENGTH)
	@NoSignOfAttack
	private String body;

	private List<@Valid DataAttachment> dataAttachments;

	@Data
	public static class DataAttachment {

		@NotBlank
		@Size(max = IrisMessageData.DISCRIMINATOR_MAX_LENGTH)
		@NoSignOfAttack
		private String discriminator;

		@NotNull
		// The validation of the insert payload is handled by the data processor
		// Doesn't hurt to validate the keys & values of the payloads JSON string
		@NoSignOfAttack(payload = MessageDataPayload.class)
		@JsonDeserialize(using = RawJsonDeserializer.class)
		private String payload;

		@NotBlank
		@Size(max = IrisMessageData.DESCRIPTION_MAX_LENGTH)
		@NoSignOfAttack
		private String description;

	}
}
