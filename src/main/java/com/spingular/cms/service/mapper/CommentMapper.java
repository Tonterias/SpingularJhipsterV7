package com.spingular.cms.service.mapper;

import com.spingular.cms.domain.*;
import com.spingular.cms.service.dto.CommentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class, PostMapper.class })
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {
    @Mapping(target = "appuser", source = "appuser", qualifiedByName = "id")
    @Mapping(target = "post", source = "post", qualifiedByName = "id")
    CommentDTO toDto(Comment s);
}
