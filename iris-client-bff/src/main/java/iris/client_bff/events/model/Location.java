package iris.client_bff.events.model;

import iris.client_bff.core.Id;
import iris.client_bff.events.EventDataRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

	@EmbeddedId
	private LocationIdentifier id;

	private String providerId;

	private String locationId;

	@FullTextField(name = "location_name", analyzer = "german")
	private String name;

	@FullTextField(analyzer = "german")
	private String contactOfficialName;

	private String contactRepresentative;

	@FullTextField(analyzer = "german")
	private String contactAddressStreet;

	@KeywordField(normalizer = "german")
	private String contactAddressCity;

	@GenericField
	private String contactAddressZip;

	private String contactOwnerEmail;

	private String contactEmail;

	private String contactPhone;

	// is required by Hibernate Search
	@OneToOne(mappedBy = "location")
	private EventDataRequest request;

	@Embeddable
	@EqualsAndHashCode
	@Getter
	@RequiredArgsConstructor(staticName = "of")
	public static class LocationIdentifier implements Id, Serializable {

		private static final long serialVersionUID = -6053473869126771751L;

		private final UUID id;

		public static LocationIdentifier of(String uuid) {
			return of(UUID.fromString(uuid));
		}

		public LocationIdentifier() {
			id = UUID.randomUUID();
		}
	}
}
