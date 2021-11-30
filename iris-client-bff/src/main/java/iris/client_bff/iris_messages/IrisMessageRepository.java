package iris.client_bff.iris_messages;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IrisMessageRepository extends JpaRepository<IrisMessage, IrisMessage.IrisMessageIdentifier> {

    @Query("select count(m) from IrisMessage m where (m.isRead is null or m.isRead = false) and (:folder is null or m.folder = :folder)")
    int getCountUnread(String folder);

    Page<IrisMessage> findAllByFolderId(IrisMessageFolder.IrisMessageFolderIdentifier folder, Pageable pageable);

}
