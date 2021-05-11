package iris.client_bff.events.connection;

import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.MediaType.*;

import iris.client_bff.config.IrisClientProperties;
import iris.client_bff.config.IrisProperties;
import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.EventDataRequest.Status;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataRequestEndpointConnector {

  private final @Qualifier("iris-rest") @NotNull RestTemplate rest;
  private final @NonNull IrisProperties properties;
  private final @NonNull IrisClientProperties clientProperties;

  public void sendDataRequestToServer(EventDataRequest dataRequest) {

	log.trace("Request job - PUT to server is sent: {}", dataRequest.getId().toString());

	var headers = new HttpHeaders();
	headers.setContentType(new MediaType(APPLICATION_JSON, UTF_8));

	var dto = DataRequestDto.of(dataRequest, EnumSet.of(Feature.Guests), clientProperties.getClientId(),
		clientProperties.getRkiCode());

	rest.put("https://{address}:{port}/hd/data-requests/{id}", new HttpEntity<>(dto, headers),
		properties.getServerAddress().getHostName(), properties.getServerPort(), dataRequest.getId());

	log.debug("Request job - PUT to server sent: {}; Features = {}", dataRequest.getId().toString(),
		dto.getFeatures());
  }

  @Data
  static class DataRequestDto {

	private final String departmentId;

	private final String locationId;
	private final String providerId;

	private final Instant requestStart;
	private final Instant requestEnd;

	private final String requestDetails;

	private final Set<Feature> features;
	private final Status status;

	static DataRequestDto of(EventDataRequest request, Set<Feature> features, UUID departmentId, String rkiCode) {

	  var location = request.getLocation();
	  var locationId = location != null ? location.getLocationId() : null;
	  var providerId = location != null ? location.getProviderId() : null;

	  return new DataRequestDto(departmentId.toString(), locationId,
		  providerId, request.getRequestStart(), request.getRequestEnd(),
		  request.getRequestDetails(), features, request.getStatus());
	}
  }

  public enum Feature {
	Contacts_Events, Guests
  }
}
