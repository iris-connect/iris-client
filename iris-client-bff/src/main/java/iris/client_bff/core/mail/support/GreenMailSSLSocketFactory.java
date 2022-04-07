package iris.client_bff.core.mail.support;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * <a>https://www.oracle.com/technetwork/java/sslnotes-150073.txt</a>
 * <p>
 * Property to activate this <code>SSLSocketFactory</code>:
 * <code>spring.mail.properties.mail.smtp.socketFactory.class = iris.client_bff.core.mail.support.GreenMailSSLSocketFactory</code>
 * </p>
 * <p>
 * Property to prevent falling back to one standard if this <code>SSLSocketFactory</code> has an error:
 * <code>spring.mail.properties.mail.smtp.socketFactory.fallback = false</code>
 * </p>
 */
public class GreenMailSSLSocketFactory extends SSLSocketFactory {

	private SSLSocketFactory factory;

	public GreenMailSSLSocketFactory() {
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(
					null,
					new TrustManager[] {
							new GreenMailTrustManager() },
					null);
			factory = sslcontext.getSocketFactory();
		} catch (Exception ex) {
			// ignore
		}
	}

	public static SocketFactory getDefault() {
		return new GreenMailSSLSocketFactory();
	}

	@Override
	public Socket createSocket() throws IOException {
		return factory.createSocket();
	}

	@Override
	public Socket createSocket(Socket socket, String s, int i, boolean flag) throws IOException {
		return factory.createSocket(socket, s, i, flag);
	}

	@Override
	public Socket createSocket(InetAddress inaddr, int i, InetAddress inaddr1, int j) throws IOException {
		return factory.createSocket(inaddr, i, inaddr1, j);
	}

	@Override
	public Socket createSocket(InetAddress inaddr, int i) throws IOException {
		return factory.createSocket(inaddr, i);
	}

	@Override
	public Socket createSocket(String s, int i, InetAddress inaddr, int j) throws IOException {
		return factory.createSocket(s, i, inaddr, j);
	}

	@Override
	public Socket createSocket(String s, int i) throws IOException {
		return factory.createSocket(s, i);
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return factory.getDefaultCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return factory.getSupportedCipherSuites();
	}

	/**
	 * Accepts only the certificate integrated in GreenMail
	 */
	static class GreenMailTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] cert, String authType) throws CertificateException {
			// everything is trusted
		}

		@Override
		public void checkServerTrusted(X509Certificate[] cert, String authType) throws CertificateException {

			var trust = "CN=GreenMail selfsigned Test Certificate,OU=GreenMail,O=Icegreen Technologies,C=US"
					.equals(cert[0].getSubjectX500Principal()
							.getName());

			if (!trust) {
				throw new CertificateException("Certificate isn't from mail server!");
			}
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
	}
}
