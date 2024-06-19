package kr.co.thiscat.stadiumampsetting.server.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventImageDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventMusicDto;

public class RunEvent
{
    @Expose
    @SerializedName("id")
    private long id;

    @Expose
    @SerializedName("eventId")
    private long eventId;

    private String serverName;
    private String homeName;
    private String awayName;


    @Expose
    @SerializedName("resultTime")
    private int resultTime;

    @Expose
    @SerializedName("homeCount")
    private int homeCount;

    private int home1Count;
    private int home2Count;
    private int home3Count;
    private int home4Count;
    private int home5Count;

    @Expose
    @SerializedName("awayCount")
    private int awayCount;
    private int away1Count;
    private int away2Count;
    private int away3Count;
    private int away4Count;
    private int away5Count;

    @Expose
    @SerializedName("eventState")
    private String eventState;

    @Expose
    @SerializedName("startDateTime")
    private String startDateTime;
    int triggerType;
    int triggerTime;
    int triggerVote;

    String homeImg;
    String awayImg;
    String defaultImg;

    @Expose
    @SerializedName("webUrl")
    String webUrl;

    @Expose
    @SerializedName("openchatUrl")
    String openchatUrl;

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
    @SerializedName("volumeValue")
    int volumeValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public int getResultTime() {
        return resultTime;
    }

    public void setResultTime(int resultTime) {
        this.resultTime = resultTime;
    }

    public int getHomeCount() {
        return homeCount;
    }

    public void setHomeCount(int homeCount) {
        this.homeCount = homeCount;
    }

    public int getAwayCount() {
        return awayCount;
    }

    public void setAwayCount(int awayCount) {
        this.awayCount = awayCount;
    }

    public String getEventState() {
        return eventState;
    }

    public void setEventState(String eventState) {
        this.eventState = eventState;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getHomeImg() {
        return homeImg;
    }

    public void setHomeImg(String homeImg) {
        this.homeImg = homeImg;
    }

    public String getAwayImg() {
        return awayImg;
    }

    public void setAwayImg(String awayImg) {
        this.awayImg = awayImg;
    }

    public String getDefaultImg() {
        return defaultImg;
    }

    public void setDefaultImg(String defaultImg) {
        this.defaultImg = defaultImg;
    }

    public int getHome1Count() {
        return home1Count;
    }

    public void setHome1Count(int home1Count) {
        this.home1Count = home1Count;
    }

    public int getHome2Count() {
        return home2Count;
    }

    public void setHome2Count(int home2Count) {
        this.home2Count = home2Count;
    }

    public int getAway1Count() {
        return away1Count;
    }

    public void setAway1Count(int away1Count) {
        this.away1Count = away1Count;
    }

    public int getAway2Count() {
        return away2Count;
    }

    public void setAway2Count(int away2Count) {
        this.away2Count = away2Count;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getHome3Count() {
        return home3Count;
    }

    public void setHome3Count(int home3Count) {
        this.home3Count = home3Count;
    }

    public int getHome4Count() {
        return home4Count;
    }

    public void setHome4Count(int home4Count) {
        this.home4Count = home4Count;
    }

    public int getHome5Count() {
        return home5Count;
    }

    public void setHome5Count(int home5Count) {
        this.home5Count = home5Count;
    }

    public int getAway3Count() {
        return away3Count;
    }

    public void setAway3Count(int away3Count) {
        this.away3Count = away3Count;
    }

    public int getAway4Count() {
        return away4Count;
    }

    public void setAway4Count(int away4Count) {
        this.away4Count = away4Count;
    }

    public int getAway5Count() {
        return away5Count;
    }

    public void setAway5Count(int away5Count) {
        this.away5Count = away5Count;
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

    public String getOpenchatUrl() {
        return openchatUrl;
    }

    public void setOpenchatUrl(String openchatUrl) {
        this.openchatUrl = openchatUrl;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getAwayName() {
        return awayName;
    }

    public void setAwayName(String awayName) {
        this.awayName = awayName;
    }

    public int getVolumeValue() {
        return volumeValue;
    }

    public void setVolumeValue(int volumeValue) {
        this.volumeValue = volumeValue;
    }
}
