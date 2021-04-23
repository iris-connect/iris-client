package de.healthIMIS.iris.client.config;

import de.healthIMIS.iris.client.core.IrisClientProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;

import javax.annotation.PostConstruct;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class EncryptionConfig {

	@Bean
	@ConditionalOnProperty(name = "iris.client.key-store", matchIfMissing = true)
	KeyStore getKeyStore(IrisClientProperties properties)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

		char[] password = properties.getKeyStorePassword().toCharArray();
		ks.load(null, password);

		return ks;
	}

	@PostConstruct
	public void initBouncyCastle() {

		Security.addProvider(new BouncyCastleProvider());
		Security.setProperty("crypto.policy", "unlimited");
	}
}
