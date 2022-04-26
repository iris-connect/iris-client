package iris.client_bff.users.entities;

import iris.client_bff.core.Aggregate;
import iris.client_bff.core.IdWithUuid;
import iris.client_bff.users.entities.UserAccount.UserAccountIdentifier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "user_accounts")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount extends Aggregate<UserAccount, UserAccountIdentifier> {

	{
		id = UserAccountIdentifier.random();
	}

	@Column(nullable = false)
	private String userName;

	@Column(nullable = false)
	private String password;

	private String firstName;

	private String lastName;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	@EqualsAndHashCode(callSuper = false)
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // for JPA
	public static class UserAccountIdentifier extends IdWithUuid {

		private static final long serialVersionUID = 691185456738401495L;

		final UUID userId;

		public static UserAccountIdentifier random() {
			return of(UUID.randomUUID());
		}

		@Override
		protected UUID getBasicId() {
			return userId;
		}
	}
}
