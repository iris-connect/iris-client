package iris.client_bff.iris_messages;

import org.springframework.data.jpa.repository.JpaRepository;

import iris.client_bff.iris_messages.IrisMessageFolder.IrisMessageFolderIdentifier;

import java.util.List;
import java.util.Optional;

public interface IrisMessageFolderRepository extends JpaRepository<IrisMessageFolder, IrisMessageFolder.IrisMessageFolderIdentifier> {

    Optional<IrisMessageFolder> findFirstByContextAndParentFolderIsNull(IrisMessageContext context);

    List<IrisMessageFolder> findAllByParentFolder(IrisMessageFolderIdentifier parentFolder);

}
