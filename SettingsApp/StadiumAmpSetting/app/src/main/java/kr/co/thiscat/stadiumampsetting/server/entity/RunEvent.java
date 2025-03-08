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
    private int home6Count;
    private int home7Count;
    private int home8Count;
    private int home9Count;
    private int home10Count;
    private int home11Count;
    private int home12Count;
    private int home13Count;
    private int home14Count;
    private int home15Count;
    private int home16Count;
    private int home17Count;
    private int home18Count;
    private int home19Count;
    private int home20Count;

    @Expose
    @SerializedName("awayCount")
    private int awayCount;
    private int away1Count;
    private int away2Count;
    private int away3Count;
    private int away4Count;
    private int away5Count;
    private int away6Count;
    private int away7Count;
    private int away8Count;
    private int away9Count;
    private int away10Count;
    private int away11Count;
    private int away12Count;
    private int away13Count;
    private int away14Count;
    private int away15Count;
    private int away16Count;
    private int away17Count;
    private int away18Count;
    private int away19Count;
    private int away20Count;

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


    public int getAway10Count() {
        return away10Count;
    }

    public void setAway10Count(int away10Count) {
        this.away10Count = away10Count;
    }

    public int getAway11Count() {
        return away11Count;
    }

    public void setAway11Count(int away11Count) {
        this.away11Count = away11Count;
    }

    public int getAway12Count() {
        return away12Count;
    }

    public void setAway12Count(int away12Count) {
        this.away12Count = away12Count;
    }

    public int getAway13Count() {
        return away13Count;
    }

    public void setAway13Count(int away13Count) {
        this.away13Count = away13Count;
    }

    public int getAway14Count() {
        return away14Count;
    }

    public void setAway14Count(int away14Count) {
        this.away14Count = away14Count;
    }

    public int getAway15Count() {
        return away15Count;
    }

    public void setAway15Count(int away15Count) {
        this.away15Count = away15Count;
    }

    public int getAway16Count() {
        return away16Count;
    }

    public void setAway16Count(int away16Count) {
        this.away16Count = away16Count;
    }

    public int getAway17Count() {
        return away17Count;
    }

    public void setAway17Count(int away17Count) {
        this.away17Count = away17Count;
    }

    public int getAway18Count() {
        return away18Count;
    }

    public void setAway18Count(int away18Count) {
        this.away18Count = away18Count;
    }

    public int getAway19Count() {
        return away19Count;
    }

    public void setAway19Count(int away19Count) {
        this.away19Count = away19Count;
    }

    public int getAway20Count() {
        return away20Count;
    }

    public void setAway20Count(int away20Count) {
        this.away20Count = away20Count;
    }

    public int getAway6Count() {
        return away6Count;
    }

    public void setAway6Count(int away6Count) {
        this.away6Count = away6Count;
    }

    public int getAway7Count() {
        return away7Count;
    }

    public void setAway7Count(int away7Count) {
        this.away7Count = away7Count;
    }

    public int getAway8Count() {
        return away8Count;
    }

    public void setAway8Count(int away8Count) {
        this.away8Count = away8Count;
    }

    public int getAway9Count() {
        return away9Count;
    }

    public void setAway9Count(int away9Count) {
        this.away9Count = away9Count;
    }

    public int getHome10Count() {
        return home10Count;
    }

    public void setHome10Count(int home10Count) {
        this.home10Count = home10Count;
    }

    public int getHome11Count() {
        return home11Count;
    }

    public void setHome11Count(int home11Count) {
        this.home11Count = home11Count;
    }

    public int getHome12Count() {
        return home12Count;
    }

    public void setHome12Count(int home12Count) {
        this.home12Count = home12Count;
    }

    public int getHome13Count() {
        return home13Count;
    }

    public void setHome13Count(int home13Count) {
        this.home13Count = home13Count;
    }

    public int getHome14Count() {
        return home14Count;
    }

    public void setHome14Count(int home14Count) {
        this.home14Count = home14Count;
    }

    public int getHome15Count() {
        return home15Count;
    }

    public void setHome15Count(int home15Count) {
        this.home15Count = home15Count;
    }

    public int getHome16Count() {
        return home16Count;
    }

    public void setHome16Count(int home16Count) {
        this.home16Count = home16Count;
    }

    public int getHome17Count() {
        return home17Count;
    }

    public void setHome17Count(int home17Count) {
        this.home17Count = home17Count;
    }

    public int getHome18Count() {
        return home18Count;
    }

    public void setHome18Count(int home18Count) {
        this.home18Count = home18Count;
    }

    public int getHome19Count() {
        return home19Count;
    }

    public void setHome19Count(int home19Count) {
        this.home19Count = home19Count;
    }

    public int getHome20Count() {
        return home20Count;
    }

    public void setHome20Count(int home20Count) {
        this.home20Count = home20Count;
    }

    public int getHome6Count() {
        return home6Count;
    }

    public void setHome6Count(int home6Count) {
        this.home6Count = home6Count;
    }

    public int getHome7Count() {
        return home7Count;
    }

    public void setHome7Count(int home7Count) {
        this.home7Count = home7Count;
    }

    public int getHome8Count() {
        return home8Count;
    }

    public void setHome8Count(int home8Count) {
        this.home8Count = home8Count;
    }

    public int getHome9Count() {
        return home9Count;
    }

    public void setHome9Count(int home9Count) {
        this.home9Count = home9Count;
    }
}
