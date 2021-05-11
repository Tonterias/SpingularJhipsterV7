package com.spingular.cms.service.mapper;

import com.spingular.cms.domain.*;
import com.spingular.cms.service.dto.FeedbackDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Feedback} and its DTO {@link FeedbackDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FeedbackMapper extends EntityMapper<FeedbackDTO, Feedback> {}
