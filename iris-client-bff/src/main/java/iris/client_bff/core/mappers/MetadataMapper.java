package iris.client_bff.core.mappers;

import iris.client_bff.config.MapStructCentralConfig;
import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapStructCentralConfig.class)
public abstract class MetadataMapper {

	@Autowired
	protected UserService userService;

	@UserNameMetadata
	public String map(UUID value) {
		return userService.findByUuid(value).map(MetadataMapper::getFullName).orElse(null);
	}

	private static String getFullName(UserAccount user) {
		return user.getFirstName() + " " + user.getLastName();
	}

	@Qualifier
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.CLASS)
	public @interface UserNameMetadata {}
}
