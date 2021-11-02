package iris.client_bff.status.web;

import iris.client_bff.status.web.dto.AppStatus;
import iris.client_bff.status.web.dto.ErrorStatusMatch;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@AllArgsConstructor
public class AppStatusResolver {

    private static final AppStatus STATUS_OK = new AppStatus(AppStatus.Status.OK, "Die Status Prüfung war erfolgreich.");

    private static final AppStatus STATUS_UNKNOWN_ERROR = new AppStatus(AppStatus.Status.ERROR, "Bei der Prüfung des Status ist ein unbekannter Fehler aufgetreten.");

    private static final Pattern PERMISSION_DENIED_PING_CLIENT_REGEX = Pattern.compile("Permission denied for method '_ping' and client");
    private static final AppStatus PERMISSION_DENIED_PING_CLIENT_STATUS = new AppStatus(AppStatus.Status.WARNING, "Die Status Anfrage wurde durch den App Anbieter abgelehnt oder ist derzeit nicht möglich.");

    private static final Pattern CERTIFICATE_INVALID_CLIENT_REGEX = Pattern.compile("certificate is valid for [\\w.-]+, not [\\w.-]+");
    private static final AppStatus CERTIFICATE_INVALID_CLIENT_STATUS = new AppStatus(AppStatus.Status.ERROR, "Das Zertfikat ist gültig, ist aber nicht der angefragten App Anbieter ID zugeordnet.");

    private static final Pattern CERTIFICATE_INVALID_DATE_REGEX = Pattern.compile("certificate has expired or is not yet valid");
    private static final AppStatus CERTIFICATE_INVALID_DATE_STATUS = new AppStatus(AppStatus.Status.ERROR, "Das Zertfikat ist abgelaufen oder noch nicht gültig.");

    private static final Pattern CONNECTION_CLOSED_BY_REMOTE_HOST_REGEX = Pattern.compile("Eine vorhandene Verbindung wurde vom Remotehost geschlossen");
    private static final AppStatus CONNECTION_CLOSED_BY_REMOTE_HOST_STATUS = new AppStatus(AppStatus.Status.ERROR, "Eine vorhandene Verbindung wurde vom Remotehost geschlossen.");

    private static final Pattern ACCESS_DENIED_REGEX = Pattern.compile("Es konnte keine Verbindung hergestellt werden, da der Zielcomputer die Verbindung verweigerte");
    private static final AppStatus ACCESS_DENIED_STATUS = new AppStatus(AppStatus.Status.ERROR, "Es konnte keine Verbindung hergestellt werden, da der Zielcomputer die Verbindung verweigerte.");

    private static final Pattern NO_SUCH_HOST_REGEX = Pattern.compile("no such host");
    private static final AppStatus NO_SUCH_HOST_STATUS = new AppStatus(AppStatus.Status.ERROR, "Der Host des App Anbieters kann nicht ermittelt werden.");

    private static final Pattern TIMEOUT_REGEX = Pattern.compile("Error while dialing dial tcp [\\d.:\\[\\]]+: i/o timeout");
    private static final AppStatus TIMEOUT_STATUS = new AppStatus(AppStatus.Status.ERROR, "Es gab eine Zeitüberschreitung bei der Anfrage.");

    private static final List<ErrorStatusMatch> ERROR_STATUS_MATCH_LIST = List.of(
            new ErrorStatusMatch(PERMISSION_DENIED_PING_CLIENT_REGEX, PERMISSION_DENIED_PING_CLIENT_STATUS),
            new ErrorStatusMatch(CERTIFICATE_INVALID_CLIENT_REGEX, CERTIFICATE_INVALID_CLIENT_STATUS),
            new ErrorStatusMatch(CERTIFICATE_INVALID_DATE_REGEX, CERTIFICATE_INVALID_DATE_STATUS),
            new ErrorStatusMatch(CONNECTION_CLOSED_BY_REMOTE_HOST_REGEX, CONNECTION_CLOSED_BY_REMOTE_HOST_STATUS),
            new ErrorStatusMatch(ACCESS_DENIED_REGEX, ACCESS_DENIED_STATUS),
            new ErrorStatusMatch(NO_SUCH_HOST_REGEX, NO_SUCH_HOST_STATUS),
            new ErrorStatusMatch(TIMEOUT_REGEX, TIMEOUT_STATUS)
    );

    public AppStatus getStatusOk() {
        return STATUS_OK;
    }

    public AppStatus getStatusByErrorMessage(String message) {
        for (ErrorStatusMatch statusMatch : ERROR_STATUS_MATCH_LIST) {
            if (statusMatch.getMatch().matcher(message).find()) {
                return statusMatch.getAppStatus();
            }
        }
        return STATUS_UNKNOWN_ERROR;
    }
}
