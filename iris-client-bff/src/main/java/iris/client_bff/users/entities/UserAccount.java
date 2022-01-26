package iris.client_bff.users.entities;

import iris.client_bff.core.Aggregate;
import iris.client_bff.core.Id;
import iris.client_bff.users.entities.UserAccount.UserAccountIdentifier;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "user_accounts")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserAccount extends Aggregate<UserAccount, UserAccountIdentifier> {

	{
		id = UserAccountIdentifier.of(UUID.randomUUID());
	}

	@Column(nullable = false)
	private String userName;

	@Column(nullable = false)
	private String password;

	private String firstName;

	private String lastName;

	@Enumerated(EnumType.STRING) @Column(nullable = false)
	private UserRole role;

	@Embeddable
	@Getter
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class UserAccountIdentifier implements Id, Serializable {

		private static final long serialVersionUID = -8254677010830428881L;

		final UUID user_id;

		/**
		 * for JSON deserialization
		 */
		public static UserAccountIdentifier of(String uuid) {
			return of(UUID.fromString(uuid));
		}

		@Override
		public String toString() {
			return user_id.toString();
		}
	}
}
