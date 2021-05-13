package iris.client_bff.cases.web.submission_dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class EventList {

  @Builder.Default
  private List<Event> events = new ArrayList<>();
  private LocalDate startDate;
  private LocalDate endDate;
}
