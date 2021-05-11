package com.spingular.cms.service.impl;

import com.spingular.cms.domain.Topic;
import com.spingular.cms.repository.TopicRepository;
import com.spingular.cms.service.TopicService;
import com.spingular.cms.service.dto.TopicDTO;
import com.spingular.cms.service.mapper.TopicMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Topic}.
 */
@Service
@Transactional
public class TopicServiceImpl implements TopicService {

    private final Logger log = LoggerFactory.getLogger(TopicServiceImpl.class);

    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;

    public TopicServiceImpl(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    @Override
    public TopicDTO save(TopicDTO topicDTO) {
        log.debug("Request to save Topic : {}", topicDTO);
        Topic topic = topicMapper.toEntity(topicDTO);
        topic = topicRepository.save(topic);
        return topicMapper.toDto(topic);
    }

    @Override
    public Optional<TopicDTO> partialUpdate(TopicDTO topicDTO) {
        log.debug("Request to partially update Topic : {}", topicDTO);

        return topicRepository
            .findById(topicDTO.getId())
            .map(
                existingTopic -> {
                    topicMapper.partialUpdate(existingTopic, topicDTO);
                    return existingTopic;
                }
            )
            .map(topicRepository::save)
            .map(topicMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TopicDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Topics");
        return topicRepository.findAll(pageable).map(topicMapper::toDto);
    }

    public Page<TopicDTO> findAllWithEagerRelationships(Pageable pageable) {
        return topicRepository.findAllWithEagerRelationships(pageable).map(topicMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TopicDTO> findOne(Long id) {
        log.debug("Request to get Topic : {}", id);
        return topicRepository.findOneWithEagerRelationships(id).map(topicMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Topic : {}", id);
        topicRepository.deleteById(id);
    }
}
