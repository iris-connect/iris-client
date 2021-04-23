package de.healthIMIS.iris.client.auth.db;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "security.auth.db")
@ConstructorBinding
@Data
@AllArgsConstructor
@ConditionalOnProperty(
		value = "security.auth",
		havingValue = "db")
public class DbAuthProperties {

	private String adminUserName;

	private String adminUserPassword;
}
