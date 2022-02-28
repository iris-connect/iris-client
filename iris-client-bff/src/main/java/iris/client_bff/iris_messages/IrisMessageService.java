package iris.client_bff.iris_messages;

import iris.client_bff.core.utils.HibernateSearcher;
import iris.client_bff.hd_search.HealthDepartment;
import iris.client_bff.hd_search.eps.EPSHdSearchClient;
import iris.client_bff.iris_messages.IrisMessage.IrisMessageIdentifier;
import iris.client_bff.iris_messages.IrisMessageFolder.IrisMessageFolderIdentifier;
import iris.client_bff.iris_messages.data.IrisMessageData;
import iris.client_bff.iris_messages.data.IrisMessageData.IrisMessageDataIdentifier;
import iris.client_bff.iris_messages.data.IrisMessageDataException;
import iris.client_bff.iris_messages.data.IrisMessageDataProcessor;
import iris.client_bff.iris_messages.data.IrisMessageDataProcessors;
import iris.client_bff.iris_messages.data.IrisMessageDataRepository;
import iris.client_bff.iris_messages.data.IrisMessageDataViewData;
import iris.client_bff.iris_messages.eps.EPSIrisMessageClient;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IrisMessageService {

	private static final String[] SEARCH_FIELDS = { "subject_search", "hdAuthor.name_search", "hdRecipient.name_search" };

    private final IrisMessageRepository messageRepository;
    private final IrisMessageFolderRepository folderRepository;
    private final IrisMessageDataRepository dataRepository;
    private final HibernateSearcher searcher;
    private final EPSIrisMessageClient irisMessageClient;
    private final EPSHdSearchClient hdSearchClient;

    private final IrisMessageDataProcessors messageDataProcessors;

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
        return messageRepository.countByIsReadFalse();
    }

    public List<IrisMessageFolder> getFolders() {
        return folderRepository.findAll();
    }

    public IrisMessageDataProcessor getMessageDataProcessor(IrisMessageDataIdentifier messageDataId) throws IrisMessageDataException {
        IrisMessageData messageData = this.getMessageData(messageDataId);
        return this.getMessageDataProcessor(messageData.getDiscriminator());
    }

    public IrisMessageDataProcessor getMessageDataProcessor(String discriminator) throws IrisMessageDataException {
        return this.messageDataProcessors.getProcessor(discriminator);
    }

    private IrisMessageData getMessageData(IrisMessageDataIdentifier messageDataId) {
        Optional<IrisMessageData> optionalIrisMessageData = this.dataRepository.findById(messageDataId);
        if (optionalIrisMessageData.isEmpty()) {
            throw new IrisMessageDataException("missing message data");
        }
        return optionalIrisMessageData.get();
    }

    public IrisMessageDataViewData getMessageDataViewData(IrisMessageDataIdentifier messageDataId) {
        IrisMessageData messageData = this.getMessageData(messageDataId);
        IrisMessageDataProcessor processor = this.getMessageDataProcessor(messageData.getDiscriminator());
        return new IrisMessageDataViewData()
                .setId(messageData.getId().toString())
                .setDiscriminator(messageData.getDiscriminator())
                .setPayload(processor.getViewPayload(messageData.getPayload()));
    }


    public IrisMessageDataViewData getMessageDataImportSelectionViewData(IrisMessageDataIdentifier messageDataId, UUID importTargetId) {
        IrisMessageData messageData = this.getMessageData(messageDataId);
        IrisMessageDataProcessor processor = this.getMessageDataProcessor(messageData.getDiscriminator());
        return new IrisMessageDataViewData()
                .setId(messageData.getId().toString())
                .setDiscriminator(messageData.getDiscriminator())
                .setPayload(processor.getImportSelectionViewPayload(messageData.getPayload(), importTargetId));
    }

    public void importMessageData(IrisMessageDataIdentifier messageDataId) {
        IrisMessageData messageData = this.getMessageData(messageDataId);
        IrisMessageDataProcessor processor = this.getMessageDataProcessor(messageData.getDiscriminator());
        processor.importPayload(messageData.getPayload());
        messageData.setIsImported(true);
        this.dataRepository.save(messageData);
    }

    public void importMessageData(IrisMessageDataIdentifier messageDataId, UUID importTargetId, String importSelection) {
        IrisMessageData messageData = this.getMessageData(messageDataId);
        IrisMessageDataProcessor processor = this.getMessageDataProcessor(messageData.getDiscriminator());
        processor.importPayload(
                messageData.getPayload(),
                importTargetId,
                importSelection
        );
        messageData.setIsImported(true);
        this.dataRepository.save(messageData);
    }

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
				.filter(contact -> hdEpsNames.contains(contact.getId()) || contact.getName().contains(search)
						|| contact.getId().contains(search))
                .toList();
    }

    public IrisMessageHdContact getOwnHdContact() {
        return this.irisMessageClient.getOwnIrisMessageHdContact();
    }

    public IrisMessage sendMessage(IrisMessage message) throws IrisMessageException {
        this.irisMessageClient.createIrisMessage(message);
        return this.messageRepository.save(message);
    }

    public IrisMessage saveMessage(IrisMessage message) {
        return this.messageRepository.save(message);
    }

	public Optional<IrisMessage> updateReadState(IrisMessageIdentifier messageId, Boolean readed) {

		Optional<IrisMessage> message = findById(messageId);

		return message.map(it -> it.setRead(readed))
				.map(this::saveMessage);
	}
}
