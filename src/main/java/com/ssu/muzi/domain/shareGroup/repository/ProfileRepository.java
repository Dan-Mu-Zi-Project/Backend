package com.ssu.muzi.domain.shareGroup.repository;

import com.ssu.muzi.domain.shareGroup.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsByShareGroupIdAndMemberId(Long shareGroupId, Long memberId);
    // shareGroupId와 memberId에 부합하는 profile id 찾기
    Optional<Profile> findByMemberIdAndShareGroupId(Long memberId, Long shareGroupId);

    // 주어진 memberId에 해당하는 모든 Profile을 조회
    @Query("select p from Profile p where p.member.id = :memberId and p.deletedAt is null")
    List<Profile> findByMemberId(@Param("memberId") Long memberId);

    // 주어진 shareGroupId에 해당하는 모든 Profile을 조회
    @Query("select p from Profile p where p.shareGroup.id = :shareGroupId and p.deletedAt is null")
    List<Profile> findByShareGroupId(@Param("shareGroupId") Long shareGroupId);
}
