package de.healthIMIS.iris.hd_server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({
	"dev",
	"inttest" })
class IrisHdServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
