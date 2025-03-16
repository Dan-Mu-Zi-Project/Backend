package com.ssu.muzi.domain.shareGroup.service;

import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.shareGroup.entity.Profile;
import com.ssu.muzi.domain.shareGroup.repository.ProfileRepository;
import com.ssu.muzi.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssu.muzi.global.error.code.ProfileErrorCode.PROFILE_NOT_FOUND;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    public Profile findProfile(Long memberId, Long shareGroupId) {
        return profileRepository.findByMemberIdAndShareGroupId(memberId, shareGroupId)
                .orElseThrow(() -> new BusinessException(PROFILE_NOT_FOUND));
    }

    @Override
    public Profile findProfile(Long profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new BusinessException(PROFILE_NOT_FOUND));
    }
}
