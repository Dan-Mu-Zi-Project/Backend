package com.ssu.muzi.domain.shareGroup.service;

import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.member.repository.MemberRepository;
import com.ssu.muzi.domain.shareGroup.converter.ProfileConverter;
import com.ssu.muzi.domain.shareGroup.converter.ShareGroupConverter;
import com.ssu.muzi.domain.shareGroup.dto.ProfileResponse;
import com.ssu.muzi.domain.shareGroup.dto.ShareGroupRequest;
import com.ssu.muzi.domain.shareGroup.entity.Profile;
import com.ssu.muzi.domain.shareGroup.entity.Role;
import com.ssu.muzi.domain.shareGroup.entity.ShareGroup;
import com.ssu.muzi.domain.shareGroup.repository.ProfileRepository;
import com.ssu.muzi.domain.shareGroup.repository.ShareGroupRepository;
import com.ssu.muzi.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.ssu.muzi.global.error.code.JwtErrorCode.MEMBER_NOT_FOUND;
import static com.ssu.muzi.global.error.code.ShareGroupErrorCode.ALREADY_EXISTS_PROGRESSING_GROUP;
import static com.ssu.muzi.global.error.code.ShareGroupErrorCode.STARTEDAT_AFTER_ENDEDAT;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ShareGroupServiceImpl implements ShareGroupService {

    private final ShareGroupConverter shareGroupConverter;
    private final ShareGroupRepository shareGroupRepository;
    private final MemberRepository memberRepository;
    private final ProfileConverter profileConverter;
    private final ProfileRepository profileRepository;

    @Override
    public ShareGroup createShareGroup(ShareGroupRequest.CreateShareGroupRequest request,
                                       Member member) {

        // 1. memberId 존재 여부 검증
        if (!memberRepository.existsById(member.getId())) {
            throw new BusinessException(MEMBER_NOT_FOUND);
        }

        // 2. 시작 날짜가 끝 날짜보다 이후이면 에러
        if (request.getStartedAt().isAfter(request.getEndedAt())) {
            throw new BusinessException(STARTEDAT_AFTER_ENDEDAT);
        }

        // 3. 그룹 생성 요청받은 그룹을 엔티티로 변환
        ShareGroup newShareGroup = shareGroupConverter.toShareGroupEntity(request);

        // 4. 생성된 공유 그룹을 먼저 저장하여 공유그룹 id가 생성되게 함
        newShareGroup = shareGroupRepository.save(newShareGroup);

        // 5. Profile 테이블에 그룹 생성자(나)를 추가
        Profile profile = profileConverter.toProfileEntity(member, newShareGroup);
        profile.setRole(Role.CREATOR);

        // 6. Profile 저장
        profileRepository.save(profile);

        // 7. 레포지토리에 저장
        return shareGroupRepository.save(newShareGroup);
    }

    // 지정된 여행 기간에 이미 진행 중인 그룹이 있는지 확인하는 메소드
    private void validateTravelPeriod(LocalDateTime startedAt, LocalDateTime endedAt) {
        if (shareGroupRepository.existsProgressingGroup(startedAt, endedAt)) {
            throw new BusinessException(ALREADY_EXISTS_PROGRESSING_GROUP);
        }
    }

}
