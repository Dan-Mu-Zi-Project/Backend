package com.ssu.muzi.domain.photo.repository;

import com.ssu.muzi.domain.photo.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    // PhotoProfileMap을 조인하여 특정 profileId에 매핑된 사진들을 페이징 조회
    @Query("select distinct p from Photo p join PhotoProfileMap ppm on p = ppm.photo where ppm.profile.id = :profileId")
    Page<Photo> findByProfileId(@Param("profileId") Long profileId, Pageable pageable);


    /**
     * 요청된 profileIds 리스트에 있는 모든 프로필이 등장하는 Photo를 페이징 조회합니다.
     * (사진에 다른 프로필이 더 있어도 OK, profileIds 내 모든 ID는 반드시 포함되어야 함)
     */
    @Query(
            value = """
        select p
        from Photo p
        join p.photoProfileMapList m
        where m.profile.id in :profileIds
        group by p.id
        having count(distinct m.profile.id) = :#{#profileIds.size()}
      """,
            countQuery = """
        select count(distinct p.id)
        from Photo p
        join p.photoProfileMapList m
        where m.profile.id in :profileIds
        group by p.id
        having count(distinct m.profile.id) = :#{#profileIds.size()}
      """
    )
    Page<Photo> findByAllProfileIds(
            @Param("profileIds") List<Long> profileIds,
            Pageable pageable
    );
}
