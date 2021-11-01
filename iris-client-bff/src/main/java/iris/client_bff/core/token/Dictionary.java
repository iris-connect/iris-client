package iris.client_bff.core.token;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@RequiredArgsConstructor
@Validated
public class Dictionary extends ArrayList<String> {

	private static final long serialVersionUID = -4408712121760885660L;

	@Value("${dictionary.size: #{20000}}")
	private int size = 20000;

	@Value("${dictionary.salt}")
	@NotBlank
	@Length(min = 32)
	private String salt;

	@Value("classpath:dict/deutsch.txt")
	File shippedDictionaryFile;

	@PostConstruct
	protected void readDictionary() {

		ensureCapacity(size);

		SecureRandom secureRandom = new SecureRandom(salt.getBytes());
		List<String> shippedDictionary = readShippedDictionary();

		IntStream.generate(() -> secureRandom.nextInt(shippedDictionary.size()))
				.distinct()
				.limit(size)
				.mapToObj(shippedDictionary::get)
				.map(String::toLowerCase)
				.forEach(this::add);
	}

	@SneakyThrows
	private List<String> readShippedDictionary() {
		return Files.readAllLines(shippedDictionaryFile.toPath(), StandardCharsets.UTF_8);
	}
}
