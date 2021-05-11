package com.spingular.cms.service.impl;

import com.spingular.cms.domain.Blockuser;
import com.spingular.cms.repository.BlockuserRepository;
import com.spingular.cms.service.BlockuserService;
import com.spingular.cms.service.dto.BlockuserDTO;
import com.spingular.cms.service.mapper.BlockuserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Blockuser}.
 */
@Service
@Transactional
public class BlockuserServiceImpl implements BlockuserService {

    private final Logger log = LoggerFactory.getLogger(BlockuserServiceImpl.class);

    private final BlockuserRepository blockuserRepository;

    private final BlockuserMapper blockuserMapper;

    public BlockuserServiceImpl(BlockuserRepository blockuserRepository, BlockuserMapper blockuserMapper) {
        this.blockuserRepository = blockuserRepository;
        this.blockuserMapper = blockuserMapper;
    }

    @Override
    public BlockuserDTO save(BlockuserDTO blockuserDTO) {
        log.debug("Request to save Blockuser : {}", blockuserDTO);
        Blockuser blockuser = blockuserMapper.toEntity(blockuserDTO);
        blockuser = blockuserRepository.save(blockuser);
        return blockuserMapper.toDto(blockuser);
    }

    @Override
    public Optional<BlockuserDTO> partialUpdate(BlockuserDTO blockuserDTO) {
        log.debug("Request to partially update Blockuser : {}", blockuserDTO);

        return blockuserRepository
            .findById(blockuserDTO.getId())
            .map(
                existingBlockuser -> {
                    blockuserMapper.partialUpdate(existingBlockuser, blockuserDTO);
                    return existingBlockuser;
                }
            )
            .map(blockuserRepository::save)
            .map(blockuserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlockuserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Blockusers");
        return blockuserRepository.findAll(pageable).map(blockuserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BlockuserDTO> findOne(Long id) {
        log.debug("Request to get Blockuser : {}", id);
        return blockuserRepository.findById(id).map(blockuserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Blockuser : {}", id);
        blockuserRepository.deleteById(id);
    }
}
