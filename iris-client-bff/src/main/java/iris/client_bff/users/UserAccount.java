package iris.client_bff.users;

import iris.client_bff.core.model.Aggregate;
import iris.client_bff.core.model.IdWithUuid;
import iris.client_bff.users.UserAccount.UserAccountIdentifier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.time.Instant;
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
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

	private boolean locked;
	private Instant deletedAt;

	public boolean isEnabled() {
		return getDeletedAt() == null;
	}

	public boolean isAdmin() {
		return getRole() == UserRole.ADMIN;
	}

	public UserAccount markDeleted() {

		var name = getUserName();

		setDeletedAt(Instant.now());
		setUserName(getId().toString());

		registerEvent(UserMarkedDeleted.of(id, name));

		return this;
	}

	public UserAccount markLoginIncompatiblyUpdated() {

		registerEvent(UserLoginIncompatiblyUpdated.of(id, getUserName()));

		return this;
	}

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

	@Value(staticConstructor = "of")
	public static class UserMarkedDeleted {

		UserAccountIdentifier userId;
		String userName;
	}

	@Value(staticConstructor = "of")
	public static class UserLoginIncompatiblyUpdated {

		UserAccountIdentifier userId;
		String userName;
	}
}
