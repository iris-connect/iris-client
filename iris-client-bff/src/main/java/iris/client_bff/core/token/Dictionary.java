package iris.client_bff.core.token;

import iris.client_bff.config.DictionaryProperties;
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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Dictionary extends ArrayList<String> {

	private static final long serialVersionUID = -4408712121760885660L;

	@Value("classpath:dict/deutsch.txt")
	File shippedDictionaryFile;

	private final DictionaryProperties properties;

	@PostConstruct
	protected void readDictionary() {

		var size = properties.getSize();

		ensureCapacity(size);

		SecureRandom secureRandom = new SecureRandom();
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
