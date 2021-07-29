/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
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
