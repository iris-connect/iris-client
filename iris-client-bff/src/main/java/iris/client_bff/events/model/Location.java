package iris.client_bff.events.model;

import iris.client_bff.core.Id;
import iris.client_bff.events.EventDataRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

	@EmbeddedId
	private LocationIdentifier id;

	private String providerId;

	private String locationId;

	private String name;

	private String contactOfficialName;

	private String contactRepresentative;

	private String contactAddressStreet;

	private String contactAddressCity;

	private String contactAddressZip;

	private String contactOwnerEmail;

	private String contactEmail;

	private String contactPhone;

	@OneToOne(mappedBy = "location")
	private EventDataRequest request;

	@Embeddable
	@EqualsAndHashCode
	@Getter
	public static class LocationIdentifier implements Id, Serializable {

		private static final long serialVersionUID = -6053473869126771751L;

		private final UUID id;

		public LocationIdentifier() {
			id = UUID.randomUUID();
		}
	}
}
