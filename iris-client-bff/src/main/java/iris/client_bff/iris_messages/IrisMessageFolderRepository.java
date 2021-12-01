package iris.client_bff.iris_messages;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IrisMessageFolderRepository extends JpaRepository<IrisMessageFolder, IrisMessageFolder.IrisMessageFolderIdentifier> {

    IrisMessageFolder findFirstByContextAndParentFolderIsNull(IrisMessageContext context);

}
