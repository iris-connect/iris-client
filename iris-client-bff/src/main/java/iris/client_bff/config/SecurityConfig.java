package iris.client_bff.config;

import dev.samstevens.totp.code.HashingAlgorithm;

import java.security.Security;

import javax.annotation.PostConstruct;

import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@PostConstruct
	public void initBouncyCastle() {

		Security.addProvider(new BouncyCastleFipsProvider());
		Security.setProperty("crypto.policy", "unlimited");
	}

	@Bean
	public HashingAlgorithm totpHashingAlgorithm() {
		return HashingAlgorithm.SHA256;
	}
}
