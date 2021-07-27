package iris.client_bff.core.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.validation.annotation.Validated;

/**
 * @author Jens Kutzsche
 */
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AlertDto {

	String title;
	String text;
	String version;
	AlertType alertType;

	public enum AlertType {
		TICKET, MESSAGE
	}
}
