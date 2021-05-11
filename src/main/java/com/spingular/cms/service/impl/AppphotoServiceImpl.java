package com.spingular.cms.service.impl;

import com.spingular.cms.domain.Appphoto;
import com.spingular.cms.repository.AppphotoRepository;
import com.spingular.cms.service.AppphotoService;
import com.spingular.cms.service.dto.AppphotoDTO;
import com.spingular.cms.service.mapper.AppphotoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Appphoto}.
 */
@Service
@Transactional
public class AppphotoServiceImpl implements AppphotoService {

    private final Logger log = LoggerFactory.getLogger(AppphotoServiceImpl.class);

    private final AppphotoRepository appphotoRepository;

    private final AppphotoMapper appphotoMapper;

    public AppphotoServiceImpl(AppphotoRepository appphotoRepository, AppphotoMapper appphotoMapper) {
        this.appphotoRepository = appphotoRepository;
        this.appphotoMapper = appphotoMapper;
    }

    @Override
    public AppphotoDTO save(AppphotoDTO appphotoDTO) {
        log.debug("Request to save Appphoto : {}", appphotoDTO);
        Appphoto appphoto = appphotoMapper.toEntity(appphotoDTO);
        appphoto = appphotoRepository.save(appphoto);
        return appphotoMapper.toDto(appphoto);
    }

    @Override
    public Optional<AppphotoDTO> partialUpdate(AppphotoDTO appphotoDTO) {
        log.debug("Request to partially update Appphoto : {}", appphotoDTO);

        return appphotoRepository
            .findById(appphotoDTO.getId())
            .map(
                existingAppphoto -> {
                    appphotoMapper.partialUpdate(existingAppphoto, appphotoDTO);
                    return existingAppphoto;
                }
            )
            .map(appphotoRepository::save)
            .map(appphotoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppphotoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Appphotos");
        return appphotoRepository.findAll(pageable).map(appphotoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppphotoDTO> findOne(Long id) {
        log.debug("Request to get Appphoto : {}", id);
        return appphotoRepository.findById(id).map(appphotoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Appphoto : {}", id);
        appphotoRepository.deleteById(id);
    }
}
