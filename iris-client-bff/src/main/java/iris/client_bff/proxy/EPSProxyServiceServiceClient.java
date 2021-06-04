package iris.client_bff.proxy;

import iris.client_bff.config.ProxyServiceConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

@Slf4j
@Service
@AllArgsConstructor
public class EPSProxyServiceServiceClient implements ProxyServiceClient {

	private final ProxyServiceConfig config;

	private final JsonRpcHttpClient proxyRpcClient;

	private String sendAnnouncement(String domain, Instant expirationTime) throws IRISAnnouncementException {
		var announcementDto = AnnouncementDto.builder()
				.domain(domain)
				.expiresAt(expirationTime)
				.proxy(config.getTargetProxy())
				.build();

		var methodName = config.getEpsName()
				+ "."
				+ "announceConnection";

		try {
			proxyRpcClient.invoke(methodName, announcementDto);
			if (expirationTime.isBefore(Instant.now())) {
				log.debug("Aborted announcement {} to {}", announcementDto.getDomain(), announcementDto.getProxy());
			} else {
				log.debug("Announced {} to {} till {}", announcementDto.getDomain(), announcementDto.getProxy(),
						announcementDto.getExpiresAt());
			}
		} catch (Throwable throwable) {
			throw new IRISAnnouncementException(throwable);
		}

		return domain;
	}

	@Override
	public String announce() throws IRISAnnouncementException {
		var domain = UUID.randomUUID()
				+ "."
				+ config.getTargetSubdomain();
		var oneWeekFromNow = Instant.now().plus(7, ChronoUnit.DAYS);
		return this.sendAnnouncement(domain, oneWeekFromNow);
	}

	@Override
	public String abortAnnouncement(String domain) throws IRISAnnouncementException {
		var oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
		return this.sendAnnouncement(domain, oneWeekAgo);
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AnnouncementDto {
		private String domain;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
		private Instant expiresAt;
		private String proxy;
	}
}
