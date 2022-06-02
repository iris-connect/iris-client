package iris.client_bff.auth.db.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "allowed_tokens")
@Value
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
class AllowedToken implements Serializable {

	@Id
	private String jwtTokenDigest;

	@Column(nullable = false)
	private String userName;

	@Column(nullable = false)
	private Instant expirationTime;

	@Column(nullable = false)
	private Instant created;
}
