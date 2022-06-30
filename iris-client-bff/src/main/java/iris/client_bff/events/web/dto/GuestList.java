package iris.client_bff.events.web.dto;

import static lombok.AccessLevel.*;

import iris.client_bff.core.serialization.DefuseJsonString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class GuestList {

	@Builder.Default
	private List<@Valid Guest> guests = new ArrayList<>();

	@NotNull
	@Valid
	private GuestListDataProvider dataProvider;

	@DefuseJsonString(maxLength = 500)
	private String additionalInformation;

	private Instant startDate;
	private Instant endDate;
}
