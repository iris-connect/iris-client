package iris.client_bff.users.web;

import iris.client_bff.config.MapStructCentralConfig;
import iris.client_bff.core.MetadataMapper.UserNameMetadata;
import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.web.dto.UserDTO;
import iris.client_bff.users.web.dto.UserInsertDTO;

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
	public abstract UserDTO toDto(UserAccount source);

	@Mapping(target = "password", expression = "java(passwordEncoder.encode(source.password()))")
	public abstract UserAccount fromDto(UserInsertDTO source);
}
