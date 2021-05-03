package de.healthIMIS.iris.client.data_submission;

import lombok.Data;

import java.util.UUID;

@Data
public class DataSubmissionDto {

	private final UUID id;

	private final UUID requestId;

	private final UUID departmentId;

	private final String secret;

	private final String keyReference;

	private final String encryptedData;

	private final Feature feature;

	public static enum Feature {
		Contacts_Events, Guests
	}
}
