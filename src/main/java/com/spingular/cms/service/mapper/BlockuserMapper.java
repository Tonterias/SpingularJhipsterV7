package com.spingular.cms.service.mapper;

import com.spingular.cms.domain.*;
import com.spingular.cms.service.dto.BlockuserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Blockuser} and its DTO {@link BlockuserDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class, CommunityMapper.class })
public interface BlockuserMapper extends EntityMapper<BlockuserDTO, Blockuser> {
    @Mapping(target = "blockeduser", source = "blockeduser", qualifiedByName = "id")
    @Mapping(target = "blockinguser", source = "blockinguser", qualifiedByName = "id")
    @Mapping(target = "cblockeduser", source = "cblockeduser", qualifiedByName = "id")
    @Mapping(target = "cblockinguser", source = "cblockinguser", qualifiedByName = "id")
    BlockuserDTO toDto(Blockuser s);
}
