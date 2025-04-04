package com.ssu.muzi.domain.member.repository;

import com.ssu.muzi.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAuthId(Long authId);
    Optional<Member> findByRefreshToken(String refreshToken);
    Boolean existsByAuthId(Long authId);
    Optional<Member> findByLoginId(String loginId);
}
