package iris.client_bff.core;

import iris.client_bff.config.MapStructCentralConfig;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructCentralConfig.class)
public abstract class AggregateMapper implements Converter<Id, String> {

	@Override
	public String convert(Id value) {
		return value.toString();
	}
}
