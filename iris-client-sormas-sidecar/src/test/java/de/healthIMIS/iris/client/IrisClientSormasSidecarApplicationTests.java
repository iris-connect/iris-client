package de.healthIMIS.iris.client;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({
	"dev",
	"inttest" })
class IrisClientSormasSidecarApplicationTests {

	@Test
	void contextLoads() {
	}

}
