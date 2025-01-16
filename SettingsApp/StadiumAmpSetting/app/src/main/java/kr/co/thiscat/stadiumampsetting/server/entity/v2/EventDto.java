package kr.co.thiscat.stadiumampsetting.server.entity.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EventDto
{
    @Expose
    @SerializedName("eventId")
    private int eventId;

    @Expose
    @SerializedName("eventName")
    String eventName;

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
    @SerializedName("webUrl")
    String webUrl;

    @Expose
    @SerializedName("continuityType")
    int continuityType;
    @Expose
    @SerializedName("continuityTime")
    int continuityTime;

    @Expose
    @SerializedName("eventImageList")
    ArrayList<EventImageDto> eventImageList;

    @Expose
    @SerializedName("eventMusicList")
    ArrayList<EventMusicDto> eventMusicList;

    @Expose
    @SerializedName("eventContents")
    EventContents eventContents;

    @Expose
    @SerializedName("runEvent")
    long runEvent;

    @Expose
    @SerializedName("eventState")
    String eventState;

    @Expose
    @SerializedName("homeColor")
    String homeColor;
    @Expose
    @SerializedName("homeFont")
    String homeFont;
    @Expose
    @SerializedName("awayColor")
    String awayColor;
    @Expose
    @SerializedName("awayFont")
    String awayFont;

    @Expose
    @SerializedName("openchatUrl")
    String openchatUrl;

    @Expose
    @SerializedName("volumeValue")
    int volumeValue;

    @Expose
    @SerializedName("cheerUrl1")
    String cheerUrl1;

    @Expose
    @SerializedName("cheerUrl2")
    String cheerUrl2;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
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

    public ArrayList<EventImageDto> getEventImageList() {
        return eventImageList;
    }

    public void setEventImageList(ArrayList<EventImageDto> eventImageList) {
        this.eventImageList = eventImageList;
    }

    public ArrayList<EventMusicDto> getEventMusicList() {
        return eventMusicList;
    }

    public void setEventMusicList(ArrayList<EventMusicDto> eventMusicList) {
        this.eventMusicList = eventMusicList;
    }

    public EventContents getEventContents() {
        return eventContents;
    }

    public void setEventContents(EventContents eventContents) {
        this.eventContents = eventContents;
    }

    public long getRunEvent() {
        return runEvent;
    }

    public void setRunEvent(long runEvent) {
        this.runEvent = runEvent;
    }

    public String getEventState() {
        return eventState;
    }

    public void setEventState(String eventState) {
        this.eventState = eventState;
    }

    public String getHomeColor() {
        return homeColor;
    }

    public void setHomeColor(String homeColor) {
        this.homeColor = homeColor;
    }

    public String getAwayColor() {
        return awayColor;
    }

    public void setAwayColor(String awayColor) {
        this.awayColor = awayColor;
    }

    public String getHomeFont() {
        return homeFont;
    }

    public void setHomeFont(String homeFont) {
        this.homeFont = homeFont;
    }

    public String getAwayFont() {
        return awayFont;
    }

    public void setAwayFont(String awayFont) {
        this.awayFont = awayFont;
    }

    public String getOpenchatUrl() {
        return openchatUrl;
    }

    public void setOpenchatUrl(String openchaUrl) {
        this.openchatUrl = openchaUrl;
    }

    public int getVolumeValue() {
        return volumeValue;
    }

    public void setVolumeValue(int volumeValue) {
        this.volumeValue = volumeValue;
    }

    public String getCheerUrl1() {
        return cheerUrl1;
    }

    public void setCheerUrl1(String cheerUrl1) {
        this.cheerUrl1 = cheerUrl1;
    }

    public String getCheerUrl2() {
        return cheerUrl2;
    }

    public void setCheerUrl2(String cheerUrl2) {
        this.cheerUrl2 = cheerUrl2;
    }
}
