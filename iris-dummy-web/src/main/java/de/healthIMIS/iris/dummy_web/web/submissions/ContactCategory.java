package de.healthIMIS.iris.dummy_web.web.submissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Category of contact that describes the intensity and thus the risk of infection of the contact.
 */
public enum ContactCategory {
	HIGH_RISK("HIGH_RISK"), HIGH_RISK_MED("HIGH_RISK_MED"), MEDIUM_RISK_MED("MEDIUM_RISK_MED"), LOW_RISK(
			"LOW_RISK"), NO_RISK("NO_RISK");

	private String value;

	ContactCategory(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static ContactCategory fromValue(String text) {
		for (ContactCategory b : ContactCategory.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
