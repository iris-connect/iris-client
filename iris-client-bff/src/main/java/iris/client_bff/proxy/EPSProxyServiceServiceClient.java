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

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

@Slf4j
@Service
@AllArgsConstructor
public class EPSProxyServiceServiceClient implements ProxyServiceClient {

    private final ProxyServiceConfig config;

    private final JsonRpcHttpClient rpcClient;

    @Override
    public String announce() throws IRISAnnouncementException {

        var domain = UUID.randomUUID()
                + "."
                + config.getTargetSubdomain();

        var oneWeekFromNow = Instant.now().plus(7, ChronoUnit.DAYS);

        var announcementDto = AnnouncementDto.builder()
                .domain(domain)
                .expiresAt(oneWeekFromNow)
                .targetProxy(config.getTargetProxy())
                .build();

        // TODO use correct method name
        var methodName = config.getEpsName()
                + "."
                + "announceConnection";

        try {
            rpcClient.invoke(methodName, announcementDto);
            log.debug("Announced {} to {} till {}", announcementDto.getDomain(), announcementDto.getTargetProxy(), announcementDto.getExpiresAt());
        } catch (Throwable throwable) {
            throw new IRISAnnouncementException(throwable);
        }

        return domain;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnnouncementDto {
        private String domain;
        private Instant expiresAt;
        private String targetProxy;
    }
}
