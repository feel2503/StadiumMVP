package kr.co.thiscat.stadiumampsetting.server.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StadiumServer
{
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("regDateTime")
    private String regDateTime;

    @Expose
    @SerializedName("edtDateTime")
    private String edtDateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
