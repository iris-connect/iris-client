package iris.client_bff.events.message.dto;

import iris.client_bff.events.web.dto.Guest;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ImportSelectionViewPayloadDto {
	private Selectables selectables;
	private Duplicates duplicates;

	@Data
	@Builder
	public static class Selectables {
		List<Guest> guests;
	}

	@Data
	@Builder
	public static class Duplicates {
		List<String> guests;
	}
}
