package iris.client_bff.iris_messages;

import iris.client_bff.core.utils.HibernateSearcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IrisMessageService {

    private static final String[] SEARCH_FIELDS = { "subject", "authorHd.name", "recipientHd.name" };

    private final IrisMessageRepository messageRepository;
    private final IrisMessageFolderRepository folderRepository;
    private final HibernateSearcher searcher;

    public Page<IrisMessage> search(String folder, String searchString, Pageable pageable) {
        var folderId = IrisMessageFolder.IrisMessageFolderIdentifier.of(folder);
        if (StringUtils.isEmpty(searchString)) {
            return messageRepository.findAllByFolderId(folderId, pageable);
        }
        var result = searcher.search(
                searchString,
                pageable,
                SEARCH_FIELDS,
                it -> it.must(f2 -> f2.match().field("folder.id").matching(folderId)),
                IrisMessage.class);
        return new PageImpl<>(result.hits(), pageable, result.total().hitCount());
    }

    public int getCountUnread(String folder) {
        return messageRepository.getCountUnread(folder);
    }

    public List<IrisMessageFolder> getFolders() {
        return folderRepository.findAll();
    }

    //@todo: implement epsClient message create functionality and save message only if eps call was successful
    //@todo: implement return value (DTO) that isn't void
    public void createMessage(IrisMessageInsert irisMessageInsert) {
        IrisMessageFolder outboxRoot = folderRepository.findFirstByContextAndParentFolderIsNull(IrisMessageContext.OUTBOX);
        //@todo: Replace dummy IrisMessageContact code with EpsClient data
        IrisMessageContact author = new IrisMessageContact()
                .setId("replace_this_dummy_author_id")
                .setName("replace_this_dummy_author_name");
        IrisMessageContact recipient = new IrisMessageContact()
                .setId("replace_this_dummy_recipient_id")
                .setName("replace_this_dummy_recipient_name");
        IrisMessage message = new IrisMessage();
        message
                .setAuthorHd(author)
                .setRecipientHd(recipient)
                .setSubject(irisMessageInsert.getSubject())
                .setBody(irisMessageInsert.getBody())
                .setFolder(outboxRoot)
                .setIsRead(true)
                .setHasAttachments(false);
        messageRepository.save(message);
    }
}
