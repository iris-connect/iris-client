package de.healthIMIS.iris.hd_server.core;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;

@Embeddable
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class DepartmentIdentifier implements Id, Serializable {

	private static final long serialVersionUID = -5946799883757153213L;
	final UUID departmentId;

	public static DepartmentIdentifier of(String uuid) {
		return of(UUID.fromString(uuid));
	}

	@Override
	public String toString() {
		return departmentId.toString();
	}
}
