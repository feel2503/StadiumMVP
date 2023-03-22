package kr.co.thiscat.stadiumampsetting.server;


import kr.co.thiscat.stadiumampsetting.server.entity.RunEventResult;
import kr.co.thiscat.stadiumampsetting.server.entity.StadiumServerResult;
import kr.co.thiscat.stadiumampsetting.server.entity.StartEvent;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServerInterface {

    @GET("v1/server/list")
    Call<StadiumServerResult> getServerList();

    @GET("v1/event/last-event")
    Call<RunEventResult> getLastEvent(@Query("serverId")long serverId);

    @GET("v1/event/state")
    Call<RunEventResult> getEventState(@Query("eventId")long eventId);

    @POST("v1/event/start")
    Call<RunEventResult> eventStart(@Body StartEvent body);

    @GET("v1/event/stop")
    Call<RunEventResult> eventStop(@Query("eventId")long eventId);

    @GET("v1/event/result")
    Call<RunEventResult> eventResult(@Query("eventId")long eventId);

}
