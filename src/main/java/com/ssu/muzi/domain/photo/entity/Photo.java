package com.ssu.muzi.domain.photo.entity;

import com.ssu.muzi.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "photo")
@SQLRestriction("deleted_at is NULL")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Photo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;
    @Column(name = "photo_url", nullable = false)
    private String photoUrl;
    @Column(name = "location")
    private String location;
    @Column(name = "take_at")
    private LocalDateTime takeAt; // 찍은 시간
    @Column(name = "width")
    private Integer width;
    @Column(name = "height")
    private Integer height;
    @Column(name = "uploader_profile_id", nullable = false)
    private Long uploaderProfileId;

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PhotoDownloadLog> photoDownloadLogList = new ArrayList<>();

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PhotoProfileMap> photoProfileMapList = new ArrayList<>();

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PhotoLike> photoLikeList = new ArrayList<>();

    public void delete() {
        //photo에 연결된 photoDownLog 삭제
        for (PhotoDownloadLog photoDownloadLog : photoDownloadLogList) {
            photoDownloadLog.delete();
        }
        //photo에 연결된 photoProfileMap 삭제
        for (PhotoProfileMap photoProfileMap : photoProfileMapList) {
            photoProfileMap.delete();
        }
        //photo에 연결된 photoLike 삭제
        for (PhotoLike photoLike : photoLikeList) {
            photoLike.delete();
        }
        super.delete();
    }
}
