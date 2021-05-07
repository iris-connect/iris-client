package iris.client_bff.data_submission.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ContactsAndEvents {

	private ContactPersonList contacts;
	private EventList events;
	private ContactsAndEventsDataProvider dataProvider;
}
