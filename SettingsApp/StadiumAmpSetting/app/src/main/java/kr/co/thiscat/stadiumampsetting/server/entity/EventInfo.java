package kr.co.thiscat.stadiumampsetting.server.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventInfo
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

    @Expose
    @SerializedName("awayCount")
    private int awayCount;

    @Expose
    @SerializedName("eventState")
    private String eventState;

    @Expose
    @SerializedName("startDateTime")
    private String startDateTime;

    private String defaultMusic;
    private String homeMusic1;
    private String homeMusic2;
    private String awayMusic1;
    private String awayMusic2;
    private String defaultImage;
    private String homeImage;
    private String awayImage;
    private String webUrl;

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


    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getDefaultMusic() {
        return defaultMusic;
    }

    public void setDefaultMusic(String defaultMusic) {
        this.defaultMusic = defaultMusic;
    }

    public String getHomeMusic1() {
        return homeMusic1;
    }

    public void setHomeMusic1(String homeMusic1) {
        this.homeMusic1 = homeMusic1;
    }

    public String getHomeMusic2() {
        return homeMusic2;
    }

    public void setHomeMusic2(String homeMusic2) {
        this.homeMusic2 = homeMusic2;
    }

    public String getAwayMusic1() {
        return awayMusic1;
    }

    public void setAwayMusic1(String awayMusic1) {
        this.awayMusic1 = awayMusic1;
    }

    public String getAwayMusic2() {
        return awayMusic2;
    }

    public void setAwayMusic2(String awayMusic2) {
        this.awayMusic2 = awayMusic2;
    }

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getHomeImage() {
        return homeImage;
    }

    public void setHomeImage(String homeImage) {
        this.homeImage = homeImage;
    }

    public String getAwayImage() {
        return awayImage;
    }

    public void setAwayImage(String awayImage) {
        this.awayImage = awayImage;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
