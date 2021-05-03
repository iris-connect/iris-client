package de.healthIMIS.iris.client.users.entities;

import lombok.Data;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "user_accounts")
public class UserAccount {

	@Id
	private UUID user_id = UUID.randomUUID();

	@Column(nullable = false)
	private String userName;

	@Column(nullable = false)
	private String password;

	private String firstName;

	private String lastName;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;
}
