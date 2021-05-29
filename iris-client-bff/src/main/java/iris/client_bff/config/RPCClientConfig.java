package iris.client_bff.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("eps-client")
@Getter
public class RPCClientConfig {

  private final @NonNull String epsClientUrl;

  private final @NonNull String proxyClientUrl;

  @Bean
  public JsonRpcHttpClient epsRpcClient() throws NoSuchAlgorithmException, KeyManagementException, MalformedURLException {
    return epsRpcClient(epsClientUrl);
  }

  @Bean
  public JsonRpcHttpClient proxyRpcClient() throws NoSuchAlgorithmException, KeyManagementException, MalformedURLException {
    return epsRpcClient(proxyClientUrl);
  }

  public JsonRpcHttpClient epsRpcClient(String clientUrl) throws MalformedURLException, NoSuchAlgorithmException, KeyManagementException {

    ObjectMapper jacksonObjectMapper = new ObjectMapper();
    jacksonObjectMapper.registerModule(new JavaTimeModule());

    JsonRpcHttpClient client = new JsonRpcHttpClient(
        jacksonObjectMapper,
        new URL(clientUrl),
        new HashMap<>());

    // Can be removed when we include the root certs
    SSLContext sc = getAllCertsTrustedSSLContext();
    client.setSslContext(sc);
    client.setHostNameVerifier(new NoopHostnameVerifier());

    // This is SO! important
    client.setContentType("application/json");

    return client;
  }

  private SSLContext getAllCertsTrustedSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
    TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
          public X509Certificate[] getAcceptedIssuers() {
            return null;
          }

          public void checkClientTrusted(
              X509Certificate[] certs, String authType) {}

          public void checkServerTrusted(
              X509Certificate[] certs, String authType) {}
        }
    };

    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    return sc;
  }
}
