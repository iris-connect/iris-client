package de.healthIMIS.sormas.client.api;

import de.healthIMIS.sormas.client.invoker.ApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen",
		date = "2021-01-28T10:11:24.562208+01:00[Europe/Berlin]")
@Component("de.healthIMIS.sormas.client.api.BAGExportControllerApi")
public class BAGExportControllerApi {
	private ApiClient apiClient;

	public BAGExportControllerApi() {
		this(new ApiClient());
	}

	@Autowired
	public BAGExportControllerApi(ApiClient apiClient) {
		this.apiClient = apiClient;
	}

	public ApiClient getApiClient() {
		return apiClient;
	}

	public void setApiClient(ApiClient apiClient) {
		this.apiClient = apiClient;
	}

	/**
	 * <p>
	 * <b>0</b> - default response
	 * 
	 * @param body The body parameter
	 * @throws RestClientException if an error occurs while attempting to invoke the API
	 */
	public void exportCases(String body) throws RestClientException {
		Object postBody = body;
		String path = UriComponentsBuilder.fromPath("/bagexport/cases").build().toUriString();

		final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
		final HttpHeaders headerParams = new HttpHeaders();
		final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

		final String[] accepts = { "application/octet-stream" };
		final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
		final String[] contentTypes = { "application/json; charset&#x3D;UTF-8" };
		final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

		String[] authNames = new String[] { "basicAuth" };

		ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
		apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType,
				authNames, returnType);
	}

	/**
	 * <p>
	 * <b>0</b> - default response
	 * 
	 * @param body The body parameter
	 * @throws RestClientException if an error occurs while attempting to invoke the API
	 */
	public void exportContacts(String body) throws RestClientException {
		Object postBody = body;
		String path = UriComponentsBuilder.fromPath("/bagexport/contacts").build().toUriString();

		final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
		final HttpHeaders headerParams = new HttpHeaders();
		final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

		final String[] accepts = { "application/octet-stream" };
		final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
		final String[] contentTypes = { "application/json; charset&#x3D;UTF-8" };
		final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

		String[] authNames = new String[] { "basicAuth" };

		ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
		apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType,
				authNames, returnType);
	}
}
