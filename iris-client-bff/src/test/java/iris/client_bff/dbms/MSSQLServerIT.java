package iris.client_bff.dbms;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

@ActiveProfiles({ "dev", "inttest", "test", "mssql" })
@SpringBootTest(properties = { "spring.datasource.url=jdbc:tc:sqlserver:2019-latest:///db",
		"spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver" })
@TestConstructor(autowireMode = AutowireMode.ALL)
class MSSQLServerIT extends DatabasesystemIT {}
