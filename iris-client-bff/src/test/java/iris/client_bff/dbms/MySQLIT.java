package iris.client_bff.dbms;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

@ActiveProfiles({ "dev", "inttest", "test", "mysql" })
@SpringBootTest(properties = { "spring.datasource.url=jdbc:tc:mysql:8.0.26:///db",
		"spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver" })
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
class MySQLIT extends DatabasesystemIT {}
