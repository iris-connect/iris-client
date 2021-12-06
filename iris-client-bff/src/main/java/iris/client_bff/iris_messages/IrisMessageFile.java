package iris.client_bff.iris_messages;

import iris.client_bff.core.Aggregate;
import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "iris_message_file")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class IrisMessageFile extends Aggregate<IrisMessageFile, IrisMessageFile.IrisMessageFileIdentifier> {

    {
        id = IrisMessageFileIdentifier.of(UUID.randomUUID());
    }

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false, columnDefinition = "BLOB")
    private byte[] content;

    @Column(nullable = false)
    private String contentType;

    @ManyToOne
    @JoinColumn(name="message_id")
    private IrisMessage message;

    @Embeddable
    @EqualsAndHashCode
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
    public static class IrisMessageFileIdentifier implements iris.client_bff.core.Id, Serializable {

        @Serial
        private static final long serialVersionUID = -7602440129090196288L;

        private final UUID id;

        /**
         * for JSON deserialization
         */
        public static IrisMessageFile.IrisMessageFileIdentifier of(String uuid) {
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
