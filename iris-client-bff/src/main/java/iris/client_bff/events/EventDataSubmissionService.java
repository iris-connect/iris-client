package iris.client_bff.events;

import iris.client_bff.events.EventDataRequest.Status;
import iris.client_bff.events.model.EventDataSubmission;
import iris.client_bff.events.model.Guest;
import iris.client_bff.events.model.GuestListDataProvider;
import iris.client_bff.events.web.dto.GuestList;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventDataSubmissionService {

  private final ModelMapper mapper;

  private final EventDataSubmissionRepository submissions;

  private final EventDataRequestService requestService;

  public void save(EventDataRequest dataRequest, GuestList guestList) {
    var guests = guestList.getGuests().stream()
        .map(it -> mapper.map(it, Guest.class))
        .collect(Collectors.toSet());

    var dataProvider = mapper.map(guestList.getDataProvider(), GuestListDataProvider.class);

    var submission = new EventDataSubmission(dataRequest, guests, dataProvider, guestList.getAdditionalInformation(),
        guestList.getStartDate(), guestList.getEndDate());

    guests.forEach(it -> it.setSubmission(submission));

    submissions.save(submission);

    dataRequest.setStatus(Status.DATA_RECEIVED);

    requestService.save(dataRequest);
  }
}