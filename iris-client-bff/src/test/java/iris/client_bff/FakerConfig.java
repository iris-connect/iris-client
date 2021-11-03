package iris.client_bff;

import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.javafaker.Faker;

/**
 * @author Jens Kutzsche
 */
@Configuration
@Slf4j
public class FakerConfig {

	@Value("${iris.test.faker.seed:#{null}}")
	Long preConfiguredSeed;

	static Faker faker;

	@Bean
	public Faker getFaker() {

		if (faker == null) {

			var seed = preConfiguredSeed == null ? RandomUtils.nextLong() : preConfiguredSeed;

			log.info(
					"Faker is created with SEED = {}; The seed can be set with property iris.test.faker.seed or env IRIS_TEST_FAKER_SEED.",
					seed);

			faker = new Faker(Locale.GERMANY, new Random(seed));
		}

		return faker;
	}
}
