package com.ssu.muzi.domain.photo.repository;

import com.ssu.muzi.domain.photo.entity.Photo;
import com.ssu.muzi.domain.photo.entity.PhotoDownloadLog;
import com.ssu.muzi.domain.shareGroup.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoDownloadLogRepository extends JpaRepository<PhotoDownloadLog, Long> {
    // 해당 Profile과, 주어진 photo id 목록에 해당하는 다운로드 레코드 수 계산
    long countByProfileAndPhotoIdIn(Profile profile, List<Long> photoIds);
    boolean existsByProfileAndPhoto(Profile profile, Photo photo);
}
