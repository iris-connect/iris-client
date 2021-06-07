package iris.client_bff.core.web.error;

import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class ResponseStatusExceptionMapper {

    public ResponseStatusException mapError(Exception ex) {
        // defaults
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String reason;
        String internalMsg = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

        if(ex instanceof IRISSearchException) {

            log.info("Search failed with exception: {}", internalMsg);
            status = HttpStatus.SERVICE_UNAVAILABLE;
            reason = ErrorMessages.LOCATION_SEARCH;

        } else if(ex instanceof IRISDataRequestException) {

            log.info("Data request failed with exception: {}", internalMsg);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            reason = ErrorMessages.EVENT_DATA_REQUEST_CREATION;

        } else {

            log.warn("Unmapped exception occurred", ex);
            reason = internalMsg;

        }

        return new ResponseStatusException(status, reason);
    }
}
