package iris.client_bff.iris_messages.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The RawJsonDeserializer is used to deserialize the message payload when creating / sending a new message. The payload
 * from the frontend is passed as JSON in the request body. The payload type in the Dto is String (raw JSON String).
 * This deserializer tells Jackson to read the message payload as JsonNode and return it as raw JSON String.
 */
public class RawJsonDeserializer extends JsonDeserializer<String> {
	@Override
	public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
		JsonNode node = mapper.readTree(jsonParser);
		return mapper.writeValueAsString(node);
	}
}
