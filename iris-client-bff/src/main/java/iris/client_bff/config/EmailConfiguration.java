package iris.client_bff.config;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.TemplateException;
import io.vavr.collection.Array;
import io.vavr.collection.List;
import iris.client_bff.core.mail.EmailTemplates;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * @author Jens Kutzsche
 */
@org.springframework.context.annotation.Configuration
@Slf4j
@RequiredArgsConstructor
class EmailConfiguration {

	@Value("${spring.mail.templates.path:#{null}}")
	private File mailTemplatesPath;

	private final I18nProperties i18nProperties;

	@Bean
	public FreeMarkerConfigurer mailTemplatesConfig() throws IOException {

		var templateLoaders = new ArrayList<TemplateLoader>();

		if (mailTemplatesPath != null) {
			try {
				templateLoaders.add(new FileTemplateLoader(mailTemplatesPath));
			} catch (IOException e) {

				log.error("The configured path for mail templates (spring.mail.templates.path) isn't readable!");

				throw e;
			}
		}

		templateLoaders.add(new ClassTemplateLoader(this.getClass(), "/mail-templates"));

		var freeMarkerConfigurer = new FreeMarkerConfigurer();
		freeMarkerConfigurer.setPreTemplateLoaders(templateLoaders.toArray(TemplateLoader[]::new));
		return freeMarkerConfigurer;
	}

	@Bean
	EmailTemplates emailTemplates(FreeMarkerConfigurer configurer) {
		return new EmailTemplates() {

			@Override
			@SneakyThrows
			public String expandTemplate(Key key, Map<String, Object> placeholders, Locale locale) {

				var template = configurer.getConfiguration().getTemplate(key.getKey(), locale);

				return FreeMarkerTemplateUtils.processTemplateIntoString(template, placeholders);
			}
		};
	}

	@EventListener
	void on(ApplicationStartedEvent event) throws IOException {

		var locales = i18nProperties.getSupportedLocales();

		var configuration = mailTemplatesConfig().getConfiguration();

		Array.of(EmailTemplates.Keys.values())
				.crossProduct(List.ofAll(locales))
				.forEach(it -> {

					try {

						var template = configuration.getTemplate(it._1.getKey(), it._2);
						FreeMarkerTemplateUtils.processTemplateIntoString(template, null);

					} catch (IOException | TemplateException e) {
						throw new IllegalStateException(String.format("Missing email text for %s!", it));
					}
				});

		log.info("Successfully verified email texts for locales {}.", locales);
	}
}
