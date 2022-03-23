package iris.client_bff.config;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.StringArraySerializer;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

@Configuration
@RequiredArgsConstructor
public class RPCClientConfig {

	private static final int CONN_TIMEOUT = 3 * 1000;
	private static final int READ_TIMEOUT = 0; // Is infinite here, since we rely on EPS's timeouts after a successful
																							// connection.

	private final RPCClientProperties properties;
	private final Jackson2ObjectMapperBuilder objectMapperBuilder;

	@Bean
	public JsonRpcHttpClient epsRpcClient() throws MalformedURLException {
		return epsRpcClient(properties.getEpsClientUrl());
	}

	@Bean
	public JsonRpcHttpClient proxyRpcClient() throws MalformedURLException {
		return epsRpcClient(properties.getProxyClientUrl());
	}

	private JsonRpcHttpClient epsRpcClient(String clientUrl) throws MalformedURLException {

		var client = new JsonRpcHttpClient(configureObjectMapper(), new URL(clientUrl), Map.of());

		// NoopHostnameVerifier is needed because the internal eps address is not necessarily part of certs SAN
		client.setHostNameVerifier(new NoopHostnameVerifier());
		client.setConnectionTimeoutMillis(CONN_TIMEOUT);
		client.setReadTimeoutMillis(READ_TIMEOUT);

		// This is SO! important
		client.setContentType("application/json");

		return client;
	}

	private ObjectMapper configureObjectMapper() {

		var module = new SimpleModule()
				.addSerializer(String[].class, new EmptyStringArraySerializer());

		return objectMapperBuilder.build().registerModule(module);
	}

	/**
	 * If no parameters are passed in a JSON-RPC method call, then with JSON-RPC in general or jsonrpc4j in particular, an
	 * empty object must be serialized so that at the receiver the request is processed properly.
	 * 
	 * @author Jens Kutzsche
	 */
	final class EmptyStringArraySerializer extends JsonSerializer<String[]> {
		@Override
		public void serialize(String[] value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

			if (ArrayUtils.isEmpty(value)) {
				gen.writeStartObject();
				gen.writeEndObject();
			} else {
				StringArraySerializer.instance.serialize(value, gen, serializers);
			}
		}
	}
}
