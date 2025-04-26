package com.ssu.muzi.domain.photo.service;

import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.photo.dto.PhotoRequest;
import com.ssu.muzi.domain.photo.dto.PhotoResponse;
import com.ssu.muzi.domain.photo.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PhotoService {
    List<PhotoResponse.PreSignedUrl> getPreSignedUrlList(PhotoRequest.PreSignedUrlRequest request, Member member);
    PhotoResponse.UploadPhotoList uploadPhotos(Long shareGroupId, Member uploader, PhotoRequest.PhotoUploadList request);
    PhotoResponse.PhotoDownload recordDownload(Long shareGroupId, Member member, PhotoRequest.PhotoDownload request);
    PhotoResponse.PhotoId likePhoto(Long photoId, Long shareGroupId, Member member);
    PhotoResponse.PhotoId cancelLike(Long shareGroupId, Long photoId, Member member);
    PhotoResponse.PhotoDetailInfo getPhotoDetail(Long photoId);
    Page<PhotoResponse.PhotoPreviewInfo> getPhotoList(Member member, Long shareGroupId, Long albumProfileId, Pageable pageable);
    PhotoResponse.PhotoDeleteInfo deletePhotoList(PhotoRequest.PhotoDelete request);
    Page<PhotoResponse.PhotoPreviewInfo> getPhotoFilterList(Member member, Long shareGroupId, List<Long> profileIds, Pageable pageable);
    List<PhotoResponse.RandomPhotoPreviewInfo> getRandomPhotos(Long shareGroupId);
}
