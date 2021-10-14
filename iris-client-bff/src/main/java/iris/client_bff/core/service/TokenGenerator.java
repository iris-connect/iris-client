package iris.client_bff.core.service;

import iris.client_bff.config.DictionaryConfig;
import iris.client_bff.config.TokenConfig;
import iris.client_bff.core.IdentifierToken;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TokenGenerator {

    public static final int ITERATIONS = 10000;
    private final DictionaryConfig dictionaryConfig;

    private final TokenConfig tokenConfig;

    public IdentifierToken generateIdentifierToken() {

        String readableToken = generateReadableToken(dictionaryConfig.getDictionary(), 4);

        int keyLength = tokenConfig.getCatLength() * 8;

        byte[] connectionAuthorizationToken = hashPassword(readableToken.toCharArray(), tokenConfig.getCatSalt().getBytes(), ITERATIONS, keyLength);

        keyLength = tokenConfig.getDatLength() * 8;

        byte[] dataAuthorizationToken = hashPassword(readableToken.toCharArray(), tokenConfig.getDatSalt().getBytes(), ITERATIONS, keyLength);

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
            byte[] res = key.getEncoded();
            return res;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }


    private String generateReadableToken(List<String> dict, int numberOfElements) {
        Random rand = new Random();
        String token = "";
        for (int i = 0; i < numberOfElements; i++) {
            int randomIndex = rand.nextInt(dict.size());
            token = token.concat(dict.get(randomIndex).toLowerCase());
            if (i != (numberOfElements - 1))
                token = token.concat("-");
            dict.remove(randomIndex);
        }
        return token;
    }

}
