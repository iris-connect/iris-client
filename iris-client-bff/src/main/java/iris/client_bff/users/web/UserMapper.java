package iris.client_bff.users.web;

import iris.client_bff.config.MapStructCentralConfig;
import iris.client_bff.core.mappers.MetadataMapper.UserNameMetadata;
import iris.client_bff.users.UserAccount;
import iris.client_bff.users.UserRole;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(config = MapStructCentralConfig.class)
abstract class UserMapper {

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Mapping(target = "createdBy", qualifiedBy = UserNameMetadata.class)
	@Mapping(target = "lastModifiedBy", qualifiedBy = UserNameMetadata.class)
	public abstract UserDtos.Output toDto(UserAccount source);

	@Mapping(target = "password", expression = "java(passwordEncoder.encode(source.password()))")
	public abstract UserAccount fromDto(UserDtos.Insert source);

	public abstract UserRole fromDto(UserDtos.Role role);
}
