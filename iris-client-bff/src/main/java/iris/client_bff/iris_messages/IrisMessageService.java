package iris.client_bff.iris_messages;

import iris.client_bff.core.utils.HibernateSearcher;
import iris.client_bff.hd_search.HealthDepartment;
import iris.client_bff.hd_search.eps.EPSHdSearchClient;
import iris.client_bff.iris_messages.data.*;
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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IrisMessageService {

    private static final String[] SEARCH_FIELDS = { "subject", "hdAuthor.name", "hdRecipient.name" };

    private final IrisMessageRepository messageRepository;
    private final IrisMessageFolderRepository folderRepository;
    private final IrisMessageDataRepository dataRepository;
    private final IrisMessageFileRepository fileRepository;
    private final HibernateSearcher searcher;
    private final EPSIrisMessageClient irisMessageClient;
    private final EPSHdSearchClient hdSearchClient;

    private final IrisMessageBuilder messageBuilder;

    private final IrisMessageDataProcessors messageDataProcessors;

    public Optional<IrisMessage> findById(UUID messageId) {
        return messageRepository.findById(IrisMessage.IrisMessageIdentifier.of(messageId));
    }

    public Page<IrisMessage> search(UUID folderId, String searchString, Pageable pageable) {
        var folderIdentifier = IrisMessageFolder.IrisMessageFolderIdentifier.of(folderId);
        if (StringUtils.isEmpty(searchString)) {
            return messageRepository.findAllByFolderIdOrderByIsReadAsc(folderIdentifier, pageable);
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

    private IrisMessageData getMessageData(UUID messageDataId) {
        Optional<IrisMessageData> optionalIrisMessageData = this.dataRepository.findById(IrisMessageData.IrisMessageDataIdentifier.of(messageDataId));
        if (optionalIrisMessageData.isEmpty()) {
            throw new IrisMessageDataException("missing message data");
        }
        return optionalIrisMessageData.get();
    }

    public IrisMessageViewData viewMessageData(UUID messageDataId) {
        IrisMessageData messageData = this.getMessageData(messageDataId);
        IrisMessageDataProcessor processor = this.messageDataProcessors.getProcessor(messageData.getDiscriminator());
        return new IrisMessageViewData()
                .setId(messageData.getId().toString())
                .setDiscriminator(messageData.getDiscriminator())
                .setPayload(processor.viewPayload(messageData.getPayload()));
    }

    public void importMessageData(UUID messageDataId) {
        IrisMessageData messageData = this.getMessageData(messageDataId);
        IrisMessageDataProcessor processor = this.messageDataProcessors.getProcessor(messageData.getDiscriminator());
        processor.importPayload(messageData.getPayload());
        messageData.setIsImported(true);
        this.dataRepository.save(messageData);
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
