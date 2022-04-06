package iris.client_bff.iris_messages;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IrisMessageRepository extends JpaRepository<IrisMessage, IrisMessage.IrisMessageIdentifier> {

	@Query("select count(m) from IrisMessage m where m.isRead = false and m.folder.id = :folderId")
	int getCountUnreadByFolderId(IrisMessageFolder.IrisMessageFolderIdentifier folderId);

	int countByIsReadFalse();

	Page<IrisMessage> findAllByFolderIdOrderByIsReadAsc(IrisMessageFolder.IrisMessageFolderIdentifier folder,
			Pageable pageable);

}
