package com.spingular.cms.service.impl;

import com.spingular.cms.domain.Post;
import com.spingular.cms.repository.PostRepository;
import com.spingular.cms.service.PostService;
import com.spingular.cms.service.dto.PostDTO;
import com.spingular.cms.service.mapper.PostMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Post}.
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public PostDTO save(PostDTO postDTO) {
        log.debug("Request to save Post : {}", postDTO);
        Post post = postMapper.toEntity(postDTO);
        post = postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    public Optional<PostDTO> partialUpdate(PostDTO postDTO) {
        log.debug("Request to partially update Post : {}", postDTO);

        return postRepository
            .findById(postDTO.getId())
            .map(
                existingPost -> {
                    postMapper.partialUpdate(existingPost, postDTO);
                    return existingPost;
                }
            )
            .map(postRepository::save)
            .map(postMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Posts");
        return postRepository.findAll(pageable).map(postMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostDTO> findOne(Long id) {
        log.debug("Request to get Post : {}", id);
        return postRepository.findById(id).map(postMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Post : {}", id);
        postRepository.deleteById(id);
    }
}
