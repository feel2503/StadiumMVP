package kr.co.thiscat.stadiumampsetting.server.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entertainment
{
    @Expose
    @SerializedName("id")
    private int id;
    private long serverId;
    @Expose
    @SerializedName("ssaid")
    private String ssaid;

    @Expose
    @SerializedName("regDateTime")
    private String regDateTime;

    @Expose
    @SerializedName("edtDateTime")
    private String edtDateTime;

    String defaultMusic;
    String homeMusic1;
    String homeMusic2;
    String awayMusic1;
    String awayMusic2;
    String defaultImage;
    String homeImage;
    String awayImage;
    String webUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSsaid() {
        return ssaid;
    }

    public void setSsaid(String ssaid) {
        this.ssaid = ssaid;
    }

    public String getRegDateTime() {
        return regDateTime;
    }

    public void setRegDateTime(String regDateTime) {
        this.regDateTime = regDateTime;
    }

    public String getEdtDateTime() {
        return edtDateTime;
    }

    public void setEdtDateTime(String edtDateTime) {
        this.edtDateTime = edtDateTime;
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

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
