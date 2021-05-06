package iris.client_bff.search_client.web;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import iris.client_bff.config.RPCClientConfig;
import iris.client_bff.search_client.SearchClient;
import iris.client_bff.search_client.web.dto.LocationList;
import iris.client_bff.search_client.web.dto.SearchRequestDto;
import lombok.RequiredArgsConstructor;
import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;

@RestController
@Validated
@RequiredArgsConstructor
public class LocationSearchController {

    private final SearchClient searchClient;

    private final @NotNull JsonRpcHttpClient rpcClient;

    @GetMapping("/search/{search_keyword}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LocationList> searchSearchKeywordGet(
            @Size(min = 4) @PathVariable("search_keyword") String searchKeyword) throws Throwable {

        SearchRequestDto search = SearchRequestDto.builder().searchKeyword(searchKeyword).build();

        LocationList locations = rpcClient.invoke("ls-1.searchForLocation", search, LocationList.class);

        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
}
