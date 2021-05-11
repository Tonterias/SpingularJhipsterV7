package com.spingular.cms.repository;

import com.spingular.cms.domain.Interest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Interest entity.
 */
@Repository
public interface InterestRepository extends JpaRepository<Interest, Long>, JpaSpecificationExecutor<Interest> {
    @Query(
        value = "select distinct interest from Interest interest left join fetch interest.appusers",
        countQuery = "select count(distinct interest) from Interest interest"
    )
    Page<Interest> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct interest from Interest interest left join fetch interest.appusers")
    List<Interest> findAllWithEagerRelationships();

    @Query("select interest from Interest interest left join fetch interest.appusers where interest.id =:id")
    Optional<Interest> findOneWithEagerRelationships(@Param("id") Long id);
}
