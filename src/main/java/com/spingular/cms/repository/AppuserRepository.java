package com.spingular.cms.repository;

import java.util.Optional;

import com.spingular.cms.domain.Appuser;
import com.spingular.cms.domain.User;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Appuser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppuserRepository extends JpaRepository<Appuser, Long>, JpaSpecificationExecutor<Appuser> {

    Appuser findByUserId(Long id);

}
