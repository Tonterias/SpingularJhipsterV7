package com.spingular.cms.service.impl;

import com.spingular.cms.domain.ConfigVariables;
import com.spingular.cms.repository.ConfigVariablesRepository;
import com.spingular.cms.service.ConfigVariablesService;
import com.spingular.cms.service.dto.ConfigVariablesDTO;
import com.spingular.cms.service.mapper.ConfigVariablesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ConfigVariables}.
 */
@Service
@Transactional
public class ConfigVariablesServiceImpl implements ConfigVariablesService {

    private final Logger log = LoggerFactory.getLogger(ConfigVariablesServiceImpl.class);

    private final ConfigVariablesRepository configVariablesRepository;

    private final ConfigVariablesMapper configVariablesMapper;

    public ConfigVariablesServiceImpl(ConfigVariablesRepository configVariablesRepository, ConfigVariablesMapper configVariablesMapper) {
        this.configVariablesRepository = configVariablesRepository;
        this.configVariablesMapper = configVariablesMapper;
    }

    @Override
    public ConfigVariablesDTO save(ConfigVariablesDTO configVariablesDTO) {
        log.debug("Request to save ConfigVariables : {}", configVariablesDTO);
        ConfigVariables configVariables = configVariablesMapper.toEntity(configVariablesDTO);
        configVariables = configVariablesRepository.save(configVariables);
        return configVariablesMapper.toDto(configVariables);
    }

    @Override
    public Optional<ConfigVariablesDTO> partialUpdate(ConfigVariablesDTO configVariablesDTO) {
        log.debug("Request to partially update ConfigVariables : {}", configVariablesDTO);

        return configVariablesRepository
            .findById(configVariablesDTO.getId())
            .map(
                existingConfigVariables -> {
                    configVariablesMapper.partialUpdate(existingConfigVariables, configVariablesDTO);
                    return existingConfigVariables;
                }
            )
            .map(configVariablesRepository::save)
            .map(configVariablesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConfigVariablesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigVariables");
        return configVariablesRepository.findAll(pageable).map(configVariablesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigVariablesDTO> findOne(Long id) {
        log.debug("Request to get ConfigVariables : {}", id);
        return configVariablesRepository.findById(id).map(configVariablesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigVariables : {}", id);
        configVariablesRepository.deleteById(id);
    }
}
