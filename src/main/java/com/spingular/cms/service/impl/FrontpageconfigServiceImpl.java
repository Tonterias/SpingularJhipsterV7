package com.spingular.cms.service.impl;

import com.spingular.cms.domain.Frontpageconfig;
import com.spingular.cms.repository.FrontpageconfigRepository;
import com.spingular.cms.service.FrontpageconfigService;
import com.spingular.cms.service.dto.FrontpageconfigDTO;
import com.spingular.cms.service.mapper.FrontpageconfigMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Frontpageconfig}.
 */
@Service
@Transactional
public class FrontpageconfigServiceImpl implements FrontpageconfigService {

    private final Logger log = LoggerFactory.getLogger(FrontpageconfigServiceImpl.class);

    private final FrontpageconfigRepository frontpageconfigRepository;

    private final FrontpageconfigMapper frontpageconfigMapper;

    public FrontpageconfigServiceImpl(FrontpageconfigRepository frontpageconfigRepository, FrontpageconfigMapper frontpageconfigMapper) {
        this.frontpageconfigRepository = frontpageconfigRepository;
        this.frontpageconfigMapper = frontpageconfigMapper;
    }

    @Override
    public FrontpageconfigDTO save(FrontpageconfigDTO frontpageconfigDTO) {
        log.debug("Request to save Frontpageconfig : {}", frontpageconfigDTO);
        Frontpageconfig frontpageconfig = frontpageconfigMapper.toEntity(frontpageconfigDTO);
        frontpageconfig = frontpageconfigRepository.save(frontpageconfig);
        return frontpageconfigMapper.toDto(frontpageconfig);
    }

    @Override
    public Optional<FrontpageconfigDTO> partialUpdate(FrontpageconfigDTO frontpageconfigDTO) {
        log.debug("Request to partially update Frontpageconfig : {}", frontpageconfigDTO);

        return frontpageconfigRepository
            .findById(frontpageconfigDTO.getId())
            .map(
                existingFrontpageconfig -> {
                    frontpageconfigMapper.partialUpdate(existingFrontpageconfig, frontpageconfigDTO);
                    return existingFrontpageconfig;
                }
            )
            .map(frontpageconfigRepository::save)
            .map(frontpageconfigMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FrontpageconfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Frontpageconfigs");
        return frontpageconfigRepository.findAll(pageable).map(frontpageconfigMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FrontpageconfigDTO> findOne(Long id) {
        log.debug("Request to get Frontpageconfig : {}", id);
        return frontpageconfigRepository.findById(id).map(frontpageconfigMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Frontpageconfig : {}", id);
        frontpageconfigRepository.deleteById(id);
    }
}
