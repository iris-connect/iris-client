package iris.client_bff.config;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.TemplateLookupContext;
import freemarker.cache.TemplateLookupResult;
import freemarker.cache.TemplateLookupStrategy;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
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
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * @author Jens Kutzsche
 */
@org.springframework.context.annotation.Configuration
@Slf4j
@RequiredArgsConstructor
class EmailConfig {

	@Value("${spring.mail.templates.path:#{null}}")
	private File mailTemplatesPath;

	private final I18nProperties i18nProperties;

	@Bean
	FreeMarkerConfig mailTemplatesConfigurer() throws IOException {

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

		var settings = new Properties();
		settings.put("incompatible_improvements", Configuration.VERSION_2_3_30.toString());
		freeMarkerConfigurer.setFreemarkerSettings(settings);

		return freeMarkerConfigurer;
	}

	/**
	 * Checks if all templates can be loaded in all supported languages.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@EventListener
	void on(ApplicationStartedEvent event) throws IOException {

		var locales = i18nProperties.getSupportedLocales();

		var configuration = mailTemplatesConfigurer().getConfiguration();

		// disable locale lookup to be able to check the existence of all templates.
		configuration.setTemplateLookupStrategy(new TemplateLookupStrategy() {

			@Override
			public TemplateLookupResult lookup(TemplateLookupContext ctx) throws IOException {
				return ctx.lookupWithAcquisitionStrategy(ctx.getTemplateName());
			}
		});

		Array.of(EmailTemplates.Keys.values()).crossProduct(List.ofAll(locales)).forEach(it -> {

			try {

				var key = it._1.getKey().replace(".ftl", "_" + it._2.toLanguageTag() + ".ftl");
				log.info("This is the needed file:" + key);
				var template = configuration.getTemplate(key, it._2);
				FreeMarkerTemplateUtils.processTemplateIntoString(template, null);

			} catch (IOException | TemplateException e) {
				throw new IllegalStateException(String.format("Missing email text for %s!", it));
			}
		});

		configuration.unsetTemplateLookupStrategy();

		log.info("Successfully verified email texts for locales {}.", locales);
	}

	/**
	 * @author Jens Kutzsche
	 */
	@Component
	@RequiredArgsConstructor
	final class FreeMarkerEmailTemplates implements EmailTemplates {

		private final Configuration config;

		@Override
		@SneakyThrows
		public String expandTemplate(Key key, @Nullable Locale locale, Map<String, ? extends Object> placeholders) {

			var template = config.getTemplate(key.getKey(), locale);

			return FreeMarkerTemplateUtils.processTemplateIntoString(template, placeholders);
		}

		@Override
		@SneakyThrows
		public EmailType getTemplateType(Key key, Locale locale) {

			var template = config.getTemplate(key.getKey(), locale);

			return template.getOutputFormat() instanceof HTMLOutputFormat ? EmailType.HTML : EmailType.TEXT;
		}
	}
}
