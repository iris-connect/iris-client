package iris.client_bff.sormas_integration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.net.InetAddress;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author Jens Kutzsche
 */
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("iris.sormas")
@ConditionalOnProperty("iris.sormas.user")
@Getter
public class IrisSormasProperties {

  private final InetAddress serverAddress;
  private final Integer serverPort;
  private final String user;
  private final String password;

  private @Setter String irisUserId;
}
