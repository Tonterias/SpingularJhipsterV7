package com.spingular.cms.service.mapper;

import com.spingular.cms.domain.*;
import com.spingular.cms.service.dto.ConfigVariablesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigVariables} and its DTO {@link ConfigVariablesDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigVariablesMapper extends EntityMapper<ConfigVariablesDTO, ConfigVariables> {}
