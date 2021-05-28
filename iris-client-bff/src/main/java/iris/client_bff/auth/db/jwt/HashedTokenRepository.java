package iris.client_bff.auth.db.jwt;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface HashedTokenRepository extends JpaRepository<HashedToken, String> {
	Optional<HashedToken> findByJwtTokenDigest(String token);

	@Transactional
	void deleteByUserName(String userName);
}
