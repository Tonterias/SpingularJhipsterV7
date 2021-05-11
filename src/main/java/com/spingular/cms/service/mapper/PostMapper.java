package com.spingular.cms.service.mapper;

import com.spingular.cms.domain.*;
import com.spingular.cms.service.dto.PostDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Post} and its DTO {@link PostDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class, BlogMapper.class })
public interface PostMapper extends EntityMapper<PostDTO, Post> {
    @Mapping(target = "appuser", source = "appuser", qualifiedByName = "id")
    @Mapping(target = "blog", source = "blog", qualifiedByName = "title")
    PostDTO toDto(Post s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostDTO toDtoId(Post post);

    @Named("headlineSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "headline", source = "headline")
    Set<PostDTO> toDtoHeadlineSet(Set<Post> post);
}
