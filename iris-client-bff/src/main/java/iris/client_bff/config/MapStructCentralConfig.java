package iris.client_bff.config;

import iris.client_bff.core.mappers.ConversionServiceAdapter;
import iris.client_bff.core.mappers.MetadataMapper;
import iris.client_bff.core.model.IdWithUuid;

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
@SpringMapperConfig(conversionServiceAdapterPackage = "iris.client_bff.core.mappers",
		externalConversions = { // Conversions from Spring’s Conversion Service
				@ExternalConversion(sourceType = String.class, targetType = IdWithUuid.class),
				@ExternalConversion(sourceType = UUID.class, targetType = IdWithUuid.class),
				@ExternalConversion(sourceType = IdWithUuid.class, targetType = String.class),
				@ExternalConversion(sourceType = IdWithUuid.class, targetType = UUID.class)
		})
public interface MapStructCentralConfig {}
