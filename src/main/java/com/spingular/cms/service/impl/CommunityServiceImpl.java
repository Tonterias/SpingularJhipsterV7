package com.spingular.cms.service.impl;

import com.spingular.cms.domain.Community;
import com.spingular.cms.repository.CommunityRepository;
import com.spingular.cms.service.CommunityService;
import com.spingular.cms.service.dto.CommunityDTO;
import com.spingular.cms.service.mapper.CommunityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Community}.
 */
@Service
@Transactional
public class CommunityServiceImpl implements CommunityService {

    private final Logger log = LoggerFactory.getLogger(CommunityServiceImpl.class);

    private final CommunityRepository communityRepository;

    private final CommunityMapper communityMapper;

    public CommunityServiceImpl(CommunityRepository communityRepository, CommunityMapper communityMapper) {
        this.communityRepository = communityRepository;
        this.communityMapper = communityMapper;
    }

    @Override
    public CommunityDTO save(CommunityDTO communityDTO) {
        log.debug("Request to save Community : {}", communityDTO);
        Community community = communityMapper.toEntity(communityDTO);
        community = communityRepository.save(community);
        return communityMapper.toDto(community);
    }

    @Override
    public Optional<CommunityDTO> partialUpdate(CommunityDTO communityDTO) {
        log.debug("Request to partially update Community : {}", communityDTO);

        return communityRepository
            .findById(communityDTO.getId())
            .map(
                existingCommunity -> {
                    communityMapper.partialUpdate(existingCommunity, communityDTO);
                    return existingCommunity;
                }
            )
            .map(communityRepository::save)
            .map(communityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommunityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Communities");
        return communityRepository.findAll(pageable).map(communityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommunityDTO> findOne(Long id) {
        log.debug("Request to get Community : {}", id);
        return communityRepository.findById(id).map(communityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Community : {}", id);
        communityRepository.deleteById(id);
    }
}
