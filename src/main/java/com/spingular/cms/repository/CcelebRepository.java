package com.spingular.cms.repository;

import com.spingular.cms.domain.Cceleb;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cceleb entity.
 */
@Repository
public interface CcelebRepository extends JpaRepository<Cceleb, Long>, JpaSpecificationExecutor<Cceleb> {
    @Query(
        value = "select distinct cceleb from Cceleb cceleb left join fetch cceleb.communities",
        countQuery = "select count(distinct cceleb) from Cceleb cceleb"
    )
    Page<Cceleb> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct cceleb from Cceleb cceleb left join fetch cceleb.communities")
    List<Cceleb> findAllWithEagerRelationships();

    @Query("select cceleb from Cceleb cceleb left join fetch cceleb.communities where cceleb.id =:id")
    Optional<Cceleb> findOneWithEagerRelationships(@Param("id") Long id);
}
