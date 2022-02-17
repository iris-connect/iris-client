package iris.client_bff.iris_messages;

import iris.client_bff.iris_messages.IrisMessageFile.IrisMessageFileIdentifier;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IrisMessageFileRepository
	extends JpaRepository<IrisMessageFile, IrisMessageFileIdentifier> {}
