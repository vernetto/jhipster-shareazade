package org.pierre.shareazade.service.mapper;

import org.mapstruct.*;
import org.pierre.shareazade.domain.ShareUser;
import org.pierre.shareazade.service.dto.ShareUserDTO;

/**
 * Mapper for the entity {@link ShareUser} and its DTO {@link ShareUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShareUserMapper extends EntityMapper<ShareUserDTO, ShareUser> {}
