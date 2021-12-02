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

    public List<IrisMessageHdContact> getHdContacts() {
        return this.irisMessageClient.getIrisMessageHdContacts();
    }

    //@todo: implement epsClient message create functionality and save message only if eps call was successful
    public IrisMessage createMessage(IrisMessageInsert messageInsert) {
        IrisMessageFolder outboxRoot = folderRepository.findFirstByContextAndParentFolderIsNull(IrisMessageContext.OUTBOX);
        IrisMessageHdContact hdAuthor = this.irisMessageClient.getOwnIrisMessageHdContact();
        IrisMessageHdContact hdRecipient = this.irisMessageClient.findIrisMessageHdContactById(messageInsert.getHdRecipient());
        IrisMessage message = new IrisMessage();
        message
                .setHdAuthor(hdAuthor)
                .setHdRecipient(hdRecipient)
                .setSubject(messageInsert.getSubject())
                .setBody(messageInsert.getBody())
                .setFolder(outboxRoot)
                .setIsRead(true)
                .setHasAttachments(false);
        return messageRepository.save(message);
    }
}
