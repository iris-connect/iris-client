package iris.client_bff.events.model;

import iris.client_bff.core.model.IdWithUuid;
import iris.client_bff.events.EventDataRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

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

	@EqualsAndHashCode(callSuper = false)
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // for JPA
	public static class LocationIdentifier extends IdWithUuid {

		private static final long serialVersionUID = 7944836016992870248L;

		private final UUID id;

		public static LocationIdentifier random() {
			return of(UUID.randomUUID());
		}

		@Override
		protected UUID getBasicId() {
			return id;
		}
	}
}
