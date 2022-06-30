package iris.client_bff.config;

import static com.googlecode.jsonrpc4j.ErrorResolver.JsonError.*;

import iris.client_bff.cases.eps.CaseDataController;
import iris.client_bff.core.alert.AlertService;
import iris.client_bff.events.eps.EventDataController;
import iris.client_bff.iris_messages.eps.IrisMessageDataController;
import iris.client_bff.users.UserService;
import iris.client_bff.vaccination_info.eps.VaccinationInfoController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.jsonrpc4j.AnnotationsErrorResolver;
import com.googlecode.jsonrpc4j.DefaultErrorResolver;
import com.googlecode.jsonrpc4j.ErrorData;
import com.googlecode.jsonrpc4j.ErrorResolver;
import com.googlecode.jsonrpc4j.JsonRpcInterceptor;
import com.googlecode.jsonrpc4j.MultipleErrorResolver;
import com.googlecode.jsonrpc4j.ProxyUtil;
import com.googlecode.jsonrpc4j.spring.JsonServiceExporter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSubmissionConfig {

	public static final String DATA_SUBMISSION_ENDPOINT = "/data-submission-rpc";
	public static final String DATA_SUBMISSION_ENDPOINT_WITH_SLASH = "/data-submission-rpc/";

	private final Map<Class<? extends Throwable>, Integer> exception2CodeMap = Map.of(
			ConstraintViolationException.class, INVALID_REQUEST.code,
			IllegalArgumentException.class, INVALID_REQUEST.code);

	private final MessageSourceAccessor messages;
	private final UserService userService;
	private final AlertService alertService;

	private final CaseDataController caseDataController;
	private final EventDataController eventDataController;
	private final IrisMessageDataController irisMessageDataController;
	private final VaccinationInfoController vaccinationProofController;

	@Bean(name = DATA_SUBMISSION_ENDPOINT)
	public JsonServiceExporter jsonRpcServiceImplExporter() {
		return createCompositeJsonServiceExporter();
	}

	@Bean(name = DATA_SUBMISSION_ENDPOINT_WITH_SLASH)
	public JsonServiceExporter jsonRpcServiceImplExporterWithSlash() {
		return createCompositeJsonServiceExporter();
	}

	private JsonServiceExporter createCompositeJsonServiceExporter() {

		Object[] services = {
				caseDataController,
				eventDataController,
				irisMessageDataController,
				vaccinationProofController };
		Class<?>[] serviceInterfaces = {
				CaseDataController.class,
				EventDataController.class,
				IrisMessageDataController.class,
				VaccinationInfoController.class };

		// create the composite service
		var service = ProxyUtil.createCompositeServiceProxy(getClass().getClassLoader(), services, serviceInterfaces,
				false);

		var jsonServiceExporter = new JsonServiceExporter();
		jsonServiceExporter.setService(service);
		// Used to allow the EPS to add common parameters (e.g. a signature) and not have to change all methods.
		jsonServiceExporter.setAllowExtraParams(true);
		// Used to allow extension of an RPC method to include new parameters with default values while remaining compatible
		// with old RPC clients.
		jsonServiceExporter.setAllowLessParams(true);

		jsonServiceExporter.setErrorResolver(new MessageResolvingErrorResolver());
		jsonServiceExporter.setInterceptorList(List.of(new ClientAsUserInterceptor()));

		return jsonServiceExporter;
	}

	/**
	 * @author Jens Kutzsche
	 */
	private final class MessageResolvingErrorResolver extends MultipleErrorResolver {

		private MessageResolvingErrorResolver() {
			super(AnnotationsErrorResolver.INSTANCE, new IrisErrorResolver(), DefaultErrorResolver.INSTANCE);
		}

		@Override
		public JsonError resolveError(Throwable t, Method method, List<JsonNode> arguments) {

			var error = super.resolveError(t, method, arguments);

			try {
				var message = messages.getMessage(error.message);

				var data = error.data;
				if (data instanceof ErrorData ed) {
					data = new ErrorData(ed.getExceptionTypeName(), message);
				}

				return new JsonError(error.code, message, data);

			} catch (NoSuchMessageException e) {

				return error;
			}
		}
	}

	private class IrisErrorResolver implements ErrorResolver {

		@Override
		public JsonError resolveError(Throwable t, Method method, List<JsonNode> arguments) {

			return determineCode(t)
					.map(code -> new JsonError(code, t.getMessage(), new ErrorData(t.getClass().getName(), t.getMessage())))
					.orElse(null);
		}

		private Optional<Integer> determineCode(Throwable t) {

			return exception2CodeMap.entrySet().stream()
					.filter(it -> it.getKey().isAssignableFrom(t.getClass()))
					.map(Entry::getValue)
					.findAny();
		}
	}

	/**
	 * @author Jens Kutzsche
	 */
	private final class ClientAsUserInterceptor implements JsonRpcInterceptor {

		private static final String CLIENT_USER_PREFIX = "IRIS_CLIENT__";

		@Override
		public void preHandleJson(JsonNode json) {

			try {
				var clientName = json.findPath("params").findPath("_client").findPath("name");
				if (clientName.isTextual()) {

					var username = CLIENT_USER_PREFIX + clientName.asText();

					var user = userService.findOrCreateUser(username, it -> it
							.firstName("IRIS-Client")
							.lastName(clientName.asText()));

					SecurityContextHolder.getContext()
							.setAuthentication(new AnonymousAuthenticationToken(username, user,
									AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")));
				}

			} catch (Exception e) {

				// This is to ensure that proper error handling always takes place and no errors go unnoticed. Exceptions are
				// not handled sufficiently by jsonrcp4j here: "In case preHandleJson throws exception, it will generate HTTP
				// code 400 "Bad Request" with empty body."
				//
				// However, I do not expect exceptions here.

				var msg = "Can't determine user account for the client of a JSON-RPC request.";
				log.error(msg, e);
				alertService.createAlertMessage("Error: Determine user for JSON-RPC client",
						msg + " Exception message: " + e.getMessage());
			}
		}

		@Override
		public void preHandle(Object target, Method method, List<JsonNode> params) {
			// not used
		}

		@Override
		public void postHandle(Object target, Method method, List<JsonNode> params, JsonNode result) {
			// not used
		}

		@Override
		public void postHandleJson(JsonNode json) {
			// not used
		}
	}
}
