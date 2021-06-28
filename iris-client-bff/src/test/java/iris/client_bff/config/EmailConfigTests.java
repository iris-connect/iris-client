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
package iris.client_bff.config;

import static org.assertj.core.api.Assertions.*;

import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import iris.client_bff.IrisWebIntegrationTest;
import lombok.RequiredArgsConstructor;

import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@RequiredArgsConstructor
class EmailConfigTests {

	private final Configuration freemarkerConfig;

	@Test
	void getTemplate() throws Exception {

		var templateModel = Map.of("recipientName", "Name", "text", "Der Text", "senderName", "Sender");

		var template = freemarkerConfig.getTemplate("test-mail.ftl", Locale.GERMAN);
		var body = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);

		assertThat(body).contains("Name", "Der Text", "Sender", "Gesundheitsamt", "Hallo");

		template = freemarkerConfig.getTemplate("test-mail.ftl", Locale.ENGLISH);
		body = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);

		assertThat(body).contains("Name", "Der Text", "Sender", "Health Department", "Hi");

		template = freemarkerConfig.getTemplate("test-html-mail.ftlh", Locale.GERMAN);
		body = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);

		assertThat(template.getOutputFormat()).isExactlyInstanceOf(HTMLOutputFormat.class);
		assertThat(body).contains("Name", "Der Text", "Sender", "Gesundheitsamt", "Hallo");
	}
}
