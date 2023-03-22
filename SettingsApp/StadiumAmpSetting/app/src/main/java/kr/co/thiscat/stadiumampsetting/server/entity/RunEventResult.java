package kr.co.thiscat.stadiumampsetting.server.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RunEventResult
{
    @Expose
    @SerializedName("result")
    private ResultMsg result;

    @Expose
    @SerializedName("data")
    private RunEvent data;

    public ResultMsg getResult() {
        return result;
    }

    public void setResult(ResultMsg result) {
        this.result = result;
    }

    public RunEvent getData() {
        return data;
    }

    public void setData(RunEvent data) {
        this.data = data;
    }
}
