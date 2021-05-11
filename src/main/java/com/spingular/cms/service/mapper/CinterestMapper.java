package com.spingular.cms.service.mapper;

import com.spingular.cms.domain.*;
import com.spingular.cms.service.dto.CinterestDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cinterest} and its DTO {@link CinterestDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommunityMapper.class })
public interface CinterestMapper extends EntityMapper<CinterestDTO, Cinterest> {
    @Mapping(target = "communities", source = "communities", qualifiedByName = "idSet")
    CinterestDTO toDto(Cinterest s);

    @Mapping(target = "removeCommunity", ignore = true)
    Cinterest toEntity(CinterestDTO cinterestDTO);
}
