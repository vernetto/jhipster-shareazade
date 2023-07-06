package org.pierre.shareazade.service.mapper;

import org.mapstruct.*;
import org.pierre.shareazade.domain.City;
import org.pierre.shareazade.service.dto.CityDTO;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring")
public interface CityMapper extends EntityMapper<CityDTO, City> {}
