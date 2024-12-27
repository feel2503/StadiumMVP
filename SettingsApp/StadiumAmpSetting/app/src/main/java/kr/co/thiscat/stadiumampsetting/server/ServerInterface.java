package kr.co.thiscat.stadiumampsetting.server;


import kr.co.thiscat.stadiumampsetting.server.entity.Entertainment;
import kr.co.thiscat.stadiumampsetting.server.entity.EventInfoResult;
import kr.co.thiscat.stadiumampsetting.server.entity.result.EventListResult;
import kr.co.thiscat.stadiumampsetting.server.entity.result.EventResult;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventContents;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventRequestDto;
import kr.co.thiscat.stadiumampsetting.server.entity.ResultMsg;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEventResult;
import kr.co.thiscat.stadiumampsetting.server.entity.StadiumServerResult;
import kr.co.thiscat.stadiumampsetting.server.entity.StartEvent;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventStartReqDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventcontinuityTypeDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServerInterface {

    @POST("v1/server/update")
    Call<ResultMsg> serverUpdate(@Body Entertainment body);

    @GET("v1/server/server")
    Call<StadiumServerResult> getServer(@Query("serverId")long serverId);

//    @GET("v1/server/list")
//    Call<StadiumServerResult> getServerList();

    @GET("v1/event/event-info")
    Call<EventInfoResult> getEventInfo(@Query("serverId")long serverId, @Query("ssaid")String ssaid);

    @GET("v1/event/last-event")
    Call<RunEventResult> getLastEvent(@Query("serverId")long serverId);

    @GET("v1/event/state")
    Call<RunEventResult> getEventState(@Query("eventId")long eventId);

    @POST("v1/event/start")
    Call<RunEventResult> eventStart(@Body EventStartReqDto eventStartReqDto);

    @GET("v1/event/stop")
    Call<RunEventResult> eventStop(@Query("runEventId")long runEventId);

    @GET("v1/event/result")
    Call<RunEventResult> eventResult(@Query("eventId")long eventId);

    @GET("v1/event/nowstate")
    Call<RunEventResult> eventNowResult(@Query("eventId")long eventId);

    // v2
    @GET("v1/server/event-info")
    Call<EventResult> getEvent(@Query("eventId")long eventId);

    @GET("v1/server/list")
    Call<EventListResult> getServerList();

    @POST("v1/server/content-list")
    Call<EventContents> getEventContentList(@Body EventRequestDto eventRequestDto);

    @GET("v1/event/runstate")
    Call<RunEventResult> getRunEventState(@Query("runEventId")long eventId);

    @POST("v1/server/continuityType")
    Call<EventResult> setContinuityType(@Body EventcontinuityTypeDto eventcontinuityTypeDto);

    @GET("v1/event/set-volume")
    Call<EventResult> setEventVolume(@Query("eventId")long eventId, @Query("volume")int volume);

    @GET("v1/event/next-runevent")
    Call<RunEventResult> nextRunEvent(@Query("eventId")long eventId, @Query("runEventId")long runEventId);

    @GET("v1/event/stop-lastevent")
    Call<RunEventResult> stopLastEvent(@Query("eventId")long eventId);
}
