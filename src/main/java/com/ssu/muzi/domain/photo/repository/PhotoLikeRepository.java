package com.ssu.muzi.domain.photo.repository;

import com.ssu.muzi.domain.photo.entity.Photo;
import com.ssu.muzi.domain.photo.entity.PhotoLike;
import com.ssu.muzi.domain.shareGroup.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoLikeRepository extends JpaRepository<PhotoLike, Long> {
    Optional<PhotoLike> findByProfileAndPhoto(Profile profile, Photo photo);
}
