package kr.co.thiscat.stadiumampsetting.server.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventInfoResult
{
    @Expose
    @SerializedName("result")
    private ResultMsg result;

    @Expose
    @SerializedName("data")
    private EventInfo data;

    public ResultMsg getResult() {
        return result;
    }

    public void setResult(ResultMsg result) {
        this.result = result;
    }

    public EventInfo getData() {
        return data;
    }

    public void setData(EventInfo data) {
        this.data = data;
    }
}
