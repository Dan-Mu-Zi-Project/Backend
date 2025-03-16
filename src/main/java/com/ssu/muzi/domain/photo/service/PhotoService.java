package com.ssu.muzi.domain.photo.service;

import com.ssu.muzi.domain.member.entity.Member;
import com.ssu.muzi.domain.photo.dto.PhotoRequest;
import com.ssu.muzi.domain.photo.dto.PhotoResponse;

import java.util.List;

public interface PhotoService {
    List<PhotoResponse.PreSignedUrl> getPreSignedUrlList(PhotoRequest.PreSignedUrlRequest request, Member member);
    PhotoResponse.UploadPhotoList uploadPhotos(Long shareGroupId, Member uploader, PhotoRequest.PhotoUploadList request);
}
