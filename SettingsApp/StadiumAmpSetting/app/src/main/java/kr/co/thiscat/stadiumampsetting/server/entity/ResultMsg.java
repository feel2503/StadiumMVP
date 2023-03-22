package kr.co.thiscat.stadiumampsetting.server.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultMsg
{
    @Expose
    @SerializedName("result")
    private CallResult result;


    class CallResult{
        @Expose
        @SerializedName("message")
        private String message;
    }
}
