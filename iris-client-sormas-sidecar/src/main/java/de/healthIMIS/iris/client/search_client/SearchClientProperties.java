package de.healthIMIS.iris.client.search_client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

@ConstructorBinding
@ConfigurationProperties(prefix = "iris.location-service")
@Getter
@AllArgsConstructor
public class SearchClientProperties {

    private String endpoint;

}
