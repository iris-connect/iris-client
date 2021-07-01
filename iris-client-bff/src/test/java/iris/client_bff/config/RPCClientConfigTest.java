package iris.client_bff.config;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class RPCClientConfigTest {

	@Bean
	public JsonRpcHttpClient epsRpcClient() {
		return Mockito.mock(JsonRpcHttpClient.class);
	}

	@Bean
	public JsonRpcHttpClient proxyRpcClient() {
		return Mockito.mock(JsonRpcHttpClient.class);
	}
}
