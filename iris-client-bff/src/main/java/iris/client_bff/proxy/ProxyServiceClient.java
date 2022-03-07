package iris.client_bff.proxy;

import java.time.Instant;

public interface ProxyServiceClient {
	String announce() throws IRISAnnouncementException;

	String announceExplicitToken(String connectionAuthorizationToken) throws IRISAnnouncementException;

	String abortAnnouncement(String domain) throws IRISAnnouncementException;

	String announce(Instant expirationTime) throws IRISAnnouncementException;
}
