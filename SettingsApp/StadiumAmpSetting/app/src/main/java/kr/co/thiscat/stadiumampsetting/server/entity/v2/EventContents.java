package kr.co.thiscat.stadiumampsetting.server.entity.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EventContents
{
    @Expose
    @SerializedName("eventId")
    String eventId;

    @Expose
    @SerializedName("allImageList")
    ArrayList<EventImageDto> allImageList;
    @Expose
    @SerializedName("eventMusicList")
    ArrayList<EventMusicDto> allMusicList;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public ArrayList<EventImageDto> getAllImageList() {
        return allImageList;
    }

    public void setAllImageList(ArrayList<EventImageDto> allImageList) {
        this.allImageList = allImageList;
    }

    public ArrayList<EventMusicDto> getAllMusicList() {
        return allMusicList;
    }

    public void setAllMusicList(ArrayList<EventMusicDto> allMusicList) {
        this.allMusicList = allMusicList;
    }
}
