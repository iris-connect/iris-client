package iris.client_bff.core.util;

import org.flywaydb.core.internal.logging.slf4j.Slf4jLog;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BusinessProcessInfo {

    public String getNextId() {
        return UUID.randomUUID().toString();
    }

    public String generateLogMessage(String processType, String traceId) {
        return String.format("Process initiated: %s; traceId: %s; User: %s", processType, traceId,
                SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public void logProcess(Logger logger, String processType, String traceId) {
        logger.info(generateLogMessage(processType, traceId));
    }
    public void logProcessWithData(Logger logger, String processType, String traceId, String additionalData) {
			logger.info("{}; {}", generateLogMessage(processType, traceId), additionalData);
    }

    public void logProcessWithConnToken(Logger logger, String processType, String traceId, String proxyEndpoint) {
        logger.info("{}; connection auth token: {}",
                generateLogMessage(processType, traceId),
                proxyEndpoint.substring(0, proxyEndpoint.indexOf('.')));
    }
}