package iris.client_bff.config;

import iris.client_bff.cases.eps.CaseDataController;
import iris.client_bff.events.eps.EventDataController;
import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.googlecode.jsonrpc4j.spring.CompositeJsonServiceExporter;

@Configuration
@AllArgsConstructor
public class DataSubmissionConfig {

	public static final String DATA_SUBMISSION_ENDPOINT = "/data-submission-rpc/";

	CaseDataController caseDataController;

	EventDataController eventDataController;

	@Bean(name = DATA_SUBMISSION_ENDPOINT)
	public CompositeJsonServiceExporter jsonRpcServiceImplExporter() {

		CompositeJsonServiceExporter compositeJsonServiceExporter = new CompositeJsonServiceExporter();
		compositeJsonServiceExporter.setServices(
				new Object[] { caseDataController, eventDataController });
		compositeJsonServiceExporter.setServiceInterfaces(
				new Class<?>[] { CaseDataController.class, EventDataController.class });
		compositeJsonServiceExporter.setAllowMultipleInheritance(true);

		return compositeJsonServiceExporter;
	}

}
