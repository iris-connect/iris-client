package iris.client_bff.config;

import iris.client_bff.core.ConversionServiceAdapter;
import iris.client_bff.core.MetadataMapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.extensions.spring.SpringMapperConfig;

@MapperConfig(
		uses = { ConversionServiceAdapter.class, MetadataMapper.class },
		componentModel = ComponentModel.SPRING,
		injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@SpringMapperConfig(conversionServiceAdapterPackage = "iris.client_bff.core")
public interface MapStructCentralConfig {}
