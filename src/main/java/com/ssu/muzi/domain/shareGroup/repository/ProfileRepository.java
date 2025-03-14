package com.ssu.muzi.domain.shareGroup.repository;

import com.ssu.muzi.domain.shareGroup.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsByShareGroupIdAndMemberId(Long shareGroupId, Long memberId);
}
