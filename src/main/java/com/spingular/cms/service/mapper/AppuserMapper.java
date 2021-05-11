package com.spingular.cms.service.mapper;

import com.spingular.cms.domain.*;
import com.spingular.cms.service.dto.AppuserDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Appuser} and its DTO {@link AppuserDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface AppuserMapper extends EntityMapper<AppuserDTO, Appuser> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    AppuserDTO toDto(Appuser s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppuserDTO toDtoId(Appuser appuser);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<AppuserDTO> toDtoIdSet(Set<Appuser> appuser);
}
