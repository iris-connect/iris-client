package de.healthIMIS.iris.client.auth.db.model;


import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@Table(name = "user_accounts")
public class UserAccount {

    @Id
    private UUID user_id = UUID.randomUUID();

    private String userName;

    private String password;

}
