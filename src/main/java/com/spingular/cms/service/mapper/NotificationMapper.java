package com.spingular.cms.service.mapper;

import com.spingular.cms.domain.*;
import com.spingular.cms.service.dto.NotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppuserMapper.class })
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "appuser", source = "appuser", qualifiedByName = "id")
    NotificationDTO toDto(Notification s);
}
