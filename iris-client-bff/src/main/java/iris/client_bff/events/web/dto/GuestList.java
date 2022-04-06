package iris.client_bff.events.web.dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
@ToString
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class GuestList {

	@Builder.Default
	private List<Guest> guests = new ArrayList<>();
	private @NotNull @Valid GuestListDataProvider dataProvider;
	private String additionalInformation;
	private Instant startDate;
	private Instant endDate;
}
