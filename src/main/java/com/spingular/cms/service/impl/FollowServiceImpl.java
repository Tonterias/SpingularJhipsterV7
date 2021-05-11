package com.spingular.cms.service.impl;

import com.spingular.cms.domain.Follow;
import com.spingular.cms.repository.FollowRepository;
import com.spingular.cms.service.FollowService;
import com.spingular.cms.service.dto.FollowDTO;
import com.spingular.cms.service.mapper.FollowMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Follow}.
 */
@Service
@Transactional
public class FollowServiceImpl implements FollowService {

    private final Logger log = LoggerFactory.getLogger(FollowServiceImpl.class);

    private final FollowRepository followRepository;

    private final FollowMapper followMapper;

    public FollowServiceImpl(FollowRepository followRepository, FollowMapper followMapper) {
        this.followRepository = followRepository;
        this.followMapper = followMapper;
    }

    @Override
    public FollowDTO save(FollowDTO followDTO) {
        log.debug("Request to save Follow : {}", followDTO);
        Follow follow = followMapper.toEntity(followDTO);
        follow = followRepository.save(follow);
        return followMapper.toDto(follow);
    }

    @Override
    public Optional<FollowDTO> partialUpdate(FollowDTO followDTO) {
        log.debug("Request to partially update Follow : {}", followDTO);

        return followRepository
            .findById(followDTO.getId())
            .map(
                existingFollow -> {
                    followMapper.partialUpdate(existingFollow, followDTO);
                    return existingFollow;
                }
            )
            .map(followRepository::save)
            .map(followMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FollowDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Follows");
        return followRepository.findAll(pageable).map(followMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FollowDTO> findOne(Long id) {
        log.debug("Request to get Follow : {}", id);
        return followRepository.findById(id).map(followMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Follow : {}", id);
        followRepository.deleteById(id);
    }
}
