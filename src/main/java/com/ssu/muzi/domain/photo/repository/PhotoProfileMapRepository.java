package com.ssu.muzi.domain.photo.repository;

import com.ssu.muzi.domain.photo.entity.PhotoProfileMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoProfileMapRepository extends JpaRepository<PhotoProfileMap, Long> {
}
