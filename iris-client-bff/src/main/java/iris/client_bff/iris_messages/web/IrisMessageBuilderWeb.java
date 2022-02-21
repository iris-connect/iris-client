package iris.client_bff.iris_messages.web;

import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageContext;
import iris.client_bff.iris_messages.IrisMessageException;
import iris.client_bff.iris_messages.IrisMessageFolder;
import iris.client_bff.iris_messages.IrisMessageFolderRepository;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import iris.client_bff.iris_messages.eps.EPSIrisMessageClient;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class IrisMessageBuilderWeb {

	private final IrisMessageFolderRepository folderRepository;
	private final EPSIrisMessageClient irisMessageClient;

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

		message
				.setHdAuthor(hdAuthor)
				.setHdRecipient(hdRecipient.get())
				.setSubject(messageInsert.getSubject())
				.setBody(messageInsert.getBody())
				.setFolder(folder.get())
				.setRead(true);

		return message;
	}
}
