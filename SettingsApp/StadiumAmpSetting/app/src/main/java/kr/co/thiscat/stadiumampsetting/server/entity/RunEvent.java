package kr.co.thiscat.stadiumampsetting.server.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RunEvent
{
    @Expose
    @SerializedName("id")
    private long id;

    @Expose
    @SerializedName("stadiumserver")
    private long stadiumServerId;

    private String serverName;

    @Expose
    @SerializedName("voteTime")
    private int voteTime;

    @Expose
    @SerializedName("resultTime")
    private int resultTime;

    @Expose
    @SerializedName("homeCount")
    private int homeCount;

    private int home1Count;
    private int home2Count;


    @Expose
    @SerializedName("awayCount")
    private int awayCount;
    private int away1Count;
    private int away2Count;

    @Expose
    @SerializedName("eventState")
    private String eventState;

    @Expose
    @SerializedName("startDateTime")
    private String startDateTime;



    String homeImg;
    String awayImg;
    String defaultImg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStadiumServerId() {
        return stadiumServerId;
    }

    public void setStadiumServerId(long stadiumServerId) {
        this.stadiumServerId = stadiumServerId;
    }

    public int getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(int voteTime) {
        this.voteTime = voteTime;
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
}
