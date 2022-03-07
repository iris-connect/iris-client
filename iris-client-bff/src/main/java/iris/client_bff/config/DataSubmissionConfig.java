package iris.client_bff.config;

import iris.client_bff.cases.eps.CaseDataController;
import iris.client_bff.events.eps.EventDataController;
import iris.client_bff.iris_messages.eps.IrisMessageDataController;
import iris.client_bff.vaccination_info.eps.VaccinationInfoController;
import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.googlecode.jsonrpc4j.spring.CompositeJsonServiceExporter;

@Configuration
@AllArgsConstructor
public class DataSubmissionConfig {

	public static final String DATA_SUBMISSION_ENDPOINT = "/data-submission-rpc";

	public static final String DATA_SUBMISSION_ENDPOINT_WITH_SLASH = "/data-submission-rpc/";

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
		compositeJsonServiceExporter.setServices(new Object[] { caseDataController, eventDataController,
				irisMessageDataController, vaccinationProofController });
		compositeJsonServiceExporter.setAllowExtraParams(true); // Used to allow the EPS to add common parameters (e.g. a
																														// signature) and not have to change all methods.

		return compositeJsonServiceExporter;
	}
}
