package iris.client_bff.iris_messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.Embeddable;

@Data
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IrisMessageHdContact {

    public static final int ID_MAX_LENGTH = 255;
    public static final int NAME_MAX_LENGTH = 255;

    private String id;
    @KeywordField(sortable = Sortable.YES, normalizer = "german")
    private String name;
}
