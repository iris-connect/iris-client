package iris.client_bff.core.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

@Embeddable
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Address {

	@FullTextField(analyzer = "german")
	private String street;
	private String houseNumber;

	@GenericField
	private String zipCode;

	@KeywordField(normalizer = "german")
	private String city;
}
