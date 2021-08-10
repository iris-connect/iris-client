package iris.client_bff.auth.db.login_attempts;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Jens Kutzsche
 */
@Entity
@Table(name = "login_attempts")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "reference", callSuper = false)
public class LoginAttempts {

	@Id
	private String reference;

	@Column(nullable = false)
	private @Setter int attempts;

	@Column(nullable = false)
	private @Setter int nextWarningThreshold;

	@Column(nullable = false)
	private @Setter Duration waitingTime;

	@CreatedDate
	private Instant created;
	@LastModifiedDate
	private Instant lastModified;
}
