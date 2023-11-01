package kr.co.thiscat.stadiumampsetting.server.entity.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EventStartReqDto
{
    @Expose
    @SerializedName("eventId")
    private int eventId;

    @Expose
    @SerializedName("triggerType")
    int triggerType;
    @Expose
    @SerializedName("triggerTime")
    int triggerTime;
    @Expose
    @SerializedName("triggerVote")
    int triggerVote;

    @Expose
    @SerializedName("continuityType")
    int continuityType;
    @Expose
    @SerializedName("continuityTime")
    int continuityTime;

    public EventStartReqDto(int eventId, int triggerType, int triggerTime, int triggerVote, int continuityType, int continuityTime) {
        this.eventId = eventId;
        this.triggerType = triggerType;
        this.triggerTime = triggerTime;
        this.triggerVote = triggerVote;
        this.continuityType = continuityType;
        this.continuityTime = continuityTime;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(int triggerType) {
        this.triggerType = triggerType;
    }

    public int getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(int triggerTime) {
        this.triggerTime = triggerTime;
    }

    public int getTriggerVote() {
        return triggerVote;
    }

    public void setTriggerVote(int triggerVote) {
        this.triggerVote = triggerVote;
    }

    public int getContinuityType() {
        return continuityType;
    }

    public void setContinuityType(int continuityType) {
        this.continuityType = continuityType;
    }

    public int getContinuityTime() {
        return continuityTime;
    }

    public void setContinuityTime(int continuityTime) {
        this.continuityTime = continuityTime;
    }


}
