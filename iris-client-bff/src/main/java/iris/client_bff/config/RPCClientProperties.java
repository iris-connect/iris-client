package iris.client_bff.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

/**
 * @author Jens Kutzsche
 */
@ConfigurationProperties("eps-client")
@ConstructorBinding
@Validated
@Value
public class RPCClientProperties {

	private final @NotEmpty @Getter(value = AccessLevel.PACKAGE) String epsClientUrl;

	private final @NotEmpty @Getter(value = AccessLevel.PACKAGE) String proxyClientUrl;

	private final @NotEmpty String ownEndpoint;
}
