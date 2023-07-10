package org.pierre.shareazade.service.mapper;

import org.mapstruct.*;
import org.pierre.shareazade.domain.ShareCity;
import org.pierre.shareazade.domain.ShareRide;
import org.pierre.shareazade.domain.User;
import org.pierre.shareazade.service.dto.ShareCityDTO;
import org.pierre.shareazade.service.dto.ShareRideDTO;
import org.pierre.shareazade.service.dto.UserDTO;

/**
 * Mapper for the entity {@link ShareRide} and its DTO {@link ShareRideDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShareRideMapper extends EntityMapper<ShareRideDTO, ShareRide> {
    @Mapping(target = "rideCityFrom", source = "rideCityFrom", qualifiedByName = "shareCityCityName")
    @Mapping(target = "rideCityTo", source = "rideCityTo", qualifiedByName = "shareCityCityName")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    ShareRideDTO toDto(ShareRide s);

    @Named("shareCityCityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cityName", source = "cityName")
    ShareCityDTO toDtoShareCityCityName(ShareCity shareCity);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
