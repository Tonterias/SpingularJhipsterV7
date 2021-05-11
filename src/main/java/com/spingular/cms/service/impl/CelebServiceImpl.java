package com.spingular.cms.service.impl;

import com.spingular.cms.domain.Celeb;
import com.spingular.cms.repository.CelebRepository;
import com.spingular.cms.service.CelebService;
import com.spingular.cms.service.dto.CelebDTO;
import com.spingular.cms.service.mapper.CelebMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Celeb}.
 */
@Service
@Transactional
public class CelebServiceImpl implements CelebService {

    private final Logger log = LoggerFactory.getLogger(CelebServiceImpl.class);

    private final CelebRepository celebRepository;

    private final CelebMapper celebMapper;

    public CelebServiceImpl(CelebRepository celebRepository, CelebMapper celebMapper) {
        this.celebRepository = celebRepository;
        this.celebMapper = celebMapper;
    }

    @Override
    public CelebDTO save(CelebDTO celebDTO) {
        log.debug("Request to save Celeb : {}", celebDTO);
        Celeb celeb = celebMapper.toEntity(celebDTO);
        celeb = celebRepository.save(celeb);
        return celebMapper.toDto(celeb);
    }

    @Override
    public Optional<CelebDTO> partialUpdate(CelebDTO celebDTO) {
        log.debug("Request to partially update Celeb : {}", celebDTO);

        return celebRepository
            .findById(celebDTO.getId())
            .map(
                existingCeleb -> {
                    celebMapper.partialUpdate(existingCeleb, celebDTO);
                    return existingCeleb;
                }
            )
            .map(celebRepository::save)
            .map(celebMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CelebDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Celebs");
        return celebRepository.findAll(pageable).map(celebMapper::toDto);
    }

    public Page<CelebDTO> findAllWithEagerRelationships(Pageable pageable) {
        return celebRepository.findAllWithEagerRelationships(pageable).map(celebMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CelebDTO> findOne(Long id) {
        log.debug("Request to get Celeb : {}", id);
        return celebRepository.findOneWithEagerRelationships(id).map(celebMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Celeb : {}", id);
        celebRepository.deleteById(id);
    }
}
