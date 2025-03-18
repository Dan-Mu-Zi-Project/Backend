package com.ssu.muzi.domain.shareGroup.service;

import com.ssu.muzi.domain.member.dto.MemberResponse;
import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.member.entity.MemberSampleImage;
import com.ssu.muzi.domain.member.repository.MemberRepository;
import com.ssu.muzi.domain.member.repository.MemberSampleImageRepository;
import com.ssu.muzi.domain.photo.entity.PhotoProfileMap;
import com.ssu.muzi.domain.photo.repository.PhotoDownloadLogRepository;
import com.ssu.muzi.domain.photo.repository.PhotoProfileMapRepository;
import com.ssu.muzi.domain.shareGroup.converter.ProfileConverter;
import com.ssu.muzi.domain.shareGroup.converter.ShareGroupConverter;
import com.ssu.muzi.domain.shareGroup.dto.ShareGroupRequest;
import com.ssu.muzi.domain.shareGroup.dto.ShareGroupResponse;
import com.ssu.muzi.domain.shareGroup.entity.Profile;
import com.ssu.muzi.domain.shareGroup.entity.Role;
import com.ssu.muzi.domain.shareGroup.entity.ShareGroup;
import com.ssu.muzi.domain.shareGroup.entity.Status;
import com.ssu.muzi.domain.shareGroup.repository.ProfileRepository;
import com.ssu.muzi.domain.shareGroup.repository.ShareGroupRepository;
import com.ssu.muzi.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ssu.muzi.global.error.code.JwtErrorCode.MEMBER_NOT_FOUND;
import static com.ssu.muzi.global.error.code.ShareGroupErrorCode.ALREADY_EXISTS_PROGRESSING_GROUP;
import static com.ssu.muzi.global.error.code.ShareGroupErrorCode.ALREADY_JOINED_GROUP;
import static com.ssu.muzi.global.error.code.ShareGroupErrorCode.ALREADY_STARTED_TRAVEL;
import static com.ssu.muzi.global.error.code.ShareGroupErrorCode.ALREADY_STARTED_TRAVEL_NOT_JOIN;
import static com.ssu.muzi.global.error.code.ShareGroupErrorCode.INVALID_DATE_MODIFICATION;
import static com.ssu.muzi.global.error.code.ShareGroupErrorCode.SHARE_GROUP_NOT_FOUND;
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
    private final MemberSampleImageRepository memberSampleImageRepository;
    private final ProfileService profileService;
    private final PhotoProfileMapRepository photoProfileMapRepository;
    private final PhotoDownloadLogRepository photoDownloadLogRepository;

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

        // 4. 현재 날짜 이전으로 시작/종료 날짜 생성 불가
        LocalDateTime now = LocalDateTime.now();
        if (request.getStartedAt().isBefore(now) || request.getEndedAt().isBefore(now)) {
            throw new BusinessException(INVALID_DATE_MODIFICATION);
        }

        // 3. 그룹 생성시, 여행기간이 겹치는 그룹이 이미 존재하는지 검증
        validateTravelPeriodForMember(request.getStartedAt(), request.getEndedAt(), member);

        // 4. 그룹 생성 요청받은 그룹을 엔티티로 변환
        ShareGroup newShareGroup = shareGroupConverter.toShareGroupEntity(request, member);

        // 5. 생성된 공유 그룹을 먼저 저장하여 공유그룹 id가 생성되게 함
        newShareGroup = shareGroupRepository.save(newShareGroup);

        // 6. Profile 테이블에 그룹 생성자(나)를 추가
        Profile profile = profileConverter.toProfileEntity(member, newShareGroup);
        profile.setRole(Role.CREATOR);

        // 7. Profile 저장
        profileRepository.save(profile);

        // 8. 그룹 정보에 내 프로필 추가
        newShareGroup.addProfile(profile);

        // 9. 그룹을 레포지토리에 저장
        return shareGroupRepository.save(newShareGroup);
    }

    @Override
    public ShareGroup updateShareGroup(ShareGroupRequest.UpdateShareGroupRequest request, Long shareGroupId, Member member) {

        // 0. 해당 id의 그룹이 있는지 검증
        ShareGroup shareGroup = findShareGroup(shareGroupId);

        // 1. 시작 날짜가 끝 날짜보다 이후이면 에러
        if (request.getStartedAt().isAfter(request.getEndedAt())) {
            throw new BusinessException(STARTEDAT_AFTER_ENDEDAT);
        }

        // 2. 여행일정 수정 시, 여행기간이 겹치는 그룹이 이미 존재하는지 검증 (현재 수정하려는 그룹 제외)
        validateTravelPeriodForMember(request.getStartedAt(), request.getEndedAt(), member, shareGroupId);

        // 3. 이미 여행 시작 날짜가 지났다면 업데이트 불가
        LocalDateTime now = LocalDateTime.now();
        if (!now.isBefore(shareGroup.getStartedAt())) {
            throw new BusinessException(ALREADY_STARTED_TRAVEL);
        }

        // 4. 현재 날짜 이전으로 시작/종료 날짜 업데이트 불가
        if (request.getStartedAt().isBefore(now) || request.getEndedAt().isBefore(now)) {
            throw new BusinessException(INVALID_DATE_MODIFICATION);
        }

        // 6. 컨버터를 사용해서 그룹 엔티티의 필드를 요청값으로 업데이트
        shareGroup = shareGroupConverter.updateShareGroupEntity(shareGroup, request);

        // 7. 변경된 엔티티 저장 후 반환
        return shareGroupRepository.save(shareGroup);
    }

    @Override
    public Profile joinShareGroup(Long shareGroupId, Member member) {

        // 1. 해당 공유 그룹 조회
        ShareGroup shareGroup = findShareGroup(shareGroupId);

        // 2. 먼저, 이미 그룹에 참여 중인 멤버인지 확인 (중복 가입 방지)
        if (doesProfileExist(shareGroupId, member.getId())) {
            throw new BusinessException(ALREADY_JOINED_GROUP);
        }

        // 3. 여행 날짜가 이미 지나버린 경우 참여 불가
        LocalDateTime now = LocalDateTime.now();
        if (!now.isBefore(shareGroup.getStartedAt())) {
            throw new BusinessException(ALREADY_STARTED_TRAVEL_NOT_JOIN);
        }

        // 4. 여행일정 수정 시, 내 그룹에 여행기간이 겹치는 그룹이 이미 존재하는지 검증
        validateTravelPeriodForMember(shareGroup.getStartedAt(), shareGroup.getEndedAt(), member);

        // 5. 가입하지 않은 사용자라면, 새로운 Profile 생성 - 참여자 역할로
        Profile profile = profileConverter.toProfileEntity(member, shareGroup);
        profile.setRole(Role.PARTICIPANT);

        // 6. Profile 저장
        profileRepository.save(profile);

        return profile;  // 새로 생성된 프로필 반환
    }

    // 주어진 shareGroupId에 해당하는 공유 그룹의 회원 임베딩(샘플 이미지 벡터) 정보를 조회
    @Override
    public ShareGroupResponse.ShareGroupVector getShareGroupVectorList(Long shareGroupId) {

        // 1. shareGroupId로 공유 그룹 조회
        ShareGroup shareGroup = findShareGroup(shareGroupId);

        // 2. 해당 그룹에 참여한 회원 리스트 조회
        List<Profile> profileList = shareGroup.getProfileList();

        // 3. 각 Profile마다, 연결된 Member의 샘플 이미지(벡터) 정보를 조회하여 DTO 변환
        List<MemberResponse.MemberEmbedding> memberEmbeddingList =
                profileList.stream()
                .map(profile -> {
                    List<MemberSampleImage> sampleImageList = memberSampleImageRepository.findByMember(profile.getMember());
                    return shareGroupConverter.toMemberEmbedding(profile, sampleImageList);
        }).collect(Collectors.toList());

        // 4. 최종 응답 DTO 생성
        return shareGroupConverter.toShareGroupVector(shareGroup, memberEmbeddingList);
    }

    // 홈 화면 반환
    @Override
    public ShareGroupResponse.Home getHomeGroups(Member member) {

        // 현재 시간 기준
        LocalDateTime now = LocalDateTime.now();

        // Member가 참여한 그룹들을 조회 (Profile, ShareGroup 관계를 통해)
        List<ShareGroup> groupList = shareGroupRepository.findByMemberId(member.getId());

        // 각 그룹의 상태를 서비스에서 계산한 후, 조건에 맞는 그룹만 DTO로 변환
        List<ShareGroupResponse.HomeDetail> homeDetailList = groupList
                .stream()
                .map(group -> {
                    Status status = computeGroupStatus(group, now);
                    return status != null ? shareGroupConverter.toHomeDetail(group, status) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return ShareGroupResponse.Home
                .builder()
                .name(member.getName())  // 로그인한 사용자의 이름
                .homeDetailList(homeDetailList)
                .build();
    }

    // 내가 속한 전체 그룹의 리스트를 페이징 처리해서 반환 (그룹 프리뷰)
    @Override
    public Page<ShareGroupResponse.ShareGroupPreviewInfo> getMyShareGroupList(Member member, Pageable pageable) {

        // 1. 해당 멤버의 논리 삭제되지 않은 모든 프로필에서 참여 중인 공유 그룹 ID 추출
        List<Long> shareGroupIdList = profileRepository.findByMemberId(member.getId())
                .stream()
                .map(profile -> profile.getShareGroup().getId())
                .distinct()
                .collect(Collectors.toList());

        // 2. 공유 그룹 ID 목록으로 페이징된 ShareGroup 조회
        Page<ShareGroup> groupPageIds = shareGroupRepository.findByIdIn(shareGroupIdList, pageable);
        LocalDateTime now = LocalDateTime.now();

        // 3. 각 그룹마다, 내가 나온 내 앨범을 기준으로 해서, 전체 사진 수, 다운로드 수, 상태를 계산해서, 컨버터를 통해 응답으로 반환
        List<ShareGroupResponse.ShareGroupPreviewInfo> groupPreviewList =
                groupPageIds
                        .getContent()
                        .stream()
                        .map(group -> {

                // 해당 그룹에서 로그인한 사용자의 Profile id를 조회
                Profile profile = profileService.findProfile(member.getId(), group.getId());

                // 전체 사진 수: 해당 Profile에 매핑된 PhotoProfileMap 엔티티 수
                List<PhotoProfileMap> albumPhotos = photoProfileMapRepository.findByProfile(profile);
                long entireCount = albumPhotos.size();

                // 다운로드 수: 위에서 계산한, albumPhotos의 photo id 목록을 기반으로, PhotoDownloadLog에서 내 Profile의 다운로드 수 집계
                // a. 위에서 계산한 albumPhotos의 photo id를 집계
                List<Long> albumPhotoIds = albumPhotos
                        .stream()
                        .map(ppm -> ppm.getPhoto().getId())
                        .collect(Collectors.toList());
                // b. 다운로드 수 집계
                long downloadCount = albumPhotoIds.isEmpty() ? 0 :
                        photoDownloadLogRepository.countByProfileAndPhotoIdIn(profile, albumPhotoIds);

                // 그룹 상태 계산
                Status status = computeGroupStatus(group, now);

                return shareGroupConverter.toShareGroupPreview(group, status, downloadCount, entireCount);
        }).collect(Collectors.toList());

        // 4. PageImpl로 새로운 페이징된 그룹 리스트 생성
        return new PageImpl<>(groupPreviewList, pageable, groupPageIds.getTotalElements());
    }


    // 현재 시간을 기준으로 그룹의 상태를 계산 (홈 화면 조회시)
    private Status computeGroupStatus(ShareGroup group, LocalDateTime now) {
        if (now.isBefore(group.getStartedAt())) {
            return Status.BEFORE_START;
        } else if (!now.isAfter(group.getEndedAt())) { // now <= endedAt
            return Status.IN_PROGRESS;
        } else if (now.isBefore(group.getEndedAt().plusDays(7))) {
            return Status.RECENTLY_ENDED;
        } else {
            return null; // 홈 화면에 표시하지 않을 그룹
        }
    }

    // 그룹 이미지 업로드
    public ShareGroupResponse.ShareGroupId updateGroupImage(Long shareGroupId, ShareGroupRequest.GroupImageUploadRequest request) {

        ShareGroup shareGroup = findShareGroup(shareGroupId);

        // 그룹의 이미지 URL 업데이트
        shareGroup.setGroupImageUrl(request.getGroupImageUrl());
        shareGroupRepository.save(shareGroup);

        // shareGroupId를 응답으로 반환
        return shareGroupConverter.toShareGroupId(shareGroup);
    }

    // 그룹 생성시!! 기간이 겹치는 그룹이 이미 존재하는지 확인하는 메소드 - 내가 참여한 그룹 안에서만 안겹치면 됨
    private void validateTravelPeriodForMember(LocalDateTime startedAt, LocalDateTime endedAt, Member member) {
        // 내가 참여한 그룹 목록을 조회
        List<ShareGroup> myGroups = shareGroupRepository.findByMemberId(member.getId());

        // 각 그룹의 여행 기간과 요청된 기간이 겹치는지 체크
        for (ShareGroup group : myGroups) {
            if (group.getStartedAt().isBefore(endedAt) && group.getEndedAt().isAfter(startedAt)) {
                throw new BusinessException(ALREADY_EXISTS_PROGRESSING_GROUP);
            }
        }
    }

    // 그룹 수정시!! 기간이 겹치는 그룹이 이미 존재하는지 확인하는 메소드 - 현재 수정하려는 그룹은 제외하고 확인
    private void validateTravelPeriodForMember(LocalDateTime startedAt, LocalDateTime endedAt, Member member, Long excludeGroupId) {
        // 내가 참여한 그룹 목록 조회
        List<ShareGroup> myGroups = shareGroupRepository.findByMemberId(member.getId());
        for (ShareGroup group : myGroups) {
            // 수정 중인 그룹은 건너뜁니다.
            if (group.getId().equals(excludeGroupId)) {
                continue;
            }
            // 여행 기간이 겹치는지 확인 (겹치면 예외 발생)
            if (group.getStartedAt().isBefore(endedAt) && group.getEndedAt().isAfter(startedAt)) {
                throw new BusinessException(ALREADY_EXISTS_PROGRESSING_GROUP);
            }
        }
    }

    @Override
    public ShareGroup findShareGroup(Long shareGroupId) {
        return shareGroupRepository.findById(shareGroupId)
                .orElseThrow(() -> new BusinessException(SHARE_GROUP_NOT_FOUND));
    }

    private boolean doesProfileExist(Long shareGroupId, Long memberId) {
        return profileRepository.existsByShareGroupIdAndMemberId(shareGroupId, memberId);
    }
}
