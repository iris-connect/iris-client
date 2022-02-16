package iris.client_bff.iris_messages;

import iris.client_bff.iris_messages.eps.EPSIrisMessageClient;
import iris.client_bff.iris_messages.eps.IrisMessageTransferDto;
import iris.client_bff.iris_messages.web.IrisMessageInsertDto;
import iris.client_bff.iris_messages.web.IrisMessageInsertFileDto;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IrisMessageBuilder {

	private final IrisMessageFolderRepository folderRepository;
	private final EPSIrisMessageClient irisMessageClient;

	public IrisMessage build(IrisMessageTransferDto messageTransfer) throws IrisMessageException {

		Optional<IrisMessageFolder> folder = this.folderRepository
				.findFirstByContextAndParentFolderIsNull(IrisMessageContext.INBOX);
		if (folder.isEmpty()) {
			throw new IrisMessageException("iris_message.invalid_folder");
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
			throw new IrisMessageException("iris_message.invalid_recipient");
		}

		IrisMessage message = new IrisMessage();

		List<IrisMessageFile> files = new ArrayList<>();
		if (messageTransfer.getFileAttachments() != null) {
			for ( IrisMessageTransferDto.FileAttachment file : messageTransfer.getFileAttachments() ) {
				IrisMessageFile messageFile = new IrisMessageFile()
						.setMessage(message)
						.setContent(Base64.getDecoder().decode(file.getContent()))
						.setName(file.getName());
				files.add(messageFile);
			}
		}

		message
				.setHdAuthor(hdAuthor)
				.setHdRecipient(hdRecipient)
				.setSubject(messageTransfer.getSubject())
				.setBody(messageTransfer.getBody())
				.setFolder(folder.get())
				.setIsRead(false)
				.setFileAttachments(files);

		return message;
	}

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

		List<IrisMessageFile> files = new ArrayList<>();
		if (messageInsert.getFileAttachments() != null) {
			for ( IrisMessageInsertFileDto file : messageInsert.getFileAttachments() ) {
				IrisMessageFile messageFile = new IrisMessageFile()
						.setMessage(message)
						.setContent(Base64.getDecoder().decode(file.getContent()))
						.setName(file.getName());
				files.add(messageFile);
			}
		}

		message
				.setHdAuthor(hdAuthor)
				.setHdRecipient(hdRecipient.get())
				.setSubject(messageInsert.getSubject())
				.setBody(messageInsert.getBody())
				.setFolder(folder.get())
				.setIsRead(true)
				.setFileAttachments(files);

		return message;
	}
}
