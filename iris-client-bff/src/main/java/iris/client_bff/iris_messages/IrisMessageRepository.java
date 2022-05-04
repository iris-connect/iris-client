package iris.client_bff.iris_messages;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

public interface IrisMessageRepository extends JpaRepository<IrisMessage, IrisMessage.IrisMessageIdentifier> {

	@Query("select count(m) from IrisMessage m where m.isRead = false and m.folder.id = :folderId")
	int getCountUnreadByFolderId(IrisMessageFolder.IrisMessageFolderIdentifier folderId);

	int countByIsReadFalse();

	Page<IrisMessage> findAllByFolderIdOrderByIsReadAsc(IrisMessageFolder.IrisMessageFolderIdentifier folder,
			Pageable pageable);

	/**
	 * Returns the {@link IrisMessage}s created before the given {@link Instant}.
	 *
	 * @param refDate must not be {@literal null}.
	 * @return
	 */
	Streamable<IrisMessage> findByMetadataCreatedIsBefore(Instant refDate);

	@Query("select count(m)>0 from IrisMessage m where m.metadata.createdBy = :userId OR m.metadata.lastModifiedBy = :userId")
	boolean isReferencedToUser(UUID userId);
}
