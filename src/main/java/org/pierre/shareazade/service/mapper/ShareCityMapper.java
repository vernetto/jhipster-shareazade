package org.pierre.shareazade.service.mapper;

import org.mapstruct.*;
import org.pierre.shareazade.domain.ShareCity;
import org.pierre.shareazade.service.dto.ShareCityDTO;

/**
 * Mapper for the entity {@link ShareCity} and its DTO {@link ShareCityDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShareCityMapper extends EntityMapper<ShareCityDTO, ShareCity> {}
