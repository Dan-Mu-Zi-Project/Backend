package com.ssu.muzi.domain.member.service;

import com.ssu.muzi.domain.member.converter.MemberConverter;
import com.ssu.muzi.domain.member.dto.MemberRequest;
import com.ssu.muzi.domain.member.dto.MemberResponse;
import com.ssu.muzi.domain.member.entity.AngleType;
import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.member.entity.MemberSampleImage;
import com.ssu.muzi.domain.member.repository.MemberRepository;
import com.ssu.muzi.domain.member.repository.MemberSampleImageRepository;
import com.ssu.muzi.global.error.BusinessException;
import com.ssu.muzi.global.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.ssu.muzi.global.error.code.MemberErrorCode.INVALID_SAMPLE_IMAGE_COUNT;
import static com.ssu.muzi.global.error.code.MemberErrorCode.MEMBER_IMAGE_BLANK;
import static com.ssu.muzi.global.error.code.MemberErrorCode.MEMBER_NAME_BLANK;
import static com.ssu.muzi.global.error.code.MemberErrorCode.MEMBER_NOT_FOUND_BY_MEMBER_ID;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final MemberSampleImageRepository memberSampleImageRepository;

    //회원 정보 조회
    @Override
    @Transactional(readOnly = true)
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
    public MemberResponse.MemberId setWifi(Member member, Boolean onlyWifi) {
        member.setOnlyWifi(onlyWifi);
        memberRepository.save(member);

        return memberConverter.toMemberId(member);
    }

    //샘플 이미지 저장
    @Override
    public MemberResponse.MemberId saveSampleImages(Member member, MemberRequest.SampleImageList request) {

        // 1. 저장된 샘플 이미지 개수가 3개 미만이면 예외 처리
        if (request.getFaceSampleList().size() < 3) {
            throw new BusinessException(INVALID_SAMPLE_IMAGE_COUNT);
        }

        // 2. 요청 DTO List를 하나씩 엔티티 목록으로 변환 -> 마지막에 리스트로
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

        // 3. 변환된 엔티티들을 저장
        memberSampleImageRepository.saveAll(images);

        // 4. 회원의 isFaceCaptured 필드를 true로 업데이트
        member.setIsFaceCaptured(true);
        memberRepository.save(member);

        return memberConverter.toMemberId(member);
    }

    //샘플 이미지 수정
    @Override
    public MemberResponse.MemberId updateSampleImages(Member member, MemberRequest.SampleImageList request) {

        // 1. 저장된 샘플 이미지 개수가 3개 미만이면 예외 처리
        if (request.getFaceSampleList().size() < 3) {
            throw new BusinessException(INVALID_SAMPLE_IMAGE_COUNT);
        }

        // 2. 기존 회원의 샘플 이미지 모두 삭제
        memberSampleImageRepository.deleteByMember(member);

        // 2. 요청 DTO List를 하나씩 엔티티 목록으로 변환 -> 마지막에 리스트로
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

        // 3. 변환된 엔티티들을 저장
        memberSampleImageRepository.saveAll(images);

        // 4. 회원의 isFaceCaptured 필드를 true로 업데이트
        member.setIsFaceCaptured(true);
        memberRepository.save(member);

        return memberConverter.toMemberId(member);
    }

    public Member findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND_BY_MEMBER_ID));
    }

}
