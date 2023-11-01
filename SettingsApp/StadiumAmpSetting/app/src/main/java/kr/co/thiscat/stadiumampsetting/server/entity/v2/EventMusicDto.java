package kr.co.thiscat.stadiumampsetting.server.entity.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventMusicDto
{
    @Expose
    @SerializedName("musicId")
    private long musicId;
    @Expose
    @SerializedName("eventId")
    private long eventId;
    @Expose
    @SerializedName("musicName")
    private String musicName;
    @Expose
    @SerializedName("teamType")
    private String teamType;
    @Expose
    @SerializedName("musicUrl")
    private String musicUrl;
    @Expose
    @SerializedName("sequence")
    private int sequence;

    public long getMusicId() {
        return musicId;
    }

    public void setMusicId(long musicId) {
        this.musicId = musicId;
    }

    public String getTeamType() {
        return teamType;
    }

    public void setTeamType(String teamType) {
        this.teamType = teamType;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }
}
