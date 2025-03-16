package com.ssu.muzi.domain.shareGroup.service;

import com.ssu.muzi.domain.shareGroup.entity.Profile;

public interface ProfileService {
    Profile findProfile(Long profileId);
    Profile findProfile(Long memberId, Long shareGroupId);
}
