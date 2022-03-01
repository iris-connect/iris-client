package iris.client_bff.iris_messages.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class RawJsonDeserializer extends JsonDeserializer<String> {
	@Override
	public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
		JsonNode node = mapper.readTree(jsonParser);
		return mapper.writeValueAsString(node);
	}
}
