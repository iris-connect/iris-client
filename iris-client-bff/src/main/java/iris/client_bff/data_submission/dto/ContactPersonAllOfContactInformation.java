package iris.client_bff.data_submission.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Builder
@ToString
public class ContactPersonAllOfContactInformation {

	private LocalDate firstContactDate;
	private LocalDate lastContactDate;
	private ContactCategory contactCategory;
	private String basicConditions;
}
