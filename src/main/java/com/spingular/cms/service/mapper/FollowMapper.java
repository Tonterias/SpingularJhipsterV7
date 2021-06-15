package com.spingular.cms.service.mapper;

import com.spingular.cms.domain.*;
import com.spingular.cms.service.dto.FollowDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Follow} and its DTO {@link FollowDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class, CommunityMapper.class })
public interface FollowMapper extends EntityMapper<FollowDTO, Follow> {
    @Mapping(target = "followed", source = "followed")
    @Mapping(target = "following", source = "following")
    @Mapping(target = "cfollowed", source = "cfollowed")
    @Mapping(target = "cfollowing", source = "cfollowing")
    FollowDTO toDto(Follow s);
}
