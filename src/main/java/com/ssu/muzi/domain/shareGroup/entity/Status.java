package com.ssu.muzi.domain.shareGroup.entity;

public enum Status {
    BEFORE_START,    // 아직 시작 전: now < startedAt
    IN_PROGRESS,     // 진행 중: startedAt <= now <= endedAt
    RECENTLY_ENDED   // 종료 후 7일 이내: endedAt < now < endedAt + 7일
    ;
}
