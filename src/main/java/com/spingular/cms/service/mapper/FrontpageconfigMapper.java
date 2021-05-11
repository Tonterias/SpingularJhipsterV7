package com.spingular.cms.service.mapper;

import com.spingular.cms.domain.*;
import com.spingular.cms.service.dto.FrontpageconfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Frontpageconfig} and its DTO {@link FrontpageconfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FrontpageconfigMapper extends EntityMapper<FrontpageconfigDTO, Frontpageconfig> {}
