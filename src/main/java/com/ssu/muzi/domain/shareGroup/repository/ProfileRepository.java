package com.ssu.muzi.domain.shareGroup.repository;

import com.ssu.muzi.domain.shareGroup.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsByShareGroupIdAndMemberId(Long shareGroupId, Long memberId);
    // shareGroupId와 memberId에 부합하는 profile id 찾기
    Optional<Profile> findByMemberIdAndShareGroupId(Long memberId, Long shareGroupId);
}
