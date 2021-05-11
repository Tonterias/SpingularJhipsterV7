package com.spingular.cms.service.mapper;

import com.spingular.cms.domain.*;
import com.spingular.cms.service.dto.BlogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Blog} and its DTO {@link BlogDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class, CommunityMapper.class })
public interface BlogMapper extends EntityMapper<BlogDTO, Blog> {
    @Mapping(target = "appuser", source = "appuser", qualifiedByName = "id")
    @Mapping(target = "community", source = "community", qualifiedByName = "communityName")
    BlogDTO toDto(Blog s);

    @Named("title")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    BlogDTO toDtoTitle(Blog blog);
}
