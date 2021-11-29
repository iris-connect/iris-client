package iris.client_bff.core.token;

import iris.client_bff.core.log.LogHelper;
import lombok.Builder;

@Builder
public record IdentifierToken(String readableToken,
		String connectionAuthorizationToken,
		String dataAuthorizationToken) {

	public String toStringWithObfuscation() {
		return String.format("readable token: %s, dataAuthorizationToken: %s, connectionAuthorizationToken: %s",
				LogHelper.obfuscateAtStart16(readableToken),
				LogHelper.obfuscateAtStart16(dataAuthorizationToken),
				LogHelper.obfuscateAtStart16(connectionAuthorizationToken));
	}
}
