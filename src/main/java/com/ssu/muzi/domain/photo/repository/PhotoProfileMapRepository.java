package com.ssu.muzi.domain.photo.repository;

import com.ssu.muzi.domain.photo.entity.PhotoProfileMap;
import com.ssu.muzi.domain.shareGroup.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoProfileMapRepository extends JpaRepository<PhotoProfileMap, Long> {
    // 해당 Profile에 매핑된 PhotoProfileMap 리스트 조회
    List<PhotoProfileMap> findByProfile(Profile profile);
}
