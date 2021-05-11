package com.spingular.cms.repository;

import com.spingular.cms.domain.Appphoto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Appphoto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppphotoRepository extends JpaRepository<Appphoto, Long>, JpaSpecificationExecutor<Appphoto> {}
