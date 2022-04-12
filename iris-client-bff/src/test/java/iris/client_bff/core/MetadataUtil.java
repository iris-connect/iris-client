package iris.client_bff.core;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Jens Kutzsche
 */
public class MetadataUtil {

	public static void setMetadata(Aggregate<?, ?> aggregate, Instant created, Instant lastModified, UUID createdBy,
			UUID lastModifiedBy) {

		setMetadata(aggregate.getMetadata(), created, lastModified, createdBy, lastModifiedBy);
	}

	public static void setMetadata(Metadata metadata, Instant created, Instant lastModified, UUID createdBy,
			UUID lastModifiedBy) {

		metadata.created = created;
		metadata.lastModified = lastModified;
		metadata.createdBy = createdBy;
		metadata.lastModifiedBy = lastModifiedBy;
	}

	public static void setCreated(Metadata metadata, Instant created) {
		metadata.created = created;
	}

	public static void setLastModified(Metadata metadata, Instant lastModified) {
		metadata.lastModified = lastModified;
	}

	public static void setUuid(Metadata metadata, UUID createdBy) {
		metadata.createdBy = createdBy;
	}

	public static void setLastModifiedBy(Metadata metadata, UUID lastModifiedBy) {
		metadata.lastModifiedBy = lastModifiedBy;
	}
}
