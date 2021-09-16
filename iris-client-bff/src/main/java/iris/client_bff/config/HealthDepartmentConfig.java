package iris.client_bff.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@RequiredArgsConstructor
@ConfigurationProperties("hd")
public class HealthDepartmentConfig {
    private String zipCode;
}
