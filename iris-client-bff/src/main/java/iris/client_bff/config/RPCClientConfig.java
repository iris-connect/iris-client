package iris.client_bff.config;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.StringArraySerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

@Configuration
@RequiredArgsConstructor
public class RPCClientConfig {

	private static final int CONN_TIMEOUT = 3 * 1000;
	private static final int READ_TIMEOUT = 0; // Is infinite here, since we rely on EPS's timeouts after a successful
																							// connection.

	private final RPCClientProperties properties;

	@Bean
	public JsonRpcHttpClient epsRpcClient()
			throws NoSuchAlgorithmException, KeyManagementException, MalformedURLException {
		return epsRpcClient(properties.getEpsClientUrl());
	}

	@Bean
	public JsonRpcHttpClient proxyRpcClient()
			throws NoSuchAlgorithmException, KeyManagementException, MalformedURLException {
		return epsRpcClient(properties.getProxyClientUrl());
	}

	private JsonRpcHttpClient epsRpcClient(String clientUrl)
			throws MalformedURLException, NoSuchAlgorithmException, KeyManagementException {

		var jacksonObjectMapper = new ObjectMapper();
		jacksonObjectMapper.registerModule(new JavaTimeModule());

		var module = new SimpleModule();
		module.addDeserializer(Status.class, new StatusDeserializer());
		module.addSerializer(String[].class, new EmptyStringArraySerializer());
		jacksonObjectMapper.registerModule(module);

		var client = new JsonRpcHttpClient(
				jacksonObjectMapper,
				new URL(clientUrl),
				new HashMap<>());

		// NoopHostnameVerifier is needed because the internal eps address is not necessarily part of certs SAN
		client.setHostNameVerifier(new NoopHostnameVerifier());
		client.setConnectionTimeoutMillis(CONN_TIMEOUT);
		client.setReadTimeoutMillis(READ_TIMEOUT);

		// This is SO! important
		client.setContentType("application/json");

		return client;
	}

	private SSLContext getAllCertsTrustedSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					@Override
					public void checkClientTrusted(
							X509Certificate[] certs, String authType) {}

					@Override
					public void checkServerTrusted(
							X509Certificate[] certs, String authType) {}
				}
		};

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		return sc;
	}

	/**
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

	/**
	 * @author Jens Kutzsche
	 */
	class StatusDeserializer extends StdDeserializer<Status> {

		private static final long serialVersionUID = -314531032736552403L;

		public StatusDeserializer() {
			this(Status.class);
		}

		public StatusDeserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public Status deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

			JsonNode node = jp.getCodec().readTree(jp);

			return new Status(
					Optional.ofNullable(node.get("status"))
							.map(JsonNode::asText)
							.orElse(""),
					Optional.ofNullable(node.get("description"))
							.map(JsonNode::asText)
							.orElse(""));
		}
	}
}
