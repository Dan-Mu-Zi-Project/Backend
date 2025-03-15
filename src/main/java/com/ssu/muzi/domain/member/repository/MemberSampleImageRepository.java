package com.ssu.muzi.domain.member.repository;

import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.member.entity.MemberSampleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberSampleImageRepository extends JpaRepository<MemberSampleImage, Long> {
    @Modifying
    @Query("delete from MemberSampleImage msi where msi.member = :member")
    void deleteByMember(@Param("member") Member member);
}
