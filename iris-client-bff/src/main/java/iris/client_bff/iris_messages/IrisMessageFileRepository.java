package iris.client_bff.iris_messages;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IrisMessageFileRepository extends JpaRepository<IrisMessageFile, IrisMessageFile.IrisMessageFileIdentifier> {

}
