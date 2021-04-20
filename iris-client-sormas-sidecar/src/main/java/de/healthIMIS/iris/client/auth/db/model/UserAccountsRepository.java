package de.healthIMIS.iris.client.auth.db.model;

import de.healthIMIS.iris.client.data_request.DataRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAccountsRepository extends CrudRepository<UserAccount, UUID> {

    Optional<UserAccount> findByUserName(String userName);

}
