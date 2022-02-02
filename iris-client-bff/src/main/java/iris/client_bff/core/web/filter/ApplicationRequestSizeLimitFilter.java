package iris.client_bff.core.web.filter;

import iris.client_bff.config.DataSubmissionConfig;
import iris.client_bff.core.alert.AlertService;
import iris.client_bff.core.log.LogHelper;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.LongConsumer;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author Jens Kutzsche
 */
@Component
@RequiredArgsConstructor
public class ApplicationRequestSizeLimitFilter extends OncePerRequestFilter {

	private final AlertService alertService;
	private final SuspiciouslyRequestProperties suspiciouslyRequestProps;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		var clientIp = getClientIP(request);
		var requestUri = request.getRequestURI();
		var warningSize = suspiciouslyRequestProps.getContentLengthWarningSizeFor(requestUri);
		var blockingSize = suspiciouslyRequestProps.getContentLengthBlockingSizeFor(requestUri);

		var wrappedRequest = new LimitedHttpServletRequest(request, response,
				warningSize, __ -> handleExceedWarnLimit(clientIp, requestUri, warningSize),
				blockingSize, __ -> handleExceedBlockLimit(clientIp, requestUri, blockingSize));

		filterChain.doFilter(wrappedRequest, response);
	}

	void handleExceedWarnLimit(String clientIp, String requestUri, DataSize warningSize) {

		var title = "Request content length exceeded warning limit!";
		alertService.createAlertMessage(title, String.format(
				"The content length of a request from IP '%s' to URI '%s' exceeded the warning limit of %s, this could indicate an attack!",
				LogHelper.obfuscateLastThree(clientIp), requestUri, warningSize));
	}

	@SneakyThrows(BlockLimitExceededException.class) // handled by framework
	void handleExceedBlockLimit(String clientIp, String requestUri, DataSize blockingSize) {

		var title = "Request content length exceeded blocking limit!";
		alertService.createAlertTicketAndMessage(title,
				String.format(
						"The content length of a request from IP '%s' to URI '%s' exceeded the blocking limit of %s, that is a potential attack!",
						LogHelper.obfuscateLastThree(clientIp), requestUri, blockingSize));

		throw new BlockLimitExceededException(title);
	}

	private String getClientIP(HttpServletRequest request) {
		String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}

	/**
	 * @author Jens Kutzsche
	 */
	final class LimitedHttpServletRequest extends HttpServletRequestWrapper {

		private final HttpServletResponse response;
		private final DataSize warningSize;
		private final DataSize blockingSize;
		private final LongConsumer exceedWarnLimitHeandler;
		private final LongConsumer exceedBlockLimitHeandler;

		private LimitedHttpServletRequest(HttpServletRequest request, HttpServletResponse response, DataSize warningSize,
				LongConsumer exceedWarnLimitHeandler, DataSize blockingSize, LongConsumer exceedBlockLimitHeandler) {
			super(request);

			this.response = response;
			this.warningSize = warningSize;
			this.exceedWarnLimitHeandler = exceedWarnLimitHeandler;
			this.blockingSize = blockingSize;
			this.exceedBlockLimitHeandler = exceedBlockLimitHeandler;
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return new BufferedReader(new InputStreamReader(getInputStream()));
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {

			var inputStream = new LimitedInputStream(super.getInputStream(), warningSize, exceedWarnLimitHeandler,
					blockingSize, exceedBlockLimitHeandler);

			return new ServletInputStream() {
				private boolean finished = false;

				@Override
				public boolean isFinished() {
					return finished;
				}

				@Override
				public int available() throws IOException {
					return inputStream.available();
				}

				@Override
				public void close() throws IOException {
					super.close();
					inputStream.close();
				}

				@Override
				public boolean isReady() {
					return true;
				}

				@Override
				public void setReadListener(ReadListener readListener) {
					throw new UnsupportedOperationException();
				}

				@Override
				public int read() throws IOException {

					try {

						int data = inputStream.read();
						if (data == -1) {
							finished = true;
						}
						return data;

					} catch (BlockLimitExceededException e) {

						handleBlockLimitException(e);
						throw e;
					}
				}

				@Override
				public int read(byte[] b, int off, int len) throws IOException {

					try {

						int data = inputStream.read(b, off, len);
						if (data == -1) {
							finished = true;
						}
						return data;

					} catch (BlockLimitExceededException e) {

						handleBlockLimitException(e);
						throw e;
					}
				}

				private void handleBlockLimitException(BlockLimitExceededException e) throws IOException {

					if (getRequestURI().startsWith(DataSubmissionConfig.DATA_SUBMISSION_ENDPOINT)) {
						response.getWriter().append("{\"jsonrpc\": \"2.0\", \"error\": {\"code\": -00001, \"message\": \""
								+ ErrorMessages.REQUEST_TOO_LARGE + "\"}}");
					}
				}
			};
		}
	}

	final class LimitedInputStream extends FilterInputStream {

		private final DataSize warningSize;
		private final DataSize blockingSize;
		private LongConsumer exceedWarnLimitHeandler;
		private LongConsumer exceedBlockLimitHeandler;

		private long count;

		public LimitedInputStream(InputStream inputStream, DataSize warningSize, LongConsumer exceedWarnLimitHeandler,
				DataSize blockingSize, LongConsumer exceedBlockLimitHeandler) {
			super(inputStream);

			this.warningSize = warningSize;
			this.exceedWarnLimitHeandler = exceedWarnLimitHeandler;
			this.blockingSize = blockingSize;
			this.exceedBlockLimitHeandler = exceedBlockLimitHeandler;
		}

		private void checkLimit() {

			if (count > blockingSize.toBytes()) {
				exceedBlockLimitHeandler.accept(count);
			}
		}

		private void checkWarningLimit() {

			if (count > warningSize.toBytes()) {
				exceedWarnLimitHeandler.accept(count);
			}
		}

		@Override
		public int read() throws IOException {
			final int res = super.read();
			if (res != -1) {
				count++;
				checkLimit();
			} else {
				checkWarningLimit();
			}
			return res;
		}

		@Override
		public int read(final byte[] b, final int off, final int len) throws IOException {
			final int res = super.read(b, off, len);
			if (res > 0) {
				count += res;
				checkLimit();
			} else {
				checkWarningLimit();
			}
			return res;
		}
	}

	public class BlockLimitExceededException extends RuntimeException {

		private static final long serialVersionUID = 3987483052714783027L;

		public BlockLimitExceededException(String message) {
			super(message);
		}
	}
}
