package kr.co.thiscat.stadiumampsetting.server.entity.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventcontinuityTypeDto {
    @Expose
    @SerializedName("eventId")
    private long eventId;

    @Expose
    @SerializedName("continuityType")
    private int continuityType;

    public EventcontinuityTypeDto(long eventId, int continuityType) {
        this.eventId = eventId;
        this.continuityType = continuityType;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public int getContinuityType() {
        return continuityType;
    }

    public void setContinuityType(int continuityType) {
        this.continuityType = continuityType;
    }
}
