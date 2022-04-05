package iris.client_bff.iris_messages.eps;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageContext;
import iris.client_bff.iris_messages.exceptions.IrisMessageException;
import iris.client_bff.iris_messages.IrisMessageFolder;
import iris.client_bff.iris_messages.IrisMessageFolderRepository;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import iris.client_bff.iris_messages.IrisMessageData;
import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class IrisMessageBuilderEps {

	private final IrisMessageFolderRepository folderRepository;
	private final EPSIrisMessageClient irisMessageClient;
	private final MessageSourceAccessor messages;

	private final ValidationHelper validationHelper;

	public IrisMessage build(IrisMessageTransferDto messageTransfer) throws IrisMessageException {

		Optional<IrisMessageFolder> folder = this.folderRepository
				.findFirstByContextAndParentFolderIsNull(IrisMessageContext.INBOX);
		if (folder.isEmpty()) {
			throw new IrisMessageException(messages.getMessage("iris_message.invalid_folder"));
		}

		IrisMessageHdContact hdAuthor = new IrisMessageHdContact(
				messageTransfer.getHdAuthor().getId(),
				messageTransfer.getHdAuthor().getName());

		IrisMessageHdContact hdRecipient = new IrisMessageHdContact(
				messageTransfer.getHdRecipient().getId(),
				messageTransfer.getHdRecipient().getName());

		// ensure that the message was sent to the correct recipient
		IrisMessageHdContact hdOwn = this.irisMessageClient.getOwnIrisMessageHdContact();
		if (!Objects.equals(hdOwn.getId(), hdRecipient.getId())) {
			throw new IrisMessageException(messages.getMessage("iris_message.invalid_recipient"));
		}

		IrisMessage message = new IrisMessage();

		List<IrisMessageData> dataList = new ArrayList<>();
		try {
			if (messageTransfer.getDataAttachments() != null) {
				for (IrisMessageTransferDto.DataAttachment dataAttachment : messageTransfer.getDataAttachments()) {
					// we do not process and / or defuse the payload when receiving it to be able to
					// store yet unknown payload types.
					// We defuse it while importing / viewing
					// To minimize the risk of possible attacks, we check the keys & values of the
					// payloads JSON string
					this.validateMessageDataPayload(dataAttachment.getPayload(), dataAttachment.getDiscriminator());
					IrisMessageData irisMessageData = new IrisMessageData().setMessage(message)
							.setDiscriminator(dataAttachment.getDiscriminator())
							.setDescription(dataAttachment.getDescription()).setPayload(dataAttachment.getPayload());
					dataList.add(irisMessageData);
				}
			}
		} catch (Exception e) {
			throw new IrisMessageException(messages.getMessage("iris_message.invalid_message_data"));
		}

		message
				.setHdAuthor(hdAuthor)
				.setHdRecipient(hdRecipient)
				.setSubject(messageTransfer.getSubject())
				.setBody(messageTransfer.getBody())
				.setFolder(folder.get())
				.setRead(false)
				.setDataAttachments(dataList);

		return message;
	}

	private void validateMessageDataPayload(String value, String field) {
		if (validationHelper.isPossibleAttackForMessageDataPayload(value, field, true)) {
			throw new IrisMessageDataException(messages.getMessage("iris_message.invalid_message_data"));
		}
	}
}
