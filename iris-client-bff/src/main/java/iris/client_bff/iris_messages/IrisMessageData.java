package iris.client_bff.iris_messages;

import iris.client_bff.core.Aggregate;
import iris.client_bff.core.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "iris_message_data")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class IrisMessageData extends Aggregate<IrisMessageData, IrisMessageData.IrisMessageDataIdentifier> {

	public static final int DISCRIMINATOR_MAX_LENGTH = 255;
	public static final int PAYLOAD_MAX_LENGTH = 999999;
	public static final int DESCRIPTION_MAX_LENGTH = 255;

	{
		id = IrisMessageDataIdentifier.of(UUID.randomUUID());
	}

	@Column(nullable = false)
	private String discriminator;

	// H2 and Postgres are using the same SQL migration files, because their Dialects are very similar.
	// There are differences, on how the "text" type in combination with the @Lob Annotation is handled.
	// To avoid creating dedicated Postgres migration files, we use a byte-array as the common data type.
	@Column(nullable = false, length = 16777215)
	private byte[] payload;

	public IrisMessageData setPayload(String payload) {
		this.payload = payload.getBytes(StandardCharsets.UTF_8);
		return this;
	}

	public String getPayload() {
		return new String(payload, StandardCharsets.UTF_8);
	}

	@Column(nullable = false)
	private String description;

	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "message_id")
	private IrisMessage message;

	private boolean isImported;

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
