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
}
