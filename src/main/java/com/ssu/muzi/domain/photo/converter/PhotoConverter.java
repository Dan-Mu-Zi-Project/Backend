package com.ssu.muzi.domain.photo.converter;

import com.ssu.muzi.domain.photo.dto.PhotoResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhotoConverter {

    //presigned url 리스트
    public PhotoResponse.PreSignedUrlList toPreSignedUrlList(List<PhotoResponse.PreSignedUrl> preSignedUrlList) {
        List<PhotoResponse.PreSignedUrl> preSignedUrlInfoList = preSignedUrlList.stream()
                .map(preSignedUrlInfo -> toPreSignedUrl(
                        preSignedUrlInfo.getPreSignedUrl(),
                        preSignedUrlInfo.getPhotoUrl(),
                        preSignedUrlInfo.getPhotoName()
                ))
                .collect(Collectors.toList());

        return PhotoResponse.PreSignedUrlList
                .builder()
                .preSignedUrlInfoList(preSignedUrlInfoList)
                .build();
    }

    public PhotoResponse.PreSignedUrl toPreSignedUrl(String preSignedUrl, String photoUrl, String photoName) {
        return PhotoResponse.PreSignedUrl
                .builder()
                .preSignedUrl(preSignedUrl)
                .photoUrl(photoUrl)
                .photoName(photoName)
                .build();
    }
}
