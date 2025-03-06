package com.ssu.muzi.domain.member.entity;

import com.ssu.muzi.domain.shareGroup.entity.Profile;
import com.ssu.muzi.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@SQLRestriction("deleted_at is NULL")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(nullable = false)
    private Long authId;
    @Column(nullable = false)
    private String name;
    @Column
    private String memberImageUrl;
    @Column
    private Boolean isFaceCaptured; // 샘플 사진 있는지 여부
    @Column
    private Boolean onlyWifi; // '와이파이에서만 다운로드' 버튼
    @Column
    private String refreshToken;     // Refresh Token
    @Column
    private String accessToken;      // Access Token
    @Column
    private String email;      // Access Token

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Profile> profileList = new ArrayList<>();

//    public void delete() {
//        super.delete();
//    }

}