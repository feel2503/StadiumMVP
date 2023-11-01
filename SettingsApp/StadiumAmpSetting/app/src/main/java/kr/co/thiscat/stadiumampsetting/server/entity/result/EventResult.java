package kr.co.thiscat.stadiumampsetting.server.entity.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kr.co.thiscat.stadiumampsetting.server.entity.ResultMsg;
import kr.co.thiscat.stadiumampsetting.server.entity.StadiumServer;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventDto;

public class EventResult
{
    @Expose
    @SerializedName("result")
    private ResultMsg result;

    @Expose
    @SerializedName("data")
    private EventDto data;

    public ResultMsg getResult() {
        return result;
    }

    public void setResult(ResultMsg result) {
        this.result = result;
    }

    public EventDto getData() {
        return data;
    }

    public void setData(EventDto data) {
        this.data = data;
    }
}
