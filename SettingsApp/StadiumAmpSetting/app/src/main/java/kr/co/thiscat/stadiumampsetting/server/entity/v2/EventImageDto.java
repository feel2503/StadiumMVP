package kr.co.thiscat.stadiumampsetting.server.entity.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventImageDto
{
    @Expose
    @SerializedName("imageId")
    private long imageId;
    @Expose
    @SerializedName("eventId")
    private long eventId;
    @Expose
    @SerializedName("imageType")
    private String imageType;
    @Expose
    @SerializedName("imageName")
    private String imageName;
    @Expose
    @SerializedName("imageUrl")
    private String imageUrl;


    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
