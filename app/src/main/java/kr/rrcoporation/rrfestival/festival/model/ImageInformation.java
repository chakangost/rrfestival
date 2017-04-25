package kr.rrcoporation.rrfestival.festival.model;

import com.google.gson.annotations.SerializedName;

public class ImageInformation {

    @SerializedName("contentid")
    private int contentId;
    @SerializedName("originimgurl")
    private String originalImageUrl;
    @SerializedName("smallimageurl")
    private String thumbnailUrl;

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getOriginalImageUrl() {
        return originalImageUrl;
    }

    public void setOriginalImageUrl(String originalImageUrl) {
        this.originalImageUrl = originalImageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @SerializedName("serialnum")
    private String serialNumber;
}
