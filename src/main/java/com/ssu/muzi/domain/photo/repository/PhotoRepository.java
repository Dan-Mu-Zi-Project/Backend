package com.ssu.muzi.domain.photo.repository;

import com.ssu.muzi.domain.photo.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    // PhotoProfileMap을 조인하여 특정 profileId에 매핑된 사진들을 페이징 조회
    @Query("select distinct p from Photo p join PhotoProfileMap ppm on p = ppm.photo where ppm.profile.id = :profileId")
    Page<Photo> findByProfileId(@Param("profileId") Long profileId, Pageable pageable);;
}
