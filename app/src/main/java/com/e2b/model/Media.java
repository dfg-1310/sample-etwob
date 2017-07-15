package com.e2b.model;

import com.e2b.model.response.BaseResponse;

import java.io.Serializable;

/**
 * Class is used to contain media info.
 */
public class Media extends BaseResponse implements Serializable {

    public int mediaType;
    public String imgUrl;
    public String videoUrl;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
