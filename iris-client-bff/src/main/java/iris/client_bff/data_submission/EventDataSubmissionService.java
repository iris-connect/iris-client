package iris.client_bff.data_submission;

import iris.client_bff.data_request.DataRequest.Status;
import iris.client_bff.data_request.events.EventDataRequest;
import iris.client_bff.data_request.events.EventDataRequestService;
import iris.client_bff.data_request.events.web.dto.GuestList;
import iris.client_bff.data_submission.model.DataSubmission;
import iris.client_bff.data_submission.model.Guest;
import iris.client_bff.data_submission.model.GuestListDataProvider;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventDataSubmissionService {

  private final ModelMapper mapper;

  private final DataSubmissionRepository submissions;

  private final EventDataRequestService requestService;

  public void save(EventDataRequest dataRequest, GuestList guestList) {
    var guests = guestList.getGuests().stream()
        .map(it -> mapper.map(it, Guest.class))
        .collect(Collectors.toSet());

    var dataProvider = mapper.map(guestList.getDataProvider(), GuestListDataProvider.class);

    var submission = new DataSubmission(dataRequest, guests, dataProvider, guestList.getAdditionalInformation(),
        guestList.getStartDate(), guestList.getEndDate());

    guests.forEach(it -> it.setSubmission(submission));

    submissions.save(submission);

    dataRequest.setStatus(Status.DATA_RECEIVED);

    requestService.save(dataRequest);
  }
}
