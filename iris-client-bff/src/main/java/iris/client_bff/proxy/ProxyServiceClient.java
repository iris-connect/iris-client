package iris.client_bff.proxy;

public interface ProxyServiceClient {
	String announce() throws IRISAnnouncementException;

	String announceExplicitToken(String connectionAuthorizationToken) throws IRISAnnouncementException;

	String abortAnnouncement(String domain) throws IRISAnnouncementException;
}
