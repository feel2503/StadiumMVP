package kr.co.thiscat.stadiumampsetting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

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

    private PermissionUtil mPermUtil;
    public static String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        SettingListener(); //리스너 등록

        //맨 처음 시작할 탭 설정
        bottomNavigationView.setSelectedItemId(R.id.tab_setting);

        mPreferenceUtil = new PreferenceUtil(MainActivity.this);
        mServer = ServerManager.getInstance(MainActivity.this);
        int serverId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_SERVER_ID, -1);
        //mEventId = 1;
        if(serverId > 0)
        {
            mServer.getLastEvent(mLastEventCallBack, serverId);
        }
        mMediaPlayer = new MediaPlayer();

        mPermUtil = new PermissionUtil(MainActivity.this, REQUIRED_PERMISSIONS);
        mPermUtil.onSetPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
        mMediaPlayer.release();
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

    private void updateTime(final RunEvent runEvent)
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
                second = (int)(diffTime / 1000);
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if(second > 0) {
                            //1초씩 감소
                            second--;
                            // 0분 이상이면
                        }
                        else
                        {
                            //mMediaPlayer.reset();
                            mServer.getLastEvent(mEventStateCallBack, runEvent.getId());
                        }
                    }
                };
                timer.schedule(timerTask, 0, 1000);
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
                updateTime(runEvent);
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
                        strUri = runEvent.getHomeMusic();
                    }
                    else
                    {
                        strUri = runEvent.getAwayMusic();
                    }
                    if(strUri == null || strUri.length() < 1)
                        strUri = runEvent.getDefaultMusic();
                    if(strUri != null && strUri.length() > 0)
                    {
                        Uri uri = Uri.parse(strUri);
                        mMediaPlayer.setDataSource(getApplicationContext(), uri);
                        mMediaPlayer.start();
                    }
                }catch (Exception e)
                {

                }

            }
            else
            {
                // no event
            }
        }
    };

}