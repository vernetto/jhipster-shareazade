package org.pierre.shareazade.service.mapper;

import org.mapstruct.*;
import org.pierre.shareazade.domain.Users;
import org.pierre.shareazade.service.dto.UsersDTO;

/**
 * Mapper for the entity {@link Users} and its DTO {@link UsersDTO}.
 */
@Mapper(componentModel = "spring")
public interface UsersMapper extends EntityMapper<UsersDTO, Users> {}
