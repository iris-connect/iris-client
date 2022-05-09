package iris.client_bff.config;

import iris.client_bff.cases.eps.CaseDataController;
import iris.client_bff.events.eps.EventDataController;
import iris.client_bff.iris_messages.eps.IrisMessageDataController;
import iris.client_bff.vaccination_info.eps.VaccinationInfoController;
import lombok.AllArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.jsonrpc4j.AnnotationsErrorResolver;
import com.googlecode.jsonrpc4j.DefaultErrorResolver;
import com.googlecode.jsonrpc4j.ErrorData;
import com.googlecode.jsonrpc4j.MultipleErrorResolver;
import com.googlecode.jsonrpc4j.spring.CompositeJsonServiceExporter;

@Configuration
@AllArgsConstructor
public class DataSubmissionConfig {

	public static final String DATA_SUBMISSION_ENDPOINT = "/data-submission-rpc";

	public static final String DATA_SUBMISSION_ENDPOINT_WITH_SLASH = "/data-submission-rpc/";

	private final MessageSourceAccessor messages;

	CaseDataController caseDataController;
	EventDataController eventDataController;
	IrisMessageDataController irisMessageDataController;
	VaccinationInfoController vaccinationProofController;

	@Bean(name = DATA_SUBMISSION_ENDPOINT)
	public CompositeJsonServiceExporter jsonRpcServiceImplExporter() {
		return createCompositeJsonServiceExporter();
	}

	@Bean(name = DATA_SUBMISSION_ENDPOINT_WITH_SLASH)
	public CompositeJsonServiceExporter jsonRpcServiceImplExporterWithSlash() {
		return createCompositeJsonServiceExporter();
	}

	private CompositeJsonServiceExporter createCompositeJsonServiceExporter() {

		CompositeJsonServiceExporter compositeJsonServiceExporter = new CompositeJsonServiceExporter();
		compositeJsonServiceExporter.setServiceInterfaces(new Class[] { CaseDataController.class, EventDataController.class,
				IrisMessageDataController.class, VaccinationInfoController.class });
		compositeJsonServiceExporter.setServices(new Object[] { caseDataController, eventDataController,
				irisMessageDataController, vaccinationProofController });
		// Used to allow the EPS to add common parameters (e.g. a signature) and not have to change all methods.
		compositeJsonServiceExporter.setAllowExtraParams(true);
		// Used to allow extension of an RPC method to include new parameters with default values while remaining compatible
		// with old RPC clients.
		compositeJsonServiceExporter.setAllowLessParams(true);

		compositeJsonServiceExporter.setErrorResolver(new MessageResolvingErrorResolver());

		return compositeJsonServiceExporter;
	}

	/**
	 * @author Jens Kutzsche
	 */
	private final class MessageResolvingErrorResolver extends MultipleErrorResolver {

		private MessageResolvingErrorResolver() {
			super(AnnotationsErrorResolver.INSTANCE, DefaultErrorResolver.INSTANCE);
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
}
