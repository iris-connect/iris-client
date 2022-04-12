package iris.client_bff.iris_messages.eps;

import iris.client_bff.core.serialization.DefuseJsonString;
import iris.client_bff.core.validation.AttackDetector.MessageDataPayload;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageData;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IrisMessageTransferDto {

	// we send the name & id as we do not know if the author`s health department is accessible by the recipient
	@Valid
	private HdContact hdAuthor;

	// the recipient is used to check if it is identical with the endpoint identifier of the health department, the
	// message is sent to
	@Valid
	private HdContact hdRecipient;

	@NotBlank
	@Size(max = IrisMessage.SUBJECT_MAX_LENGTH)
	@DefuseJsonString(maxLength = IrisMessage.SUBJECT_MAX_LENGTH)
	private String subject;

	@NotBlank
	@Size(max = IrisMessage.BODY_MAX_LENGTH)
	@DefuseJsonString(maxLength = IrisMessage.BODY_MAX_LENGTH)
	private String body;

	private List<@Valid DataAttachment> dataAttachments;

	public static IrisMessageTransferDto fromEntity(IrisMessage message) {
		return IrisMessageTransferDto.builder()
				.hdAuthor(HdContact.fromEntity(message.getHdAuthor()))
				.hdRecipient(HdContact.fromEntity(message.getHdRecipient()))
				.subject(message.getSubject())
				.body(message.getBody())
				.dataAttachments(DataAttachment.fromEntity(message.getDataAttachments()))
				.build();
	}

	@Data
	public static class HdContact {

		@NotBlank
		@Size(max = IrisMessageHdContact.ID_MAX_LENGTH)
		@DefuseJsonString(maxLength = IrisMessageHdContact.ID_MAX_LENGTH)
		private String id;

		@NotBlank
		@Size(max = IrisMessageHdContact.NAME_MAX_LENGTH)
		@DefuseJsonString(maxLength = IrisMessageHdContact.NAME_MAX_LENGTH)
		private String name;

		public static HdContact fromEntity(IrisMessageHdContact contact) {
			return new HdContact()
					.setId(contact.getId())
					.setName(contact.getName());
		}
	}

	@Data
	public static class DataAttachment {

		@NotBlank
		@Size(max = IrisMessageData.DISCRIMINATOR_MAX_LENGTH)
		@DefuseJsonString(maxLength = IrisMessageData.DISCRIMINATOR_MAX_LENGTH)
		private String discriminator;

		@NotBlank
		@Size(max = IrisMessageData.PAYLOAD_MAX_LENGTH)
		@DefuseJsonString(maxLength = IrisMessageData.PAYLOAD_MAX_LENGTH, payload = MessageDataPayload.class)
		private String payload;

		@NotBlank
		@Size(max = IrisMessageData.DESCRIPTION_MAX_LENGTH)
		@DefuseJsonString(maxLength = IrisMessageData.DESCRIPTION_MAX_LENGTH)
		private String description;

		public static List<DataAttachment> fromEntity(List<IrisMessageData> dataList) {
			return dataList.stream().map(DataAttachment::fromEntity).toList();
		}

		public static DataAttachment fromEntity(IrisMessageData data) {
			return new DataAttachment()
					.setDiscriminator(data.getDiscriminator())
					.setPayload(data.getPayload())
					.setDescription(data.getDescription());
		}
	}

}
