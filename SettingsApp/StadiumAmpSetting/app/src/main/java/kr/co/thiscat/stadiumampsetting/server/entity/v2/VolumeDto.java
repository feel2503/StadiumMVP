package kr.co.thiscat.stadiumampsetting.server.entity.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VolumeDto {
    @Expose
    @SerializedName("eventId")
    long eventId;

    @Expose
    @SerializedName("value")
    int value;

    public VolumeDto(long eventId, int value) {
        this.eventId = eventId;
        this.value = value;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
