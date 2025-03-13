package com.ssu.muzi.domain.shareGroup.converter;

import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.shareGroup.entity.Profile;
import com.ssu.muzi.domain.shareGroup.entity.Role;
import com.ssu.muzi.domain.shareGroup.entity.ShareGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ProfileConverter {

    // 요청받은 profile을 entity로 생성
    public Profile toProfileEntity(Member member, ShareGroup shareGroup) {
        return Profile.builder()
                .shareGroup(shareGroup)                    // 해당 그룹
                .member(member)                            // 멤버에 본인 설정
                .joinedAt(LocalDateTime.now())             // 그룹에 참가한 시간
                .build();
    }
}
