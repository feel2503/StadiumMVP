package kr.co.thiscat.stadiumampsetting;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.thiscat.stadiumampsetting.fragment.EventFragment;
import kr.co.thiscat.stadiumampsetting.fragment.EventSettingFragment;
import kr.co.thiscat.stadiumampsetting.fragment.SettingFragment;
import kr.co.thiscat.stadiumampsetting.server.SECallBack;
import kr.co.thiscat.stadiumampsetting.server.ServerManager;
import kr.co.thiscat.stadiumampsetting.server.entity.EventInfo;
import kr.co.thiscat.stadiumampsetting.server.entity.EventInfoResult;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEvent;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEventResult;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private EventSettingFragment eventSettingFragment;
    private EventFragment eventFragment;
    private SettingFragment settingFragment;

    LinearLayout home_ly;
    BottomNavigationView bottomNavigationView;

    private ServerManager mServer;
    private PreferenceUtil mPreferenceUtil;
    private int second;

    private MediaPlayer mMediaPlayer;
    private MediaPlayer mDefaultMediaPlayer;

    private PermissionUtil mPermUtil;
    public static String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public String mStrDefault;
    public String mStrHome1;
    public String mStrHome2;
    public String mStrAway1;
    public String mStrAway2;
    public String mStrDefaultImg;
    public String mStrHomeImg;
    public String mStrAwayImg;

    protected ProgressDialog mProgress = null;

    public int mServerId;
    public int mEventId;
    public EventInfo currEventInfo = null;
    private Timer mTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("stadiumAMP");
        mPreferenceUtil = new PreferenceUtil(MainActivity.this);
        mServer = ServerManager.getInstance(MainActivity.this);

        mServerId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_SERVER_ID, -1);
        mEventId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_EVENT_ID, -1);
        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        eventSettingFragment = EventSettingFragment.getInstance();
        settingFragment = SettingFragment.getInstance();
        eventFragment = EventFragment.getInstance();

        SettingListener(); //리스너 등록

        //맨 처음 시작할 탭 설정
        bottomNavigationView.setSelectedItemId(R.id.tab_event_setting);



        mMediaPlayer = new MediaPlayer();
        mDefaultMediaPlayer = new MediaPlayer();

        mPermUtil = new PermissionUtil(MainActivity.this, REQUIRED_PERMISSIONS);
        mPermUtil.onSetPermission();

        mProgress = new ProgressDialog(this);
        mTimer = new Timer();
        initEventInfo();
    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    public String getSSAID()
    {
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
        return android_id;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mMediaPlayer.stop();
//        mMediaPlayer.release();
//        try {
//            mDefaultMediaPlayer.stop();
//            mDefaultMediaPlayer.prepare();
//            mDefaultMediaPlayer.release();
//            mDefaultMediaPlayer = null;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            mMediaPlayer.stop();
            mMediaPlayer.prepare();
            mMediaPlayer.release();
            mMediaPlayer = null;

            mDefaultMediaPlayer.stop();
            mDefaultMediaPlayer.prepare();
            mDefaultMediaPlayer.release();
            mDefaultMediaPlayer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PermissionUtil.MY_PERMISSIONS_REQUEST)
        {
            if(mPermUtil.verifyPermission(permissions, grantResults) == false)
            { // 권한 얻기 실패
                mPermUtil.showRequestAgainDialog();
            }
            else
            {
                //startThisCatService();
            }
        }
