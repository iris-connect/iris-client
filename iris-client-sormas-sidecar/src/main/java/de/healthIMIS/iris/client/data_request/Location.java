package de.healthIMIS.iris.client.data_request;

import de.healthIMIS.iris.client.core.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
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

	@Embeddable
	@EqualsAndHashCode
	@Getter
	public static class LocationIdentifier implements Id, Serializable {

		private static final long serialVersionUID = -6053473869126771751L;

		private final UUID id;

		public LocationIdentifier(){
			id=UUID.randomUUID();
		}



	}
}
