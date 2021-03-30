package de.healthIMIS.iris.client.search_client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "iris.location-service")
@Getter
@Setter
@NoArgsConstructor
public class SearchClientConfiguration {

    private String endpoint;

}
