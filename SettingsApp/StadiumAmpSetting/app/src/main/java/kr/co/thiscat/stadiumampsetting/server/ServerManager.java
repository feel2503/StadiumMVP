package kr.co.thiscat.stadiumampsetting.server;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import kr.co.thiscat.stadiumampsetting.server.entity.ResultMsg;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEventResult;
import kr.co.thiscat.stadiumampsetting.server.entity.StadiumServer;
import kr.co.thiscat.stadiumampsetting.server.entity.StadiumServerResult;
import kr.co.thiscat.stadiumampsetting.server.entity.StartEvent;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerManager {
//    private String BASE_URL = "http://223.130.134.43/";
    private String BASE_URL = "http://192.168.123.195/";     // hanse
    public static int CONNECTION_TIMEOUT = 5000;
    public static int READ_TIMEOUT = 5000;
    public static int WRITE_TIMEOUT = 5000;

    private final int mDefaultTimeOut = 5 * 1000;
    private static Retrofit mRetropit;
    private Context mContext;
    private String mBaseUrl = BASE_URL;

    private static ServerInterface mServerInterface;

    private static ServerManager mServerManager;
    public static ServerManager getInstance(Context context)
    {
        if (mServerManager == null)
        {
            mServerManager = new ServerManager(context);
        }
        return mServerManager;
    }

    private ServerManager(Context context) {
        mContext = context;
        mBaseUrl = BASE_URL;
        mRetropit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
                .client(gethttpClient())
                .build();

        mServerInterface = mRetropit.create(ServerInterface.class);
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }

    private static OkHttpClient gethttpClient()
    {
        // TimeOut 주는 방법
        // 1. OkHttpClient 객체준비
        // 2. OkHttpClient 객체를 Bulid하기전에 Timeout메소드 들에 값을 넣음
        //   readTimeout(원하는 시간 Int, Timeunit.원하는 시간단위) -> 읽어오는 시간 Timeout
        //   connectTimeout(원하는 시간 Int, Timeunit.원하는 시간단위) -> 연결하는 시간 Timeout
        //   writeTimeout(원하는 시간 Int, Timeunit.원하는 시간단위) -> 쓰는데 걸리는 시간 Timeout
        // 3. Retrofit 객체가 Build되기전에 clinet("Put this Method")에 OkhttpClinet 객체를 넣어준다.
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new ServerInterceptor())
                .build();

        return httpClient;
    }

    public void getServer(SECallBack<StadiumServerResult> callBack, long serverId)
    {
        Call<StadiumServerResult> call = mServerInterface.getServer(serverId);
        call.enqueue(callBack);
    }

    public void serverUpdate(SECallBack<ResultMsg> callBack, StadiumServer body)
    {
        Call<ResultMsg> call = mServerInterface.serverUpdate(body);
        call.enqueue(callBack);
    }

    public void getServerList(SECallBack<StadiumServerResult> callBack)
    {
        Call<StadiumServerResult> call = mServerInterface.getServerList();
        call.enqueue(callBack);
    }

    public void getLastEvent(SECallBack<RunEventResult> callBack, long serverId)
    {
        Call<RunEventResult> call = mServerInterface.getLastEvent(serverId);
        call.enqueue(callBack);
    }

    public void getEventState(SECallBack<RunEventResult> callBack, long eventId)
    {
        Call<RunEventResult> call = mServerInterface.getEventState(eventId);
        call.enqueue(callBack);
    }

    public void eventStart(SECallBack<RunEventResult> callBack, StartEvent body)
    {
        Call<RunEventResult> call = mServerInterface.eventStart(body);
        call.enqueue(callBack);
    }

    public void eventStop(SECallBack<RunEventResult> callBack, long eventId)
    {
        Call<RunEventResult> call = mServerInterface.eventStop(eventId);
        call.enqueue(callBack);
    }

    public void eventResult(SECallBack<RunEventResult> callBack, long eventId)
    {
        Call<RunEventResult> call = mServerInterface.eventResult(eventId);
        call.enqueue(callBack);
    }

    public void eventNowResult(SECallBack<RunEventResult> callBack, long eventId)
    {
        Call<RunEventResult> call = mServerInterface.eventNowResult(eventId);
        call.enqueue(callBack);
    }
}
