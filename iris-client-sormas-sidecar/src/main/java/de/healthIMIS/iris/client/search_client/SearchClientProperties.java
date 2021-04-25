package de.healthIMIS.iris.client.search_client;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "iris.location-service")
@Getter
@AllArgsConstructor
public class SearchClientProperties {

	private String endpoint;

}
