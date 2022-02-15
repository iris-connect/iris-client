package iris.client_bff.iris_messages;

import iris.client_bff.core.utils.HibernateSearcher;
import iris.client_bff.hd_search.HealthDepartment;
import iris.client_bff.hd_search.eps.EPSHdSearchClient;
import iris.client_bff.iris_messages.IrisMessage.IrisMessageIdentifier;
import iris.client_bff.iris_messages.IrisMessageFolder.IrisMessageFolderIdentifier;
import iris.client_bff.iris_messages.eps.EPSIrisMessageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IrisMessageService {

    private static final String[] SEARCH_FIELDS = { "subject", "hdAuthor.name", "hdRecipient.name" };

    private final IrisMessageRepository messageRepository;
    private final IrisMessageFolderRepository folderRepository;
    private final IrisMessageFileRepository fileRepository;
    private final HibernateSearcher searcher;
    private final EPSIrisMessageClient irisMessageClient;
    private final EPSHdSearchClient hdSearchClient;

    private final IrisMessageBuilder messageBuilder;

    public Optional<IrisMessage> findById(IrisMessageIdentifier messageId) {
        return messageRepository.findById(messageId);
    }

    public Page<IrisMessage> search(IrisMessageFolderIdentifier folderId, String searchString, Pageable pageable) {
        if (StringUtils.isEmpty(searchString)) {
            return messageRepository.findAllByFolderIdOrderByIsReadAsc(folderId, pageable);
        }
        var result = searcher.search(
                searchString,
                pageable,
                SEARCH_FIELDS,
                it -> it.must(f2 -> f2.match().field("folder.id").matching(folderId)),
                IrisMessage.class);
        return new PageImpl<>(result.hits(), pageable, result.total().hitCount());
    }

    public int getCountUnreadByFolderId(IrisMessageFolderIdentifier folderId) {
        return messageRepository.getCountUnreadByFolderId(folderId);
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

    // disabled file attachments
    /*
    public Optional<IrisMessageFile> findFileById(UUID fileId) {
        return this.fileRepository.findById(IrisMessageFile.IrisMessageFileIdentifier.of(fileId));
    }
     */

    public List<IrisMessageHdContact> getHdContacts(String search) throws IrisMessageException {

        List<IrisMessageHdContact> contacts = this.irisMessageClient.getIrisMessageHdContacts();

        if (search == null || search.equals("")) {
            return contacts;
        }

        List<HealthDepartment> healthDepartments = this.hdSearchClient.searchForHd(search);
        List<String> hdEpsNames = healthDepartments
                .stream()
                .map(HealthDepartment::getEpsName)
                .filter(Objects::nonNull)
                .toList();

        return contacts
                .stream()
                .filter(contact -> hdEpsNames.contains(contact.getId()) || contact.getName().contains(search) || contact.getId().contains(search))
                .toList();
    }

    public IrisMessageHdContact getOwnHdContact() {
        return this.irisMessageClient.getOwnIrisMessageHdContact();
    }

    public IrisMessage sendMessage(IrisMessageInsert messageInsert) throws IrisMessageException {

        IrisMessage message = this.messageBuilder.build(messageInsert);

        this.irisMessageClient.createIrisMessage(message);

        return this.messageRepository.save(message);
    }

    public IrisMessage receiveMessage(IrisMessageTransfer messageTransfer) throws IrisMessageException {

        IrisMessage message = this.messageBuilder.build(messageTransfer);

        return this.messageRepository.save(message);
    }

}
