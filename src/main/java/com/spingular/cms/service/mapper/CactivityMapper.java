package com.spingular.cms.service.mapper;

import com.spingular.cms.domain.*;
import com.spingular.cms.service.dto.CactivityDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cactivity} and its DTO {@link CactivityDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommunityMapper.class })
public interface CactivityMapper extends EntityMapper<CactivityDTO, Cactivity> {
    @Mapping(target = "communities", source = "communities", qualifiedByName = "idSet")
    CactivityDTO toDto(Cactivity s);

    @Mapping(target = "removeCommunity", ignore = true)
    Cactivity toEntity(CactivityDTO cactivityDTO);
}
