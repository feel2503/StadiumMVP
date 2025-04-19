package kr.co.thiscat.stadiumampsetting.server.entity.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import kr.co.thiscat.stadiumampsetting.server.entity.ResultMsg;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.VolumeDto;

public class VolumeResult
{
    @Expose
    @SerializedName("result")
    private ResultMsg result;

    @Expose
    @SerializedName("data")
    private VolumeDto data;

    public ResultMsg getResult() {
        return result;
    }

    public void setResult(ResultMsg result) {
        this.result = result;
    }

    public VolumeDto getData() {
        return data;
    }

    public void setData(VolumeDto data) {
        this.data = data;
    }
}
