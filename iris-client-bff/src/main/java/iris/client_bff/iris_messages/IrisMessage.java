package iris.client_bff.iris_messages;

import iris.client_bff.core.Aggregate;
import iris.client_bff.core.Id;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "iris_message")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class IrisMessage extends Aggregate<IrisMessage, IrisMessage.IrisMessageIdentifier> {

    {
        id = IrisMessage.IrisMessageIdentifier.of(UUID.randomUUID());
    }

    @ManyToOne
    @JoinColumn(name="folder_id", insertable = false, updatable = false)
    private IrisMessageFolder folder;

    private String subject;

    private String body;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "id", column = @Column(name = "author_hd_id")),
            @AttributeOverride( name = "name", column = @Column(name = "author_hd_name"))
    })
    private IrisMessageContact authorHd;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "id", column = @Column(name = "recipient_hd_id")),
            @AttributeOverride( name = "name", column = @Column(name = "recipient_hd_name"))
    })
    private IrisMessageContact recipientHd;

    private Boolean isRead;

    private Boolean hasAttachments;

    public Instant getCreatedAt() {
        return this.getMetadata().getCreated();
    }

    @Embeddable
    @EqualsAndHashCode
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    public static class IrisMessageIdentifier implements Id, Serializable {

        private static final long serialVersionUID = -8254677010830428881L;

        final UUID id;

        /**
         * for JSON deserialization
         */
        public static IrisMessage.IrisMessageIdentifier of(String uuid) {
            return of(UUID.fromString(uuid));
        }

        @Override
        public String toString() {
            return id.toString();
        }

        public UUID toUUID() {
            return id;
        }
    }

}
