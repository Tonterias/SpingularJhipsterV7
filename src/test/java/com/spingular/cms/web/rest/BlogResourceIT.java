package com.spingular.cms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spingular.cms.IntegrationTest;
import com.spingular.cms.domain.Appuser;
import com.spingular.cms.domain.Blog;
import com.spingular.cms.domain.Community;
import com.spingular.cms.domain.Post;
import com.spingular.cms.repository.BlogRepository;
import com.spingular.cms.service.criteria.BlogCriteria;
import com.spingular.cms.service.dto.BlogDTO;
import com.spingular.cms.service.mapper.BlogMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link BlogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BlogResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/blogs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBlogMockMvc;

    private Blog blog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Blog createEntity(EntityManager em) {
        Blog blog = new Blog()
            .creationDate(DEFAULT_CREATION_DATE)
            .title(DEFAULT_TITLE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        blog.setAppuser(appuser);
        return blog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Blog createUpdatedEntity(EntityManager em) {
        Blog blog = new Blog()
            .creationDate(UPDATED_CREATION_DATE)
            .title(UPDATED_TITLE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        // Add required entity
        Appuser appuser;
        if (TestUtil.findAll(em, Appuser.class).isEmpty()) {
            appuser = AppuserResourceIT.createUpdatedEntity(em);
            em.persist(appuser);
            em.flush();
        } else {
            appuser = TestUtil.findAll(em, Appuser.class).get(0);
        }
        blog.setAppuser(appuser);
        return blog;
    }

    @BeforeEach
    public void initTest() {
        blog = createEntity(em);
    }

    @Test
    @Transactional
    void createBlog() throws Exception {
        int databaseSizeBeforeCreate = blogRepository.findAll().size();
        // Create the Blog
        BlogDTO blogDTO = blogMapper.toDto(blog);
        restBlogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blogDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Blog in the database
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeCreate + 1);
        Blog testBlog = blogList.get(blogList.size() - 1);
        assertThat(testBlog.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testBlog.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBlog.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testBlog.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createBlogWithExistingId() throws Exception {
        // Create the Blog with an existing ID
        blog.setId(1L);
        BlogDTO blogDTO = blogMapper.toDto(blog);

        int databaseSizeBeforeCreate = blogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Blog in the database
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = blogRepository.findAll().size();
        // set the field null
        blog.setCreationDate(null);

        // Create the Blog, which fails.
        BlogDTO blogDTO = blogMapper.toDto(blog);

        restBlogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blogDTO))
            )
            .andExpect(status().isBadRequest());

        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = blogRepository.findAll().size();
        // set the field null
        blog.setTitle(null);

        // Create the Blog, which fails.
        BlogDTO blogDTO = blogMapper.toDto(blog);

        restBlogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blogDTO))
            )
            .andExpect(status().isBadRequest());

        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBlogs() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList
        restBlogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blog.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getBlog() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get the blog
        restBlogMockMvc
            .perform(get(ENTITY_API_URL_ID, blog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(blog.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getBlogsByIdFiltering() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        Long id = blog.getId();

        defaultBlogShouldBeFound("id.equals=" + id);
        defaultBlogShouldNotBeFound("id.notEquals=" + id);

        defaultBlogShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBlogShouldNotBeFound("id.greaterThan=" + id);

        defaultBlogShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBlogShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBlogsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where creationDate equals to DEFAULT_CREATION_DATE
        defaultBlogShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the blogList where creationDate equals to UPDATED_CREATION_DATE
        defaultBlogShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBlogsByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultBlogShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the blogList where creationDate not equals to UPDATED_CREATION_DATE
        defaultBlogShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBlogsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultBlogShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the blogList where creationDate equals to UPDATED_CREATION_DATE
        defaultBlogShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBlogsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where creationDate is not null
        defaultBlogShouldBeFound("creationDate.specified=true");

        // Get all the blogList where creationDate is null
        defaultBlogShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBlogsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where title equals to DEFAULT_TITLE
        defaultBlogShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the blogList where title equals to UPDATED_TITLE
        defaultBlogShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBlogsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where title not equals to DEFAULT_TITLE
        defaultBlogShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the blogList where title not equals to UPDATED_TITLE
        defaultBlogShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBlogsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultBlogShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the blogList where title equals to UPDATED_TITLE
        defaultBlogShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBlogsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where title is not null
        defaultBlogShouldBeFound("title.specified=true");

        // Get all the blogList where title is null
        defaultBlogShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllBlogsByTitleContainsSomething() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where title contains DEFAULT_TITLE
        defaultBlogShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the blogList where title contains UPDATED_TITLE
        defaultBlogShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBlogsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where title does not contain DEFAULT_TITLE
        defaultBlogShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the blogList where title does not contain UPDATED_TITLE
        defaultBlogShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBlogsByPostIsEqualToSomething() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);
        Post post = PostResourceIT.createEntity(em);
        em.persist(post);
        em.flush();
        blog.addPost(post);
        blogRepository.saveAndFlush(blog);
        Long postId = post.getId();

        // Get all the blogList where post equals to postId
        defaultBlogShouldBeFound("postId.equals=" + postId);

        // Get all the blogList where post equals to (postId + 1)
        defaultBlogShouldNotBeFound("postId.equals=" + (postId + 1));
    }

    @Test
    @Transactional
    void getAllBlogsByAppuserIsEqualToSomething() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);
        Appuser appuser = AppuserResourceIT.createEntity(em);
        em.persist(appuser);
        em.flush();
        blog.setAppuser(appuser);
        blogRepository.saveAndFlush(blog);
        Long appuserId = appuser.getId();

        // Get all the blogList where appuser equals to appuserId
        defaultBlogShouldBeFound("appuserId.equals=" + appuserId);

        // Get all the blogList where appuser equals to (appuserId + 1)
        defaultBlogShouldNotBeFound("appuserId.equals=" + (appuserId + 1));
    }

    @Test
    @Transactional
    void getAllBlogsByCommunityIsEqualToSomething() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);
        Community community = CommunityResourceIT.createEntity(em);
        em.persist(community);
        em.flush();
        blog.setCommunity(community);
        blogRepository.saveAndFlush(blog);
        Long communityId = community.getId();

        // Get all the blogList where community equals to communityId
        defaultBlogShouldBeFound("communityId.equals=" + communityId);

        // Get all the blogList where community equals to (communityId + 1)
        defaultBlogShouldNotBeFound("communityId.equals=" + (communityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBlogShouldBeFound(String filter) throws Exception {
        restBlogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blog.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restBlogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBlogShouldNotBeFound(String filter) throws Exception {
        restBlogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBlogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBlog() throws Exception {
        // Get the blog
        restBlogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBlog() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        int databaseSizeBeforeUpdate = blogRepository.findAll().size();

        // Update the blog
        Blog updatedBlog = blogRepository.findById(blog.getId()).get();
        // Disconnect from session so that the updates on updatedBlog are not directly saved in db
        em.detach(updatedBlog);
        updatedBlog
            .creationDate(UPDATED_CREATION_DATE)
            .title(UPDATED_TITLE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        BlogDTO blogDTO = blogMapper.toDto(updatedBlog);

        restBlogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blogDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blogDTO))
            )
            .andExpect(status().isOk());

        // Validate the Blog in the database
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeUpdate);
        Blog testBlog = blogList.get(blogList.size() - 1);
        assertThat(testBlog.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testBlog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBlog.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testBlog.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingBlog() throws Exception {
        int databaseSizeBeforeUpdate = blogRepository.findAll().size();
        blog.setId(count.incrementAndGet());

        // Create the Blog
        BlogDTO blogDTO = blogMapper.toDto(blog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blogDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Blog in the database
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBlog() throws Exception {
        int databaseSizeBeforeUpdate = blogRepository.findAll().size();
        blog.setId(count.incrementAndGet());

        // Create the Blog
        BlogDTO blogDTO = blogMapper.toDto(blog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Blog in the database
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBlog() throws Exception {
        int databaseSizeBeforeUpdate = blogRepository.findAll().size();
        blog.setId(count.incrementAndGet());

        // Create the Blog
        BlogDTO blogDTO = blogMapper.toDto(blog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(blogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Blog in the database
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBlogWithPatch() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        int databaseSizeBeforeUpdate = blogRepository.findAll().size();

        // Update the blog using partial update
        Blog partialUpdatedBlog = new Blog();
        partialUpdatedBlog.setId(blog.getId());

        partialUpdatedBlog.title(UPDATED_TITLE);

        restBlogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlog.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBlog))
            )
            .andExpect(status().isOk());

        // Validate the Blog in the database
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeUpdate);
        Blog testBlog = blogList.get(blogList.size() - 1);
        assertThat(testBlog.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testBlog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBlog.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testBlog.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateBlogWithPatch() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        int databaseSizeBeforeUpdate = blogRepository.findAll().size();

        // Update the blog using partial update
        Blog partialUpdatedBlog = new Blog();
        partialUpdatedBlog.setId(blog.getId());

        partialUpdatedBlog
            .creationDate(UPDATED_CREATION_DATE)
            .title(UPDATED_TITLE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restBlogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlog.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBlog))
            )
            .andExpect(status().isOk());

        // Validate the Blog in the database
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeUpdate);
        Blog testBlog = blogList.get(blogList.size() - 1);
        assertThat(testBlog.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testBlog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBlog.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testBlog.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingBlog() throws Exception {
        int databaseSizeBeforeUpdate = blogRepository.findAll().size();
        blog.setId(count.incrementAndGet());

        // Create the Blog
        BlogDTO blogDTO = blogMapper.toDto(blog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, blogDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(blogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Blog in the database
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBlog() throws Exception {
        int databaseSizeBeforeUpdate = blogRepository.findAll().size();
        blog.setId(count.incrementAndGet());

        // Create the Blog
        BlogDTO blogDTO = blogMapper.toDto(blog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(blogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Blog in the database
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBlog() throws Exception {
        int databaseSizeBeforeUpdate = blogRepository.findAll().size();
        blog.setId(count.incrementAndGet());

        // Create the Blog
        BlogDTO blogDTO = blogMapper.toDto(blog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(blogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Blog in the database
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBlog() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        int databaseSizeBeforeDelete = blogRepository.findAll().size();

        // Delete the blog
        restBlogMockMvc
            .perform(delete(ENTITY_API_URL_ID, blog.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
