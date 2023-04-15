package kr.co.thiscat.stadiumampsetting;

import android.Manifest;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.thiscat.stadiumampsetting.fragment.EventFragment;
import kr.co.thiscat.stadiumampsetting.fragment.EventSettingFragment;
import kr.co.thiscat.stadiumampsetting.fragment.SettingFragment;
import kr.co.thiscat.stadiumampsetting.server.SECallBack;
import kr.co.thiscat.stadiumampsetting.server.ServerManager;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEvent;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEventResult;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        SettingListener(); //리스너 등록

        //맨 처음 시작할 탭 설정
        bottomNavigationView.setSelectedItemId(R.id.tab_event_setting);

        getSupportActionBar().setTitle("stadiumAMP");
        mPreferenceUtil = new PreferenceUtil(MainActivity.this);
        mServer = ServerManager.getInstance(MainActivity.this);

        mMediaPlayer = new MediaPlayer();
        mDefaultMediaPlayer = new MediaPlayer();

        mPermUtil = new PermissionUtil(MainActivity.this, REQUIRED_PERMISSIONS);
        mPermUtil.onSetPermission();

        startEventCount();
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

    public void startEventCount()
    {
        int serverId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_SERVER_ID, -1);
        //mEventId = 1;
        if(serverId > 0)
        {
            mServer.getLastEvent(mLastEventCallBack, serverId);
            if(mStrDefault != null && mStrDefault.length() > 0)
            {
//                try{
//                    Uri uri = Uri.parse(mStrDefault);
//                    mDefaultMediaPlayer.setDataSource(getApplicationContext(), uri);
//                    mDefaultMediaPlayer.prepare();
//                    mDefaultMediaPlayer.start();
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
            }
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
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new EventSettingFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_setting: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new SettingFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_event: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new EventFragment())
                            .commit();
                    return true;
                }

            }

            return false;
        }
    }

    private void updateResultTime(final RunEvent runEvent)
    {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date startTime = sdf.parse(runEvent.getStartDateTime());

            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            cal.add(Calendar.MINUTE, runEvent.getVoteTime());
            Date endDate = cal.getTime();

            Date nowDate = Calendar.getInstance().getTime();

            long diffTime = endDate.getTime() - nowDate.getTime();
            if(diffTime <= 0)
            {
                // 종료된 이벤트
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
                    mDefaultMediaPlayer.start();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        int serverId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_SERVER_ID, -1);
                        Log.d("AAAA", "serverid : " + serverId);
                        mServer.getLastEvent(mEventStateCallBack, serverId);
                    }
                };
                timer.schedule(timerTask, diffTime);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private SECallBack<RunEventResult> mLastEventCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                RunEvent runEvent = response.body().getData();
                updateResultTime(runEvent);
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
                        mMediaPlayer.start();
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