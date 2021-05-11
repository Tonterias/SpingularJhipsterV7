package com.spingular.cms.service.mapper;

import com.spingular.cms.domain.*;
import com.spingular.cms.service.dto.UrllinkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Urllink} and its DTO {@link UrllinkDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UrllinkMapper extends EntityMapper<UrllinkDTO, Urllink> {}
