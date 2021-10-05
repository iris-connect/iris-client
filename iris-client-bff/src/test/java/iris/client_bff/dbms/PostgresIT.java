package iris.client_bff.dbms;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

@ActiveProfiles({ "dev", "inttest", "test", "prod_db" })
@SpringBootTest(properties = { "spring.datasource.url=jdbc:tc:postgresql:13.2-alpine:///db",
		"spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver" })
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
class PostgresIT extends DatabasesystemIT {}
