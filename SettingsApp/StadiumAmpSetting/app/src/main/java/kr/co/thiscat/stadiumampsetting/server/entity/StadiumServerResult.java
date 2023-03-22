package kr.co.thiscat.stadiumampsetting.server.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StadiumServerResult
{
    @Expose
    @SerializedName("result")
    private ResultMsg result;

    @Expose
    @SerializedName("data")
    private ArrayList<StadiumServer> data;

    public ResultMsg getResult() {
        return result;
    }

    public void setResult(ResultMsg result) {
        this.result = result;
    }

    public ArrayList<StadiumServer> getData() {
        return data;
    }

    public void setData(ArrayList<StadiumServer> data) {
        this.data = data;
    }
}
