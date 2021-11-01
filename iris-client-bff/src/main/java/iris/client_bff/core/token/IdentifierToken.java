package iris.client_bff.core.token;

import iris.client_bff.core.log.LogHelper;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IdentifierToken {

	private String readableToken;
	private String connectionAuthorizationToken;
	private String dataAuthorizationToken;

	public String toStringWithObfuscation() {
		return String.format("readable token: %s, dataAuthorizationToken: %s, connectionAuthorizationToken: %s",
				LogHelper.obfuscateAtStart16(readableToken),
				LogHelper.obfuscateAtStart16(dataAuthorizationToken),
				LogHelper.obfuscateAtStart16(connectionAuthorizationToken));
	}
}
