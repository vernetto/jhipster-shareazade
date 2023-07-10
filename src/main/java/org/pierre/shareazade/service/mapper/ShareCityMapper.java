package org.pierre.shareazade.service.mapper;

import org.mapstruct.*;
import org.pierre.shareazade.domain.ShareCity;
import org.pierre.shareazade.domain.User;
import org.pierre.shareazade.service.dto.ShareCityDTO;
import org.pierre.shareazade.service.dto.UserDTO;

/**
 * Mapper for the entity {@link ShareCity} and its DTO {@link ShareCityDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShareCityMapper extends EntityMapper<ShareCityDTO, ShareCity> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    ShareCityDTO toDto(ShareCity s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
