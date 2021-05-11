package com.spingular.cms.repository;

import com.spingular.cms.domain.Appuser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Appuser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppuserRepository extends JpaRepository<Appuser, Long>, JpaSpecificationExecutor<Appuser> {}
