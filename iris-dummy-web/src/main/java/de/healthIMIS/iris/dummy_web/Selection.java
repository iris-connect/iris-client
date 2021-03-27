package de.healthIMIS.iris.dummy_web;

// tag::allButValidation[]
import lombok.Data;

import java.util.UUID;

// end::allButValidation[]
import javax.validation.constraints.NotNull;

@Data
public class Selection {

	@NotNull
	private String option;

	private String hd;

	@NotNull
	private UUID code;
}
