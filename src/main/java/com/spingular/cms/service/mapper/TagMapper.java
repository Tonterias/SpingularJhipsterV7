package com.spingular.cms.service.mapper;

import com.spingular.cms.domain.*;
import com.spingular.cms.service.dto.TagDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring", uses = { PostMapper.class })
public interface TagMapper extends EntityMapper<TagDTO, Tag> {
    @Mapping(target = "posts", source = "posts", qualifiedByName = "headlineSet")
    TagDTO toDto(Tag s);

    @Mapping(target = "removePost", ignore = true)
    Tag toEntity(TagDTO tagDTO);
}
