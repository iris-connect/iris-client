package iris.client_bff.events.web.dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PRIVATE)
public class LocationInformation {

	private String name;
	private String publicKey;
	private LocationContact contact;
	private List<LocationContext> contexts;
}
