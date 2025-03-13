package com.ssu.muzi.domain.shareGroup.repository;

import com.ssu.muzi.domain.shareGroup.entity.ShareGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShareGroupRepository extends JpaRepository<ShareGroup, Long> {
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM ShareGroup s " +
            "WHERE s.startedAt <= :endedAt AND s.endedAt >= :startedAt")
    boolean existsProgressingGroup(@Param("startedAt") LocalDateTime startedAt, @Param("endedAt") LocalDateTime endedAt);
}