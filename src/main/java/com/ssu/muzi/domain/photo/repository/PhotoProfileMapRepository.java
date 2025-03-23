package com.ssu.muzi.domain.photo.repository;

import com.ssu.muzi.domain.photo.entity.Photo;
import com.ssu.muzi.domain.photo.entity.PhotoProfileMap;
import com.ssu.muzi.domain.shareGroup.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoProfileMapRepository extends JpaRepository<PhotoProfileMap, Long> {
    // 해당 Profile에 매핑된 PhotoProfileMap 리스트 조회
    List<PhotoProfileMap> findByProfile(Profile profile);
    // 해당 Photo에 매핑된 프로필 리스트 조회
    List<PhotoProfileMap> findByPhoto(Photo photo);
    // photo에 엮인 활성 프로필 수 개수 새기
    @Query("select count(ppm) from PhotoProfileMap ppm where ppm.photo = :photo and ppm.profile.deletedAt is null")
    long countByPhotoAndProfileDeletedAtIsNull(@Param("photo") Photo photo);;
}
