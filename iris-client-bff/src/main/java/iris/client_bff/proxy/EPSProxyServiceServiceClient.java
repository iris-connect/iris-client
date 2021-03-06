package iris.client_bff.proxy;

import iris.client_bff.config.ProxyServiceProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.Period;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.googlecode.jsonrpc4j.IJsonRpcClient;

@Slf4j
@Service
@AllArgsConstructor
public class EPSProxyServiceServiceClient implements ProxyServiceClient {

	private final ProxyServiceProperties config;

	private final IJsonRpcClient proxyRpcClient;

	private String sendAnnouncement(String domain, Instant expirationTime) throws IRISAnnouncementException {
		var announcementDto = AnnouncementDto.builder()
				.domain(domain)
				.expiresAt(expirationTime)
				.proxy(config.getTargetProxy())
				.build();

		var methodName = config.getEpsName() + ".announceConnection";

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
	public String announce(Instant expirationTime) throws IRISAnnouncementException {
		var domain = UUID.randomUUID()
				+ "."
				+ config.getTargetSubdomain();
		return this.sendAnnouncement(domain, expirationTime);
	}

	@Override
	public String announce() throws IRISAnnouncementException {
		return this.announce(Instant.now().plus(Period.ofWeeks(1)));
	}

	@Override
	public String announceExplicitToken(String connectionAuthorizationToken) throws IRISAnnouncementException {
		var domain = connectionAuthorizationToken
				+ "."
				+ config.getTargetSubdomain();
		var oneWeekFromNow = Instant.now().plus(Period.ofWeeks(1));
		return this.sendAnnouncement(domain, oneWeekFromNow);
	}

	@Override
	public String abortAnnouncement(String domain) throws IRISAnnouncementException {
		var oneWeekAgo = Instant.now().minus(Period.ofWeeks(1));
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
