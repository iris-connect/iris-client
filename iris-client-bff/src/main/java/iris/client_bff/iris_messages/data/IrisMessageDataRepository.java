package iris.client_bff.iris_messages.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IrisMessageDataRepository extends JpaRepository<IrisMessageData, IrisMessageData.IrisMessageDataIdentifier> {

}
