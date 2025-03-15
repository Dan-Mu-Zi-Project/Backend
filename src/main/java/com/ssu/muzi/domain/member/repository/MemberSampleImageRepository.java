package com.ssu.muzi.domain.member.repository;

import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.member.entity.MemberSampleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberSampleImageRepository extends JpaRepository<MemberSampleImage, Long> {
}
