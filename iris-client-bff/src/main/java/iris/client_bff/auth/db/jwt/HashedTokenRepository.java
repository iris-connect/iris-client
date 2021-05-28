package iris.client_bff.auth.db.jwt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashedTokenRepository extends JpaRepository<HashedToken, String> {
    Optional<HashedToken> findByJwtTokenDigest(String token);
}
