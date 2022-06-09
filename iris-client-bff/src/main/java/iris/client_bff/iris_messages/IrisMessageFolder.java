package iris.client_bff.iris_messages;

import iris.client_bff.core.model.Aggregate;
import iris.client_bff.core.model.IdWithUuid;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.util.Set;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "iris_message_folder")
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
	@AttributeOverride(name = "id", column = @Column(name = "parent_folder"))
	private IrisMessageFolderIdentifier parentFolder;

	@EqualsAndHashCode(callSuper = false)
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // for JPA
	public static class IrisMessageFolderIdentifier extends IdWithUuid {

		@Serial
		private static final long serialVersionUID = 8991605733958195526L;

		final UUID id;

		@Override
		protected UUID getBasicId() {
			return id;
		}
	}
}
