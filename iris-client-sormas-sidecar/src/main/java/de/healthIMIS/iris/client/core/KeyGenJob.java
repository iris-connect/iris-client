/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.healthIMIS.iris.client.core;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jens Kutzsche
 */
@Component
@Slf4j
@Profile("!inttest")
class KeyGenJob {

	private static final int DELETE_DELAY = 7;

	private final @NonNull IrisClientProperties properties;
	private final @NonNull IrisProperties connectProperties;
	private final @NonNull KeyStore keyStore;
	private final @NonNull RestTemplate rest;

	public KeyGenJob(
		@NonNull IrisClientProperties properties,
		IrisProperties connectProperties,
		@NonNull KeyStore keyStore,
		@Qualifier("iris-rest") @NonNull RestTemplate rest) {
		this.properties = properties;
		this.connectProperties = connectProperties;
		this.keyStore = keyStore;
		this.rest = rest;
	}

	@Scheduled(cron = "0 0 3 * * ?")
	@PostConstruct
	void run()
		throws NoSuchAlgorithmException, KeyStoreException, CertificateEncodingException, InvalidKeyException, IllegalStateException,
		NoSuchProviderException, SignatureException {

		var generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);

		var keyPair = generator.generateKeyPair();

		UUID keyReferenz = UUID.randomUUID();

		putKeyToServer(keyPair.getPublic(), keyReferenz);

		var certChain = new Certificate[1];
		certChain[0] = generateCertificate(keyPair);
		keyStore.setKeyEntry(keyReferenz.toString(), keyPair.getPrivate(), null, certChain);

		deleteOldKeys();
	}

	private void putKeyToServer(PublicKey pub, UUID keyReferenz) {

		var encoder = Base64.getEncoder();
		var pub64 = new StringBuilder();
//		pub64.append("-----BEGIN RSA PRIVATE KEY-----\n");
		pub64.append(encoder.encodeToString(pub.getEncoded()));
//		pub64.append("\n-----END RSA PRIVATE KEY-----\n");

		String departmentId = properties.getClientId().toString();

		log.trace("Keygen job - PUT to server is sent: {}", departmentId);

		var dto = DepartmentDto.of(properties.getDepartmentName(), keyReferenz.toString(), pub64.toString());

		var headers = new HttpHeaders();
		headers.setContentType(new MediaType(APPLICATION_JSON, UTF_8));

		rest.put(
			"https://{address}:{port}/hd/departments/{id}",
			new HttpEntity<>(dto, headers),
			connectProperties.getServerAddress().getHostName(),
			connectProperties.getServerPort(),
			departmentId);

		log.debug("Keygen job - PUT to server sent: {}", departmentId);
	}

	public X509Certificate generateCertificate(KeyPair keyPair)
		throws CertificateEncodingException, InvalidKeyException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException,
		SignatureException {
		Date startDate = new Date(System.currentTimeMillis() - 1 * 60 * 60 * 1000);
		Date endDate = new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000);

		var cert = new X509V3CertificateGenerator();
		cert.setSerialNumber(BigInteger.valueOf(1));   //or generate a random number  
		cert.setSubjectDN(new X509Principal("CN=localhost"));  //see examples to add O,OU etc  
		cert.setIssuerDN(new X509Principal("CN=localhost")); //same since it is self-signed  
		cert.setPublicKey(keyPair.getPublic());
		cert.setNotBefore(startDate);
		cert.setNotAfter(endDate);
		cert.setSignatureAlgorithm("SHA1WithRSAEncryption");
		PrivateKey signingKey = keyPair.getPrivate();
		return cert.generate(signingKey, "BC");
	}

	private void deleteOldKeys() throws KeyStoreException {

		var deleteDate = LocalDate.now().minusDays(DELETE_DELAY);
		var aliases = keyStore.aliases();

		while (aliases.hasMoreElements()) {

			var alias = aliases.nextElement();
			var date = keyStore.getCreationDate(alias);

			if (date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(deleteDate)) {
				keyStore.deleteEntry(alias);
			}
		}
	}

	@Data(staticConstructor = "of")
	static class DepartmentDto {

		private final String name;
		private final String keyReferenz;
		private final String key;
	}
}
