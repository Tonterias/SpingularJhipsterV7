package com.spingular.cms.repository;

import com.spingular.cms.domain.Activity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Activity entity.
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {
    @Query(
        value = "select distinct activity from Activity activity left join fetch activity.appusers",
        countQuery = "select count(distinct activity) from Activity activity"
    )
    Page<Activity> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct activity from Activity activity left join fetch activity.appusers")
    List<Activity> findAllWithEagerRelationships();

    @Query("select activity from Activity activity left join fetch activity.appusers where activity.id =:id")
    Optional<Activity> findOneWithEagerRelationships(@Param("id") Long id);
}
