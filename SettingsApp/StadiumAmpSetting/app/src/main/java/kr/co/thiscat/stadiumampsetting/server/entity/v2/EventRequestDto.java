package kr.co.thiscat.stadiumampsetting.server.entity.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventRequestDto {
    @Expose
    @SerializedName("id")
    private long id;

    @Expose
    @SerializedName("ssaid")
    private String ssaid;

    public EventRequestDto(long id, String ssaid) {
        this.id = id;
        this.ssaid = ssaid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSsaid() {
        return ssaid;
    }

    public void setSsaid(String ssaid) {
        this.ssaid = ssaid;
    }
}
