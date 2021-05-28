package iris.client_bff.auth.db.jwt;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HashedTokenRepository extends JpaRepository<HashedToken, String> {
	Optional<HashedToken> findByJwtTokenDigest(String token);

	void deleteByUserName(String userName);
}
