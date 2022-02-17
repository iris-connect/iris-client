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
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

@Entity
@Table(name = "iris_message_folder")
@SecondaryTable(name = "iris_message_folder_default")
@Data
@EqualsAndHashCode(callSuper = true, exclude = "messages")
@NoArgsConstructor
public class IrisMessageFolder extends Aggregate<IrisMessageFolder, IrisMessageFolder.IrisMessageFolderIdentifier> {

	{
		id = IrisMessageFolder.IrisMessageFolderIdentifier.of(UUID.randomUUID());
	}

	@ToString.Exclude
	@OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<IrisMessage> messages;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	private IrisMessageContext context;

	@Embedded
	@AttributeOverride(name = "id",
			column = @Column(name = "id", table = "iris_message_folder_default", insertable = false, updatable = false))
	private IrisMessageFolderIdentifier defaultFolder;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "parent_folder"))
	private IrisMessageFolderIdentifier parentFolder;

	public Boolean getIsDefault() {
		return this.getId().equals(this.defaultFolder);
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class IrisMessageFolderIdentifier implements Id, Serializable {

		@Serial
		private static final long serialVersionUID = -8255216015747810442L;

		final UUID id;

		/**
		 * for JSON deserialization
		 */
		public static IrisMessageFolder.IrisMessageFolderIdentifier of(String uuid) {
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
