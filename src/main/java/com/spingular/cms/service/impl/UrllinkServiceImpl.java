package com.spingular.cms.service.impl;

import com.spingular.cms.domain.Urllink;
import com.spingular.cms.repository.UrllinkRepository;
import com.spingular.cms.service.UrllinkService;
import com.spingular.cms.service.dto.UrllinkDTO;
import com.spingular.cms.service.mapper.UrllinkMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Urllink}.
 */
@Service
@Transactional
public class UrllinkServiceImpl implements UrllinkService {

    private final Logger log = LoggerFactory.getLogger(UrllinkServiceImpl.class);

    private final UrllinkRepository urllinkRepository;

    private final UrllinkMapper urllinkMapper;

    public UrllinkServiceImpl(UrllinkRepository urllinkRepository, UrllinkMapper urllinkMapper) {
        this.urllinkRepository = urllinkRepository;
        this.urllinkMapper = urllinkMapper;
    }

    @Override
    public UrllinkDTO save(UrllinkDTO urllinkDTO) {
        log.debug("Request to save Urllink : {}", urllinkDTO);
        Urllink urllink = urllinkMapper.toEntity(urllinkDTO);
        urllink = urllinkRepository.save(urllink);
        return urllinkMapper.toDto(urllink);
    }

    @Override
    public Optional<UrllinkDTO> partialUpdate(UrllinkDTO urllinkDTO) {
        log.debug("Request to partially update Urllink : {}", urllinkDTO);

        return urllinkRepository
            .findById(urllinkDTO.getId())
            .map(
                existingUrllink -> {
                    urllinkMapper.partialUpdate(existingUrllink, urllinkDTO);
                    return existingUrllink;
                }
            )
            .map(urllinkRepository::save)
            .map(urllinkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UrllinkDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Urllinks");
        return urllinkRepository.findAll(pageable).map(urllinkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UrllinkDTO> findOne(Long id) {
        log.debug("Request to get Urllink : {}", id);
        return urllinkRepository.findById(id).map(urllinkMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Urllink : {}", id);
        urllinkRepository.deleteById(id);
    }
}
