package iris.client_bff.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Validated
public class DictionaryConfig {

    @Value("${dictionary.size: #{20000}}")
    private int size = 20000;

    private List<String> dictionary = new ArrayList<>();

    @Value("${dictionary.salt}")
    @NotBlank
    @Length(min = 32)
    private @NotNull @NotBlank String salt;

    public List<String> getDictionary() {
        return dictionary;
    }

    @SneakyThrows
    @PostConstruct
    protected void readDictionary() {

        File file = new File("dict.txt");
        if (!file.exists()) {
            List<String> shippedDictionary = readShippedDictionary();
            SecureRandom secureRandom = new SecureRandom(salt.getBytes());
            for (int i = 0; i < size; i++) {
                int randomIndex = secureRandom.nextInt(shippedDictionary.size());
                dictionary.add(shippedDictionary.get(randomIndex).toLowerCase());
                shippedDictionary.remove(randomIndex);
            }
            Files.write(Path.of(file.getPath()), dictionary, Charset.defaultCharset());
            return;
        }
        dictionary = Files.readAllLines(Path.of(file.getPath()));
    }

    @SneakyThrows
    private List<String> readShippedDictionary() {
        List<String> dict = new ArrayList<>();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("dict/deutsch.txt");
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        for (String line; (line = reader.readLine()) != null; ) {
            dict.add(line);
        }
        return dict;
    }
}
