package iris.client_bff.core.health;

import java.io.IOException;
import java.util.Optional;

import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Deserializer for the {@link Status} from backend service (formerly called location service).
 * 
 * @author Jens Kutzsche
 */
@JsonComponent
public class StatusDeserializer extends StdDeserializer<Status> {

	private static final long serialVersionUID = -314531032736552403L;

	public StatusDeserializer() {
		this(Status.class);
	}

	public StatusDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Status deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

		JsonNode node = jp.getCodec().readTree(jp);

		return new Status(
				Optional.ofNullable(node.get("status"))
						.map(JsonNode::asText)
						.orElse(""),
				Optional.ofNullable(node.get("description"))
						.map(JsonNode::asText)
						.orElse(""));
	}
}
