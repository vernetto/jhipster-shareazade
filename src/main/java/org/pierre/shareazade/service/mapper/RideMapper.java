package org.pierre.shareazade.service.mapper;

import org.mapstruct.*;
import org.pierre.shareazade.domain.City;
import org.pierre.shareazade.domain.Ride;
import org.pierre.shareazade.domain.Users;
import org.pierre.shareazade.service.dto.CityDTO;
import org.pierre.shareazade.service.dto.RideDTO;
import org.pierre.shareazade.service.dto.UsersDTO;

/**
 * Mapper for the entity {@link Ride} and its DTO {@link RideDTO}.
 */
@Mapper(componentModel = "spring")
public interface RideMapper extends EntityMapper<RideDTO, Ride> {
    @Mapping(target = "rideUser", source = "rideUser", qualifiedByName = "usersUserName")
    @Mapping(target = "rideCityFrom", source = "rideCityFrom", qualifiedByName = "cityCityName")
    @Mapping(target = "rideCityTo", source = "rideCityTo", qualifiedByName = "cityCityName")
    RideDTO toDto(Ride s);

    @Named("usersUserName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userName", source = "userName")
    UsersDTO toDtoUsersUserName(Users users);

    @Named("cityCityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cityName", source = "cityName")
    CityDTO toDtoCityCityName(City city);
}
