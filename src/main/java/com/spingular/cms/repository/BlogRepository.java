package com.spingular.cms.repository;

import com.spingular.cms.domain.Blog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Blog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {}