//        switch(requestCode)
//        {
//            case PermissionUtil.MY_PERMISSIONS_REQUEST:
//                if(mPermUtil.verifyPermission(permissions, grantResults) == false)
//                { // 권한 얻기 실패
//                    mPermUtil.showRequestAgainDialog();
//                }
//                break;
//        }
//

    }

    public void initEventInfo()
    {
        if(mServerId > 0)
        {
            showProgress(MainActivity.this, true);
            mServer.getEventInfo(mEventInfoCallBack, mServerId, getSSAID());
        }
    }

    public void startEventCount()
    {
        //int serverId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_SERVER_ID, -1);
        //mEventId = 1;
        if(mServerId > 0)
        {
            //mServer.getLastEvent(mLastEventCallBack, mServerId);
            mServer.getEventInfo(mEventInfoCallBack, mServerId, getSSAID());
        }
    }

    public void stopEvent()
    {

    }

    private void SettingListener() {
        //선택 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new TabSelectedListener());
    }


    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.tab_event_setting: {
                    Log.d("AAAA", "----- onNavigationItemSelected : " + eventSettingFragment);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, eventSettingFragment)
                            .commit();
                    return true;
                }
                case R.id.tab_setting: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, settingFragment)
                            .commit();
                    return true;
                }
                case R.id.tab_event: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, eventFragment)
                            .commit();
                    return true;
                }
            }

            return false;
        }
    }

    private void startEventTimer()
    {

    }
    private void updateResultTime(String startDate, int voteTime)
    {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date startTime = sdf.parse(startDate);

            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            cal.add(Calendar.MINUTE, voteTime);
            Date endDate = cal.getTime();

            Date nowDate = Calendar.getInstance().getTime();

            long diffTime = endDate.getTime() - nowDate.getTime();
            if(diffTime <= 0)
            {
                // 종료된 이벤트
                return;
            }
            else
            {
                try{
                    Uri uri = Uri.parse(mStrDefault);
//                    mDefaultMediaPlayer.setScreenOnWhilePlaying(true);
//                    mDefaultMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.ON_AFTER_RELEASE);
                    if(mDefaultMediaPlayer == null)
                    {
                        mDefaultMediaPlayer = new MediaPlayer();
                        mDefaultMediaPlayer.setDataSource(getApplicationContext(), uri);
                    }
                    mDefaultMediaPlayer.prepare();
                    mDefaultMediaPlayer.setLooping(true);
                    mDefaultMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mDefaultMediaPlayer.start();
                        }
                    });

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                if(mTimer != null)
                {
                    mTimer.cancel();
                    mTimer = null;
                }
                mTimer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        Date currentDate = Calendar.getInstance().getTime();
                        long diffTime = endDate.getTime() - currentDate.getTime();
                        if(diffTime > 0){
                            if(eventFragment != null){
                                eventFragment.updateTimer();
                            }
                        }
                        else {
                            mTimer.cancel();
                            mTimer = null;
                            int serverId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_SERVER_ID, -1);
                            Log.d("AAAA", "serverid : " + serverId);
                            mServer.getLastEvent(mEventStateCallBack, serverId);
                        }
                    }
                };
                mTimer.schedule(timerTask, 0, 1000);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void showProgress(final Activity act, final boolean bShow)
    {
        if(act == null)
            return;
        act.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgress.setMessage("Waitting...");
                mProgress.setCancelable(false);

                try
                {
                    if (bShow)
                    {
                        mProgress.show();
                    }
                    else
                    {
                        mProgress.dismiss();
                    }
                }
                catch (Exception e)
                {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
    }

    private SECallBack<EventInfoResult> mEventInfoCallBack = new SECallBack<EventInfoResult>()
    {
        @Override
        public void onResponseResult(Response<EventInfoResult> response)
        {
            if (response.isSuccessful())
            {
                currEventInfo = response.body().getData();
                mStrDefault = currEventInfo.getDefaultMusic();
                mStrHome1 = currEventInfo.getHomeMusic1();
                mStrHome2 = currEventInfo.getHomeMusic2();
                mStrAway1 = currEventInfo.getAwayMusic1();
                mStrAway2 = currEventInfo.getAwayMusic2();
                mStrDefaultImg = currEventInfo.getDefaultImage();
                mStrHomeImg = currEventInfo.getHomeImage();
                mStrAwayImg = currEventInfo.getAwayImage();
                eventSettingFragment.updateMediaData();

                updateResultTime(currEventInfo.getStartDateTime(), currEventInfo.getVoteTime());
            }
            else
            {
                // no event
            }
            showProgress(MainActivity.this, false);
        }
    };

    private SECallBack<RunEventResult> mLastEventCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                RunEvent runEvent = response.body().getData();
                updateResultTime(runEvent.getStartDateTime(), runEvent.getVoteTime());
            }
            else
            {
                // no event
            }
        }
    };

    private SECallBack<RunEventResult> mEventStateCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                try{
                    String strUri = "";
                    RunEvent runEvent = response.body().getData();
                    if(runEvent.getHomeCount() >= runEvent.getAwayCount())
                    {
                        if(runEvent.getHome1Count() > runEvent.getHome2Count())
                            strUri = mStrHome1;
                        else
                            strUri = mStrHome2;
                    }
                    else
                    {
                        if(runEvent.getAway1Count() > 0)
                            strUri = mStrAway1;
                        else
                            strUri = mStrAway2;
                    }
//                    if(strUri == null || strUri.length() < 1)
//                        strUri = mStrDefault;

                    Log.d("AAAA", "player Uri : " + strUri);
                    if(strUri != null && strUri.length() > 0)
                    {
                        mDefaultMediaPlayer.pause();

                        Uri uri = Uri.parse(strUri);
//                        mMediaPlayer.setScreenOnWhilePlaying(true);
//                        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.ON_AFTER_RELEASE);
                        if(mMediaPlayer == null)
                            mMediaPlayer = new MediaPlayer();
                        mMediaPlayer.setDataSource(getApplicationContext(), uri);
                        mMediaPlayer.prepare();
                        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mMediaPlayer.start();
                            }
                        });

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            else
            {
                // no event
            }
        }
    };

}