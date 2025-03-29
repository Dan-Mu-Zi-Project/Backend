package com.ssu.muzi.domain.shareGroup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssu.muzi.domain.member.dto.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public abstract class ShareGroupResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvitationInfo {
        private Long shareGroupId;
        private String groupName;
        private String description;
        private String place;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime startedAt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime endedAt;
        private String ownerName;
        private String ownerImageUrl;
        private List<ProfileResponse.ParticipantInfo> participantInfoList;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinInfo {
        private Long shareGroupId;
        private Long profileId;
        private LocalDateTime joinedAt;
    }

    // 그룹의 임베딩벡터 응답 반환하는 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShareGroupVector {
        private Long shareGroupId;
        private List<MemberResponse.MemberEmbedding> memberEmbeddingList;
    }

    // 그룹 상세정보 반환
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShareGroupDetailInfo {
        private Long shareGroupId;
        private String groupName;
        private String description;
        private String groupImage;
        private String place;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime startedAt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime endedAt;
        private List<ProfileResponse.ParticipantInfo> participantInfoList;
    }

    // 홈 화면 정보
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Home {
        private String name;               // 로그인한 사용자의 이름
        private List<HomeDetail> homeDetailList;     // 참여한 그룹 리스트
    }

    // 홈 화면에 있는 각 그룹 정보를 보여줌
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HomeDetail {
        private Long shareGroupId;
        private String status; // GroupStatus enum의 문자열 값
        private String groupName;
        private String place;
        private LocalDateTime startedAt;
        private LocalDateTime endedAt;
    }

    // 내가 속한 전체 공유그룹 페이징 조회
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagedShareGroupInfo {
        private List<ShareGroupPreviewInfo> shareGroupInfoList; //공유그룹 상세 정보 리스트
        private int page; // 페이지 번호
        private long totalElements; // 해당 조건에 부합하는 요소의 총 개수
        private boolean isFirst; // 첫 페이지 여부
        private boolean isLast; // 마지막 페이지 여부
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShareGroupPreviewInfo {
        private Long shareGroupId;
        private String groupColor;
        private String status;
        private String groupName;
        private String description;
        private LocalDateTime startedAt;
        private LocalDateTime endedAt;
        private int downloadCount;
        private int entireCount;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShareGroupId {
        private Long shareGroupId;
    }
}
