package iris.client_bff.dbms;

import org.testcontainers.containers.MSSQLServerContainer;

public class MSSQLServer extends MSSQLServerContainer<MSSQLServer> {

	private static final String URL = "spring.datasource.url";
	private static final String USERNAME = "spring.datasource.username";
	private static final String PASSWORD = "spring.datasource.password";

	private static final String IMAGE_VERSION = "mcr.microsoft.com/mssql/server:2019-latest";
	private static final MSSQLServer CONTAINER = new MSSQLServer().acceptLicense();

	private String url;
	private String username;
	private String password;

	private MSSQLServer() {
		super(IMAGE_VERSION);
	}

	public static MSSQLServer getInstance() {
		return CONTAINER;
	}

	@Override
	public void start() {

		url = System.getProperty(URL);
		username = System.getProperty(USERNAME);
		password = System.getProperty(PASSWORD);

		super.start();

		System.setProperty("spring.datasource.url", CONTAINER.getJdbcUrl());
		System.setProperty("spring.datasource.username", CONTAINER.getUsername());
		System.setProperty("spring.datasource.password", CONTAINER.getPassword());
	}

	@Override
	public void stop() {

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
	}
}
