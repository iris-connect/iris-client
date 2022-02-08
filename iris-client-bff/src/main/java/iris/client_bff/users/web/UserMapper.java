package iris.client_bff.users.web;

import iris.client_bff.config.MapStructCentralConfig;
import iris.client_bff.core.MetadataMapper.UserNameMetadata;
import iris.client_bff.users.entities.UserAccount;
import iris.client_bff.users.web.dto.UserDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructCentralConfig.class)
interface UserMapper {

	@Mapping(target = "createdBy", qualifiedBy = UserNameMetadata.class)
	@Mapping(target = "lastModifiedBy", qualifiedBy = UserNameMetadata.class)
	UserDTO mapEntity2Dto(UserAccount source);
}
