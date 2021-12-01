package iris.client_bff.iris_messages;

import lombok.Data;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class IrisMessageContact {
    private String id;
    @KeywordField(sortable = Sortable.YES, normalizer = "german")
    private String name;
}
