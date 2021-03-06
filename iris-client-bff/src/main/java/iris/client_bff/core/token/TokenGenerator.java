package iris.client_bff.core.token;

import iris.client_bff.config.CentralConfigurationService;
import lombok.RequiredArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenGenerator {

	public static final int ITERATIONS = 10000;

	private final Dictionary dictionary;
	private final CentralConfigurationService configService;

	public IdentifierToken generateIdentifierToken() {

		String readableToken = generateReadableToken(dictionary, 4);

		int keyLength = configService.getCatLength() * 8;

		byte[] connectionAuthorizationToken = hashPassword(readableToken.toCharArray(),
				configService.getCatSalt().getBytes(), ITERATIONS, keyLength);

		keyLength = configService.getDatLength() * 8;

		byte[] dataAuthorizationToken = hashPassword(readableToken.toCharArray(), configService.getDatSalt().getBytes(),
				ITERATIONS, keyLength);

		return IdentifierToken.builder()
				.readableToken(readableToken)
				.connectionAuthorizationToken(Hex.encodeHexString(connectionAuthorizationToken))
				.dataAuthorizationToken(Hex.encodeHexString(dataAuthorizationToken))
				.build();
	}

	private byte[] hashPassword(final char[] password, final byte[] salt, final int iterations, final int keyLength) {

		try {

			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
			SecretKey key = skf.generateSecret(spec);

			return key.getEncoded();

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

	private String generateReadableToken(Dictionary dict, int numberOfElements) {

		SecureRandom secureRandom = new SecureRandom();

		String tokenPart = IntStream.generate(() -> secureRandom.nextInt(dict.size()))
				.mapToObj(dict::get)
				.map(String::toLowerCase)
				.distinct() // select only different words; the list can contains equal words in different capitalization
				.limit(numberOfElements)
				.collect(Collectors.joining("-"));

		return tokenPart + "-" + configService.getAbbreviation();
	}
}
