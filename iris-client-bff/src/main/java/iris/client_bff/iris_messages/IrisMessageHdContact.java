package iris.client_bff.iris_messages;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

@Data
@Embeddable
@Builder
@NoArgsConstructor
public class IrisMessageHdContact {

	public static final int ID_MAX_LENGTH = 255;
	public static final int NAME_MAX_LENGTH = 255;

	private String id;
	@FullTextField(name = "name_search", analyzer = "german")
	@GenericField(sortable = Sortable.YES)
	private String name;

	@ToString.Exclude
	@Transient
	private boolean own;

	public IrisMessageHdContact(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public IrisMessageHdContact(String id, String name, boolean isOwn) {
		this(id, name);
		this.own = isOwn;
	}
}
