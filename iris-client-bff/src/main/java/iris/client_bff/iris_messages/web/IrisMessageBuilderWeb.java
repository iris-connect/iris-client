package iris.client_bff.iris_messages.web;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageContext;
import iris.client_bff.iris_messages.IrisMessageException;
import iris.client_bff.iris_messages.IrisMessageFolder;
import iris.client_bff.iris_messages.IrisMessageFolderRepository;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import iris.client_bff.iris_messages.data.IrisMessageData;
import iris.client_bff.iris_messages.data.IrisMessageDataException;
import iris.client_bff.iris_messages.data.IrisMessageDataInsert;
import iris.client_bff.iris_messages.data.IrisMessageDataProcessor;
import iris.client_bff.iris_messages.data.IrisMessageDataProcessors;
import iris.client_bff.iris_messages.eps.EPSIrisMessageClient;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class IrisMessageBuilderWeb {

	private final IrisMessageFolderRepository folderRepository;
	private final EPSIrisMessageClient irisMessageClient;

	private final IrisMessageDataProcessors messageDataProcessors;
	private final ValidationHelper validationHelper;

	public IrisMessage build(IrisMessageInsertDto messageInsert) throws IrisMessageException {

		Optional<IrisMessageFolder> folder = this.folderRepository
				.findFirstByContextAndParentFolderIsNull(IrisMessageContext.OUTBOX);
		if (folder.isEmpty()) {
			throw new IrisMessageException("iris_message.invalid_folder");
		}

		IrisMessageHdContact hdAuthor = this.irisMessageClient.getOwnIrisMessageHdContact();

		Optional<IrisMessageHdContact> hdRecipient = this.irisMessageClient
				.findIrisMessageHdContactById(messageInsert.getHdRecipient());
		if (hdRecipient.isEmpty()) {
			throw new IrisMessageException("iris_message.invalid_recipient");
		}

		IrisMessage message = new IrisMessage();

		List<IrisMessageData> dataList = new ArrayList<>();
		try {
			if (messageInsert.getDataAttachments() != null) {
				for (IrisMessageDataInsert dataInsert : messageInsert.getDataAttachments()) {
					IrisMessageDataProcessor processor = this.messageDataProcessors
							.getProcessor(dataInsert.getDiscriminator());
					String payload = processor.getPayloadFromInsert(dataInsert.getPayload());
					// The payload should be fine as it is composed of already validated data
					// Doesn't hurt to validate the keys & values of the payloads JSON string
					this.validateMessageDataPayload(payload, dataInsert.getDiscriminator());
					IrisMessageData irisMessageData = new IrisMessageData().setMessage(message)
							.setDiscriminator(dataInsert.getDiscriminator()).setDescription(dataInsert.getDescription())
							.setPayload(payload);
					dataList.add(irisMessageData);
				}
			}
		} catch (Throwable e) {
			throw new IrisMessageException("iris_message.invalid_message_data");
		}

		message
				.setHdAuthor(hdAuthor)
				.setHdRecipient(hdRecipient.get())
				.setSubject(messageInsert.getSubject())
				.setBody(messageInsert.getBody())
				.setFolder(folder.get())
				.setRead(true)
				.setDataAttachments(dataList);

		return message;
	}

	private void validateMessageDataPayload(String value, String field) {
		if (validationHelper.isPossibleAttackForMessageDataPayload(value, field, false)) {
			throw new IrisMessageDataException("iris_message.invalid_message_data");
		}
	}

}
