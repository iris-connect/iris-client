package iris.client_bff.auth.db.jwt;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "allowed_tokens")
@Data
public class AllowedToken implements Serializable {

	@Id
	private String jwtTokenDigest;

	@Column(nullable = false)
	private String userName;

	@Column(nullable = false)
	private Instant expirationTime;

	@Column(nullable = false)
	private Instant created;
}
