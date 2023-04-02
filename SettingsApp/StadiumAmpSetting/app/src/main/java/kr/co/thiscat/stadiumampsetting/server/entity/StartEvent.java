package kr.co.thiscat.stadiumampsetting.server.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartEvent
{
    @Expose
    @SerializedName("stadiumServerId")
    private long stadiumServerId;

    @Expose
    @SerializedName("voteTime")
    private int voteTime;

    @Expose
    @SerializedName("resultTime")
    private int resultTime;

    String defaultMusic;
    String homeMusic1;
    String homeMusic2;
    String awayMusic1;
    String awayMusic2;
    String defaultImage;
    String homeImage;
    String awayImage;

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
}
