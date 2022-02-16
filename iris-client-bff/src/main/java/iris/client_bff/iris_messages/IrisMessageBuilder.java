package iris.client_bff.iris_messages;

import iris.client_bff.iris_messages.eps.EPSIrisMessageClient;
import iris.client_bff.iris_messages.eps.IrisMessageTransferDto;
import iris.client_bff.iris_messages.web.IrisMessageInsertDto;
import lombok.RequiredArgsConstructor;

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

		// disabled file attachments
		/*
		List<IrisMessageFile> files = new ArrayList<>();
		try {
		    if (messageTransfer.getFileAttachments() != null) {
		        Tika tika = new Tika();
		        for ( IrisMessageTransferDto.FileAttachment file : messageTransfer.getFileAttachments() ) {
		            IrisMessageFile messageFile = new IrisMessageFile()
		                    .setMessage(message)
		                    .setContent(file.getContent())
		                    .setName(file.getName());
		            files.add(messageFile);
		        }
		    }
		} catch (Throwable e) {
		    throw new IrisMessageException("iris_message.invalid_file");
		}
		 */

		message
				.setHdAuthor(hdAuthor)
				.setHdRecipient(hdRecipient)
				.setSubject(messageTransfer.getSubject())
				.setBody(messageTransfer.getBody())
				.setFolder(folder.get())
				.setIsRead(false)
		// disabled file attachments
		// .setFileAttachments(files)
		;

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

		// disabled file attachments
		/*
		List<IrisMessageFile> files = new ArrayList<>();
		try {
		    if (messageInsert.getFileAttachments() != null) {
		        for ( MultipartFile file : messageInsert.getFileAttachments() ) {
		            String fileName = file.getOriginalFilename() == null ? file.getName() : file.getOriginalFilename();
		            IrisMessageFile messageFile = new IrisMessageFile()
		                    .setMessage(message)
		                    .setContent(file.getBytes())
		                    .setName(fileName);
		            files.add(messageFile);
		        }
		    }
		} catch (IOException e) {
		    throw new IrisMessageException("iris_message.invalid_file");
		}
		 */

		message
				.setHdAuthor(hdAuthor)
				.setHdRecipient(hdRecipient.get())
				.setSubject(messageInsert.getSubject())
				.setBody(messageInsert.getBody())
				.setFolder(folder.get())
				.setIsRead(true)
		// disabled file attachments
		// .setFileAttachments(files)
		;

		return message;
	}
}
