package iris.client_bff.dbms;

import org.testcontainers.containers.PostgreSQLContainer;

public class Postgres extends PostgreSQLContainer<Postgres> {

	private static final String DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";
	private static final String URL = "spring.datasource.url";
	private static final String USERNAME = "spring.datasource.username";
	private static final String PASSWORD = "spring.datasource.password";
	private static final String DIALECT = "spring.jpa.properties.hibernate.dialect";

	private static final String IMAGE_VERSION = "postgres:13.2-alpine";
	private static final Postgres CONTAINER = new Postgres();

	private String driverClassName;
	private String url;
	private String username;
	private String password;
	private String dialect;

	private Postgres() {
		super(IMAGE_VERSION);
	}

	public static Postgres getInstance() {
		return CONTAINER;
	}

	@Override
	public void start() {

		driverClassName = System.getProperty(DRIVER_CLASS_NAME);
		url = System.getProperty(URL);
		username = System.getProperty(USERNAME);
		password = System.getProperty(PASSWORD);
		dialect = System.getProperty(DIALECT);

		super.start();

		System.setProperty(DRIVER_CLASS_NAME, CONTAINER.getJdbcDriverInstance().getClass().getName());
		System.setProperty(URL, CONTAINER.getJdbcUrl());
		System.setProperty(USERNAME, CONTAINER.getUsername());
		System.setProperty(PASSWORD, CONTAINER.getPassword());
		System.setProperty(DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
	}

	@Override
	public void stop() {

		if (driverClassName == null) {
			System.clearProperty(DRIVER_CLASS_NAME);
		} else {
			System.setProperty(DRIVER_CLASS_NAME, driverClassName);
		}
		if (url == null) {
			System.clearProperty(URL);
		} else {
			System.setProperty(URL, url);
		}
		if (username == null) {
			System.clearProperty(USERNAME);
		} else {
			System.setProperty(USERNAME, username);
		}
		if (password == null) {
			System.clearProperty(PASSWORD);
		} else {
			System.setProperty(PASSWORD, password);
		}
		if (dialect == null) {
			System.clearProperty(DIALECT);
		} else {
			System.setProperty(DIALECT, dialect);
		}
	}
}
