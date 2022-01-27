package iris.client_bff.iris_messages.data;

import iris.client_bff.core.Aggregate;
import iris.client_bff.core.Id;
import iris.client_bff.iris_messages.IrisMessage;
import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "iris_message_data")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class IrisMessageData extends Aggregate<IrisMessageData, IrisMessageData.IrisMessageDataIdentifier> {

    public static final int DISCRIMINATOR_MAX_LENGTH = 255;
    public static final int PAYLOAD_MAX_LENGTH = 999999;
    public static final int DESCRIPTION_MAX_LENGTH = 500;

    {
        id = IrisMessageDataIdentifier.of(UUID.randomUUID());
    }

    @Column(nullable = false)
    private String discriminator;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private String description;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="message_id")
    private IrisMessage message;

    private Boolean isImported;

    @Embeddable
    @EqualsAndHashCode
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    public static class IrisMessageDataIdentifier implements Id, Serializable {

        @Serial
        private static final long serialVersionUID = 1140444389070674189L;

        private final UUID id;

        /**
         * for JSON deserialization
         */
        public static IrisMessageDataIdentifier of(String uuid) {
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
