package iris.client_bff.iris_messages;

import lombok.*;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Data
@Embeddable
@Builder
@NoArgsConstructor
public class IrisMessageHdContact {

    public static final int ID_MAX_LENGTH = 255;
    public static final int NAME_MAX_LENGTH = 255;

    private String id;
    @KeywordField(sortable = Sortable.YES, normalizer = "german")
    private String name;

    @ToString.Exclude
    @Transient
    private boolean isOwn;

    public IrisMessageHdContact(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public IrisMessageHdContact(String id, String name, boolean isOwn) {
        this(id, name);
        this.isOwn = isOwn;
    }
}
