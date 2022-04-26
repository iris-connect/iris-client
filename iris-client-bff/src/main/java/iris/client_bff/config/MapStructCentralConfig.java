package iris.client_bff.config;

import iris.client_bff.core.ConversionServiceAdapter;
import iris.client_bff.core.IdWithUuid;
import iris.client_bff.core.MetadataMapper;

import java.util.UUID;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.extensions.spring.ExternalConversion;
import org.mapstruct.extensions.spring.SpringMapperConfig;

@MapperConfig(
		uses = { ConversionServiceAdapter.class, MetadataMapper.class },
		componentModel = ComponentModel.SPRING,
		injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@SpringMapperConfig(conversionServiceAdapterPackage = "iris.client_bff.core",
		externalConversions = { // Conversions from Spring’s Conversion Service
				@ExternalConversion(sourceType = String.class, targetType = IdWithUuid.class),
				@ExternalConversion(sourceType = UUID.class, targetType = IdWithUuid.class),
				@ExternalConversion(sourceType = IdWithUuid.class, targetType = String.class),
				@ExternalConversion(sourceType = IdWithUuid.class, targetType = UUID.class)
		})
public interface MapStructCentralConfig {}
