package com.spingular.cms.repository;

import com.spingular.cms.domain.Frontpageconfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Frontpageconfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FrontpageconfigRepository extends JpaRepository<Frontpageconfig, Long>, JpaSpecificationExecutor<Frontpageconfig> {}
