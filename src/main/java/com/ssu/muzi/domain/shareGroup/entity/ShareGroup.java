package com.ssu.muzi.domain.shareGroup.entity;

import com.ssu.muzi.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "share_group")
@SQLRestriction("deleted_at is NULL")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShareGroup extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "share_group_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "progress_percent")
    private ProgressPercent progressPercent; //팀 여행 진행 정도
    @Column(name = "group_color")
    private String groupColor;
    @Column(name = "group_image_url")
    private String groupImageUrl;
    @Column(name = "group_name", nullable = false)
    private String groupName;
    @Column(name = "description")
    private String description;
    @Column(name = "place", nullable = false)
    private String place;
    @Column(name = "started_at")
    private LocalDateTime startedAt; // 일정 시작
    @Column(name = "ended_at")
    private LocalDateTime endedAt; // 일정 끝
    @Column(name = "owner_name")
    private String ownerName;
    @Column(name = "owner_image_url")
    private String ownerImageUrl;


    @OneToMany(mappedBy = "shareGroup")
    @Builder.Default
    private List<Profile> profileList = new ArrayList<>();


    public void addProfile(Profile profile) {
        if (this.profileList == null) {
            this.profileList = new ArrayList<>();
        }
        this.profileList.add(profile);
    }

    public void delete() {
        for (Profile profile : profileList) {
            profile.delete();
        }
        super.delete();
    }
}
