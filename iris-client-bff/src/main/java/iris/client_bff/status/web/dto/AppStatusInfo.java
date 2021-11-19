package iris.client_bff.status.web.dto;

import iris.client_bff.status.AppStatus;
import iris.client_bff.status.AppStatus.Status;
import iris.client_bff.status.eps.dto.Ping;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import org.springframework.context.MessageSourceResolvable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Value
@Builder
public class AppStatusInfo {

	private @With Ping info;

	@JsonIgnore
	private AppStatus appStatus;

	public static AppStatusInfo of(AppStatus appStatus) {
		return new AppStatusInfo(null, appStatus);
	}

	public Status getStatus() {
		return appStatus.getStatus();
	}

	public MessageSourceResolvable getMessage() {
		return appStatus;
	}
}
