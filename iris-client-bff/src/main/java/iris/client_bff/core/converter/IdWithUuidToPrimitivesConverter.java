package iris.client_bff.core.converter;

import iris.client_bff.core.IdWithUuid;

import java.util.Set;
import java.util.UUID;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * A Spring {@link Converter} to convert from IRIS {@link IdWithUuid} types to both {@link UUID} and {@link String}. We
 * use the methods {@link IdWithUuid#toUuid()} and {@link IdWithUuid#toString()} of {@link IdWithUuid} to convert the
 * value.
 *
 * @author Jens Kutzsche
 */
@Component
public class IdWithUuidToPrimitivesConverter implements GenericConverter {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.GenericConverter#getConvertibleTypes()
	 */
	@NonNull
	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {

		return Set.of(//
				new ConvertiblePair(IdWithUuid.class, String.class),
				new ConvertiblePair(IdWithUuid.class, UUID.class));
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.GenericConverter#convert(java.lang.Object, org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
	 */
	@Nullable
	@Override
	public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

		if (source instanceof IdWithUuid id) {
			return targetType.getType().equals(UUID.class) ? id.toUuid() : id.toString();
		}

		return source;
	}
}
