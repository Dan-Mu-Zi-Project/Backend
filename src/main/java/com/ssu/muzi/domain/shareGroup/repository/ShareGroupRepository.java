package com.ssu.muzi.domain.shareGroup.repository;

import com.ssu.muzi.domain.shareGroup.entity.ShareGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShareGroupRepository extends JpaRepository<ShareGroup, Long> {
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM ShareGroup s " +
            "WHERE s.startedAt <= :endedAt AND s.endedAt >= :startedAt")
    boolean existsProgressingGroup(@Param("startedAt") LocalDateTime startedAt, @Param("endedAt") LocalDateTime endedAt);

    //특정 회원(memberId)이 참여하고 있는 모든 공유 그룹(ShareGroup) 을 중복 없이 조회하는 역할
    @Query("SELECT DISTINCT s FROM ShareGroup s JOIN s.profileList p WHERE p.member.id = :memberId")
    List<ShareGroup> findByMemberId(@Param("memberId") Long memberId);
}

