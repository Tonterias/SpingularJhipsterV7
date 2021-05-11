package com.spingular.cms.repository;

import com.spingular.cms.domain.Follow;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Follow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long>, JpaSpecificationExecutor<Follow> {}
