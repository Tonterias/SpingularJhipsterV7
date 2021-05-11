package com.spingular.cms.repository;

import com.spingular.cms.domain.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tag entity.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {
    @Query(value = "select distinct tag from Tag tag left join fetch tag.posts", countQuery = "select count(distinct tag) from Tag tag")
    Page<Tag> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct tag from Tag tag left join fetch tag.posts")
    List<Tag> findAllWithEagerRelationships();

    @Query("select tag from Tag tag left join fetch tag.posts where tag.id =:id")
    Optional<Tag> findOneWithEagerRelationships(@Param("id") Long id);
}
