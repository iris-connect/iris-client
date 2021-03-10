package de.healthIMIS.iris.dummy_web;

import java.util.UUID;

// end::allButValidation[]
import javax.validation.constraints.NotNull;

// tag::allButValidation[]
import lombok.Data;

@Data
public class Selection {

	@NotNull
	private String option;

	private String hd;

	@NotNull
	private UUID code;
}
