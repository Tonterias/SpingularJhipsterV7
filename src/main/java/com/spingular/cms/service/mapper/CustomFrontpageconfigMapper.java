package com.spingular.cms.service.mapper;

import com.spingular.cms.domain.*;
import com.spingular.cms.service.dto.CustomFrontpageconfigDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Frontpageconfig and its DTO FrontpageconfigDTO.
 */
@Mapper(componentModel = "spring", uses = { FrontPagePostMapper.class })
public interface CustomFrontpageconfigMapper extends EntityMapper<CustomFrontpageconfigDTO, Frontpageconfig> {
    default Frontpageconfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        Frontpageconfig frontpageconfig = new Frontpageconfig();
        frontpageconfig.setId(id);
        return frontpageconfig;
    }
}
