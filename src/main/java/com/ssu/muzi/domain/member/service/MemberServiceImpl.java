package com.ssu.muzi.domain.member.service;

import com.ssu.muzi.domain.member.converter.MemberConverter;
import com.ssu.muzi.domain.member.dto.MemberRequest;
import com.ssu.muzi.domain.member.dto.MemberResponse;
import com.ssu.muzi.domain.member.entity.AngleType;
import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.member.entity.MemberSampleImage;
import com.ssu.muzi.domain.member.repository.MemberRepository;
import com.ssu.muzi.domain.member.repository.MemberSampleImageRepository;
import com.ssu.muzi.domain.photo.entity.Photo;
import com.ssu.muzi.domain.photo.entity.PhotoProfileMap;
import com.ssu.muzi.domain.photo.repository.PhotoProfileMapRepository;
import com.ssu.muzi.domain.photo.repository.PhotoRepository;
import com.ssu.muzi.domain.shareGroup.entity.Profile;
import com.ssu.muzi.domain.shareGroup.entity.ShareGroup;
import com.ssu.muzi.domain.shareGroup.repository.ProfileRepository;
import com.ssu.muzi.domain.shareGroup.repository.ShareGroupRepository;
import com.ssu.muzi.domain.shareGroup.service.ProfileService;
import com.ssu.muzi.domain.shareGroup.service.ShareGroupService;
import com.ssu.muzi.global.error.BusinessException;
import com.ssu.muzi.global.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ssu.muzi.global.error.code.MemberErrorCode.INVALID_SAMPLE_IMAGE_COUNT;
import static com.ssu.muzi.global.error.code.MemberErrorCode.MEMBER_IMAGE_BLANK;
import static com.ssu.muzi.global.error.code.MemberErrorCode.MEMBER_NAME_BLANK;
import static com.ssu.muzi.global.error.code.MemberErrorCode.MEMBER_NOT_FOUND_BY_MEMBER_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final MemberSampleImageRepository memberSampleImageRepository;
    private final ProfileRepository profileRepository;
    private final ShareGroupService shareGroupService;
    private final PhotoProfileMapRepository photoProfileMapRepository;
    private final PhotoRepository photoRepository;

    //회원 정보 조회
    @Override
    public MemberResponse.MemberInfo getMyInfo() {
        // SecurityContext에서 현재 로그인된 사용자의 멤버 ID를 추출
        final long memberId = SecurityUtil.getCurrentUserId();

        //memberId를 사용하여 데이터베이스에서 해당 사용자의 정보를 조회
        Member member = findMemberByMemberId(memberId);
        //컨버터로 변환
        return memberConverter.toMemberInfo(member);
    }

    //회원 정보 수정
    @Override
    @Transactional
    public MemberResponse.MemberId setNickName(Member member, String name) {

        // 이름 필드가 비어 있다면 오류
        if (name == null || name.isBlank()) {
            throw new BusinessException(MEMBER_NAME_BLANK);
        }

        // 닉네임을 설정하고 db에 저장
        member.setName(name);
        memberRepository.save(member);

        return memberConverter.toMemberId(member);
    }

    //회원 정보 수정 - 이미지
    @Override
    @Transactional
    public MemberResponse.MemberId setMemberImageUrl(Member member, String memberImageUrl) {

        // 이미지 필드가 비어 있다면 오류
        if (memberImageUrl == null || memberImageUrl.isBlank()) {
            throw new BusinessException(MEMBER_IMAGE_BLANK);
        }

        // 사진을 설정하고 db에 저장
        member.setMemberImageUrl(memberImageUrl);
        memberRepository.save(member);

        return memberConverter.toMemberId(member);
    }

    //회원 정보 수정 - 와이파이 여부
    @Override
    @Transactional
    public MemberResponse.MemberId setWifi(Member member, Boolean onlyWifi) {
        member.setOnlyWifi(onlyWifi);
        memberRepository.save(member);

        return memberConverter.toMemberId(member);
    }

    //샘플 이미지 저장
    @Override
    @Transactional
    public MemberResponse.MemberId saveSampleImages(Member member, MemberRequest.SampleImageList request) {

        // 1. 저장된 샘플 이미지 개수가 3개 미만이면 예외 처리
        if (request.getFaceSampleList().size() < 3) {
            throw new BusinessException(INVALID_SAMPLE_IMAGE_COUNT);
        }

        // 2. 기존 회원의 샘플 이미지 모두 삭제
        memberSampleImageRepository.deleteByMember(member);

        // 3. 요청 DTO List를 하나씩 엔티티 목록으로 변환 -> 마지막에 리스트로
        List<MemberSampleImage> images = request.getFaceSampleList()
                .stream()
                .map(dto -> {
                    // AngleType은 enum으로 가정하고, 대문자로 변환 후 사용
                    AngleType angle = AngleType.valueOf(dto.getAngleType().toUpperCase());

                    return MemberSampleImage.builder()
                            .angleType(angle)
                            .faceVector(dto.getFaceVector())
                            .member(member)
                            .build();
                }).toList();

        // 4. 변환된 엔티티들을 저장
        memberSampleImageRepository.saveAll(images);

        // 5. 회원의 isFaceCaptured 필드를 true로 업데이트
        member.setIsFaceCaptured(true);
        memberRepository.save(member);

        return memberConverter.toMemberId(member);
    }

    // 회원 탈퇴
    @Override
    @Transactional
    public MemberResponse.MemberId deleteMember(Member member) {
        // 1. 회원이 참여한 모든 활성 Profile 목록을 조회하고, detached 상태의 복사본을 만든다.
        List<Profile> myActiveProfiles = new ArrayList<>(profileRepository.findByMemberId(member.getId()));

        // 2. 각 프로필(즉, 각 그룹)에 대해 처리
        for (Profile profile : myActiveProfiles) {
            Long shareGroupId = profile.getShareGroup().getId();
            long activeProfileCount = profileRepository.countActiveProfilesByShareGroupId(shareGroupId);

            if (activeProfileCount > 1) { // 그룹 내에 내 프로필 외에 다른 회원이 존재할 때 -> 그룹 탈퇴 처리

                // 3. 삭제 전, 내 프로필에 연결된 PhotoProfileMap 목록을 미리 조회
                List<PhotoProfileMap> myMappings = photoProfileMapRepository.findByProfile(profile);

                // 4. 내 프로필 soft delete 처리
                profile.delete();
                profileRepository.save(profile);

                // 5. 각 매핑에 대해, 최신 활성 매핑 수를 재조회
                for (PhotoProfileMap mapping : myMappings) {
                    Photo photo = mapping.getPhoto();
                    // 최신 상태로 활성 매핑 수 조회: 삭제되지 않은(즉, deletedAt == null) 매핑 수
                    long activeCount = photoProfileMapRepository.countByPhotoAndProfileDeletedAtIsNull(photo);
                    // 만약 내 프로필 외에 활성 매핑이 없으면 (activeCount == 0), 해당 Photo도 soft delete 처리
                    if (activeCount == 0) {
                        photo.delete();
                        photoRepository.save(photo);
                    }
                }
            } else {
                // 그룹 내 활성 프로필이 내 프로필만 남은 경우 -> 그룹 삭제 처리
                shareGroupService.deleteShareGroup(shareGroupId, member);
            }
        }

        // 6. 모든 그룹 처리가 끝난 후, 회원 자체 soft delete 처리
        member.delete();
        memberRepository.save(member);

        // 7. 최종 응답 DTO 생성 (회원 ID는 삭제 후에도 유지됨)
        return memberConverter.toMemberId(member);

    }

    public Member findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND_BY_MEMBER_ID));
    }

}
