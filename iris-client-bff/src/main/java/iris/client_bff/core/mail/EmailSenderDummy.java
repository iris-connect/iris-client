package iris.client_bff.core.mail;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author Jens Kutzsche
 */
@Service
@ConditionalOnProperty(name = "iris.client.mailing.active", havingValue = "false", matchIfMissing = true)
@Slf4j
public class EmailSenderDummy implements EmailSender {

	@Override
	public Try<Object> testConnection() {
		return Try.success("Dummy Sender");
	}

	@Override
	public Try<Void> sendMail(TemplatedEmail email) {
		return Try.success(null);
	}

	@PostConstruct
	void postConstruct() {
		log.info("Email sender dummy is used.");
	}
}
