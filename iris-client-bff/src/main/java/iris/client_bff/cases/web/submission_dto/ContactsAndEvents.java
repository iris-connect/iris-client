package iris.client_bff.cases.web.submission_dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class ContactsAndEvents {

	private ContactPersonList contacts;
	private EventList events;
	private ContactsAndEventsDataProvider dataProvider;
}
