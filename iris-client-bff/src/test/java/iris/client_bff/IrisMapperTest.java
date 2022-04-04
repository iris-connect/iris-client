package iris.client_bff;

import iris.client_bff.IrisMapperTest.MapperTestContextBootstrapper;
import iris.client_bff.IrisMapperTest.MapperTypeExcludeFilter;
import iris.client_bff.core.ConversionServiceAdapter;
import iris.client_bff.core.MetadataMapper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.filter.StandardAnnotationCustomizableTypeExcludeFilter;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.env.Environment;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebMergedContextConfiguration;

/**
 * Annotation that can be used for a mapper test of MapStruct mappers that focuses <strong>only</strong> on mapper and
 * Spring conversion service with converters components.
 * <p>
 * Using this annotation will disable full auto-configuration and instead apply only configuration relevant to mapper
 * tests (i.e. {@code Converter}/{@code GenericConverter}, {@code GenericConversionService},
 * {@code ConversionServiceAdapter} and {@code MetadataMapper} beans but not {@code @Component}, {@code @Service} or
 * {@code @Repository} beans).
 * <p>
 * <strong> In order to test a mapper and have Spring create it and inject it into the test, the class of the mapper
 * must be specified in the annotation. </strong>
 * <p>
 * <strong> In your test you must include the following code to provide a necessary dependency to the
 * UserDetailsService, which is required by the MetadataMapper.
 * <p>
 * {@code @MockBean UserDetailsServiceImpl userService;}</strong><br>
 * (if necessary also:
 * {@code when(userService.findByUuid(any())).thenReturn(Optional.of(new UserAccount("admin", "admin", "admin", "admin", UserRole.ADMIN)));
 * )}
 * 
 * @author Jens Kutzsche
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(MapperTestContextBootstrapper.class)
@ExtendWith(SpringExtension.class)
@OverrideAutoConfiguration(enabled = false)
@TypeExcludeFilters(MapperTypeExcludeFilter.class)
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@ImportAutoConfiguration
@TestConstructor(autowireMode = AutowireMode.ALL)
@Tag("mappings")
public @interface IrisMapperTest {

	/**
	 * Properties in form {@literal key=value} that should be added to the Spring {@link Environment} before the test
	 * runs.
	 * 
	 * @return the properties to add
	 * @since 2.1.0
	 */
	String[] properties() default {};

	/**
	 * Specifies the mappers to test. This is an alias of {@link #mappers()} which can be used for brevity if no other
	 * attributes are defined. See {@link #mappers()} for details.
	 * 
	 * @see #mappers()
	 * @return the mappers to test
	 */
	@AliasFor("mappers")
	Class<?>[] value() default {};

	/**
	 * Specifies the mappers to test. May be left blank if all MapStruct {@code @Mapper} beans should be added to the
	 * application context.
	 * 
	 * @see #value()
	 * @return the mappers to test
	 */
	@AliasFor("value")
	Class<?>[] mappers() default {};

	/**
	 * Determines if default filtering should be used with {@link SpringBootApplication @SpringBootApplication}.
	 * 
	 * @return if default filters should be used
	 */
	boolean useDefaultFilters() default true;

	static class MapperTestContextBootstrapper extends SpringBootTestContextBootstrapper {

		@Override
		protected MergedContextConfiguration processMergedContextConfiguration(MergedContextConfiguration mergedConfig) {
			MergedContextConfiguration processedMergedConfiguration = super.processMergedContextConfiguration(mergedConfig);
			return new WebMergedContextConfiguration(processedMergedConfiguration, determineResourceBasePath(mergedConfig));
		}

		@Override
		protected String[] getProperties(Class<?> testClass) {
			return MergedAnnotations.from(testClass, SearchStrategy.INHERITED_ANNOTATIONS).get(IrisMapperTest.class)
					.getValue("properties", String[].class).orElse(null);
		}
	}

	static final class MapperTypeExcludeFilter extends StandardAnnotationCustomizableTypeExcludeFilter<IrisMapperTest> {

		private static final Class<?>[] NO_MAPPERS = {};

		private static final Set<Class<?>> DEFAULT_INCLUDES = Set.of(
				Converter.class,
				GenericConverter.class,
				GenericConversionService.class,
				ConversionServiceAdapter.class,
				MetadataMapper.class);

		private final Class<?>[] mappers;

		MapperTypeExcludeFilter(Class<?> testClass) {
			super(testClass);
			this.mappers = getAnnotation().getValue("mappers", Class[].class).orElse(NO_MAPPERS);
		}

		@Override
		protected Set<Class<?>> getDefaultIncludes() {
			return DEFAULT_INCLUDES;
		}

		@Override
		protected Set<Class<?>> getComponentIncludes() {
			return Set.of(mappers);
		}
	}
}
