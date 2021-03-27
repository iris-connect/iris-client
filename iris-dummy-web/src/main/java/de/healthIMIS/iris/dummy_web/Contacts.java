package de.healthIMIS.iris.dummy_web;

// tag::allButValidation[]
import lombok.Data;

import java.util.UUID;

// end::allButValidation[]
import javax.validation.constraints.NotNull;

@Data
public class Contacts {

	@NotNull
	private String firstName = null;

	@NotNull
	private String lastName = null;

	@NotNull
	private UUID code;
}
