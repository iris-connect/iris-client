package iris.client_bff.iris_messages;

import iris.client_bff.core.utils.HibernateSearcher;
import iris.client_bff.iris_messages.eps.IrisMessageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IrisMessageService {

    private static final String[] SEARCH_FIELDS = { "subject", "hdAuthor.name", "hdRecipient.name" };

    private final IrisMessageRepository messageRepository;
    private final IrisMessageFolderRepository folderRepository;
    private final IrisMessageFileRepository fileRepository;
    private final HibernateSearcher searcher;
    private final IrisMessageClient irisMessageClient;

    public Optional<IrisMessage> findById(UUID messageId) {
        return messageRepository.findById(IrisMessage.IrisMessageIdentifier.of(messageId));
    }

    public Page<IrisMessage> search(UUID folderId, String searchString, Pageable pageable) {
        var folderIdentifier = IrisMessageFolder.IrisMessageFolderIdentifier.of(folderId);
        if (StringUtils.isEmpty(searchString)) {
            return messageRepository.findAllByFolderId(folderIdentifier, pageable);
        }
        var result = searcher.search(
                searchString,
                pageable,
                SEARCH_FIELDS,
                it -> it.must(f2 -> f2.match().field("folder.id").matching(folderIdentifier)),
                IrisMessage.class);
        return new PageImpl<>(result.hits(), pageable, result.total().hitCount());
    }

    public int getCountUnreadByFolderId(UUID folderId) {
        return messageRepository.getCountUnreadByFolderId(IrisMessageFolder.IrisMessageFolderIdentifier.of(folderId));
    }

    public int getCountUnread() {
        return messageRepository.countByIsReadFalseOrIsReadIsNull();
    }

    public List<IrisMessageFolder> getFolders() {
        return folderRepository.findAll();
    }

    public IrisMessage updateMessage(IrisMessage message, IrisMessageUpdate messageUpdate) {
        message.setIsRead(messageUpdate.getIsRead());
        return this.messageRepository.save(message);
    }

    public Optional<IrisMessageFile> getFile(UUID fileId) {
        return this.fileRepository.findById(IrisMessageFile.IrisMessageFileIdentifier.of(fileId));
    }

    public List<IrisMessageHdContact> getHdContacts() {
        return this.irisMessageClient.getIrisMessageHdContacts();
    }

    public IrisMessage createMessage(IrisMessageInsert messageInsert) throws IOException {
        IrisMessageFolder outboxRoot = folderRepository.findFirstByContextAndParentFolderIsNull(IrisMessageContext.OUTBOX);
        IrisMessageHdContact hdAuthor = this.irisMessageClient.getOwnIrisMessageHdContact();
        IrisMessageHdContact hdRecipient = this.irisMessageClient.findIrisMessageHdContactById(messageInsert.getHdRecipient());

        IrisMessage message = new IrisMessage();

        List<IrisMessageFile> files = new ArrayList<>();
        if (messageInsert.getAttachments() != null) {
            for ( MultipartFile file : messageInsert.getAttachments() ) {
                IrisMessageFile messageFile = new IrisMessageFile()
                        .setMessage(message)
                        .setContent(file.getBytes())
                        .setContentType(file.getContentType())
                        .setName(file.getOriginalFilename());
                files.add(messageFile);
            }
        }

        message
                .setHdAuthor(hdAuthor)
                .setHdRecipient(hdRecipient)
                .setSubject(messageInsert.getSubject())
                .setBody(messageInsert.getBody())
                .setFolder(outboxRoot)
                .setIsRead(true)
                .setAttachments(files);

        //@todo: implement epsClient message create functionality and save message only if eps call was successful

        return messageRepository.save(message);
    }
}
