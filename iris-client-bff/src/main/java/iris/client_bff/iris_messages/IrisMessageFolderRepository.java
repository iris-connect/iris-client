package iris.client_bff.iris_messages;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IrisMessageFolderRepository extends JpaRepository<IrisMessageFolder, IrisMessageFolder.IrisMessageFolderIdentifier> {

    Optional<IrisMessageFolder> findFirstByContextAndParentFolderIsNull(IrisMessageContext context);

    List<IrisMessageFolder> findAllByParentFolder(UUID parentFolder);

}
