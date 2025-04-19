package kr.co.thiscat.stadiumampsetting;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import kr.co.thiscat.stadiumampsetting.fragment.AwaySettingFragment;
import kr.co.thiscat.stadiumampsetting.fragment.EventFragment;
import kr.co.thiscat.stadiumampsetting.fragment.HomeSettingFragment;
import kr.co.thiscat.stadiumampsetting.fragment.SettingFragment;
import kr.co.thiscat.stadiumampsetting.server.SECallBack;
import kr.co.thiscat.stadiumampsetting.server.ServerManager;
import kr.co.thiscat.stadiumampsetting.server.entity.DownloadItem;
import kr.co.thiscat.stadiumampsetting.server.entity.EventInfo;
import kr.co.thiscat.stadiumampsetting.server.entity.EventInfoResult;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEvent;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEventResult;
import kr.co.thiscat.stadiumampsetting.server.entity.result.EventResult;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventImageDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventMusicDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventStartReqDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventcontinuityTypeDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.VolumeDto;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    public static long CHECK_DELAY = 2000L;
    private HomeSettingFragment homeSettingFragment;
    private AwaySettingFragment awaySettingFragment;
    public EventFragment eventFragment;
    public SettingFragment settingFragment;
    private FragmentManager fm;
    Fragment active;

    FrameLayout home_fl;
    BottomNavigationView bottomNavigationView;

    private ServerManager mServer;
    private PreferenceUtil mPreferenceUtil;
    private int second;

    private MediaPlayer mMediaPlayer;
    private MediaPlayer mDefaultMediaPlayer;
    private int psusePos = 0;

    private PermissionUtil mPermUtil;
//    public static String[] REQUIRED_PERMISSIONS = {
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//    };

    public String mStrDefault;
    public String mStrHome1;
    public String mStrHome2;
    public String mStrAway1;
    public String mStrAway2;
    public String mStrDefaultImg;
    public String mStrHomeImg;
    public String mStrAwayImg;
//    public String mStrWebUrl;

    public boolean mWebViewState = false;

    protected ProgressDialog mProgress = null;

    public int mServerId;
    public int mRunEventId = -1;
    private Timer mTimer;

    public EventDto mEventDto = null;
    public RunEvent mRunEvent = null;
    public boolean mEventRepeat = false;

    private boolean isFinish = false;

    public static String contentDirPath = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS) + "/StadiumAmp/";

    public int volumeValue;

    public int currentTriggerType = 0;
    public int currentContType = 0;

    private AudioManager mAudioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        getSupportActionBar().setTitle("stadiumAMP Setting App");
        mPreferenceUtil = new PreferenceUtil(MainActivity.this);
        mServer = ServerManager.getInstance(MainActivity.this);

        mServerId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_SERVER_ID, -1);
        //mEventId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_EVENT_ID, -1);
        //home_ly = findViewById(R.id.home_ly);
        home_fl = findViewById(R.id.main_container);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        homeSettingFragment = HomeSettingFragment.getInstance();

        awaySettingFragment = AwaySettingFragment.getInstance();
        settingFragment = SettingFragment.getInstance();
        eventFragment = EventFragment.getInstance();


        SettingListener(); //리스너 등록

        //맨 처음 시작할 탭 설정
        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.main_container, awaySettingFragment).hide(awaySettingFragment).commit();
        fm.beginTransaction().add(R.id.main_container, eventFragment).hide(eventFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeSettingFragment).hide(homeSettingFragment).commit();
        fm.beginTransaction().add(R.id.main_container, settingFragment).commit();
        active = homeSettingFragment;
        bottomNavigationView.setSelectedItemId(R.id.tab_setting);


        //mMediaPlayer = new MediaPlayer();
        //mDefaultMediaPlayer = new MediaPlayer();

        String[] REQUIRED_PERMISSIONS;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            REQUIRED_PERMISSIONS = new String[] { Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_AUDIO};
        }else{
            REQUIRED_PERMISSIONS = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        }

        mPermUtil = new PermissionUtil(MainActivity.this, REQUIRED_PERMISSIONS);
        mPermUtil.onSetPermission();

        mProgress = new ProgressDialog(this);
        //mTimer = new Timer();
        //initEventInfo();

        IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadCompleteReceiver, completeFilter, RECEIVER_EXPORTED );

        Intent servIntent = new Intent(getApplicationContext(), MusicPlayService.class);
        getApplicationContext().startService(servIntent);


        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            registerReceiver(mVolumeChangeListener, filter, Context.RECEIVER_EXPORTED);
        else
            registerReceiver(mVolumeChangeListener, filter);
    }

    public String getSSAID()
    {
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initEventInfo();
        isFinish = false;

        if(mRunEventId > 0)
        {
            //mTimer.schedule(timerTask, 0, 1000);
            mServer.getRunEventState(mFirstEventStateCallBack, mRunEventId);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicPlayService.ACTION_PLAY_START_RESULT);
        filter.addAction(MusicPlayService.ACTION_UPDATE_CURRENT_POSITION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFinish = true;

        if(mTimer != null)
        {
            mTimer.cancel();
            mTimer = null;
        }

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadCompleteReceiver);


        if(mTimer != null)
        {
            mTimer.cancel();
            mTimer = null;
        }

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

        Intent intent = new Intent(MusicPlayService.ACTION_PLAY_STOP);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);

        unregisterReceiver(mVolumeChangeListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MusicPlayService.ACTION_PLAY_STOP);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        try {
            if(mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            if(mDefaultMediaPlayer != null && mDefaultMediaPlayer.isPlaying()){
                mDefaultMediaPlayer.stop();
                mDefaultMediaPlayer.release();
                mDefaultMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public Uri getContentUri(String name){
        File outputFile = new File(contentDirPath+name);
//        if (!outputFile.getParentFile().exists()) {
//            outputFile.getParentFile().mkdirs();
//        }
        if(outputFile.exists())
            return Uri.fromFile(outputFile);
        else
            return null;
    }

    public String getTypeImage(ArrayList<EventImageDto> imageDtos, String typeStr){
        if(imageDtos == null)
            return null;
        String result = null;
        for(EventImageDto imageDto : imageDtos){
            if(imageDto.getImageType().equalsIgnoreCase(typeStr)){
                result = imageDto.getImageName();
                break;
            }
        }
        return result;
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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
//                || keyCode == KeyEvent.KEYCODE_DPAD_UP  || keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_CENTER
//                || keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_RECENT_APPS
//        )
//            return super.onKeyDown(keyCode, event);
//
//        boolean isVolumeSync = mPreferenceUtil.getBooleanPreference(PreferenceUtil.VOLUME_SYNC);
//        if(isVolumeSync)
//        {
//            int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//            if(keyCode == KeyEvent.KEYCODE_M || keyCode == KeyEvent.KEYCODE_MUTE)
//            {
//                if(volume > 0)
//                {
//                    volume = 0;
//                }
//                else
//                {
//                    volume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2;
//                }
//                VolumeDto volumeDto = new VolumeDto(mServerId, volume);
//                mServer.setSyncVolume(mVolumeCallBack, volumeDto);
//                return super.onKeyDown(keyCode, event);
//            }
//            else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN )
//            {
//                VolumeDto volumeDto = new VolumeDto(mServerId, volume);
//                mServer.setSyncVolume(mVolumeCallBack, volumeDto);
//
//                eventFragment.updateVolumeSeekbar(volume);
//                return super.onKeyDown(keyCode, event);
//            }
//        }
//
//        if(settingFragment.mEventIsRunning){
//            settingFragment.eventStop();
//        }
//        else{
//            settingFragment.eventStart();
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                || keyCode == KeyEvent.KEYCODE_DPAD_UP  || keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                || keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_RECENT_APPS
        )
            return super.onKeyUp(keyCode, event);

        try{Thread.sleep(100);}catch (Exception e){}
        boolean isVolumeSync = mPreferenceUtil.getBooleanPreference(PreferenceUtil.VOLUME_SYNC);
        if(isVolumeSync)
        {
            int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if(keyCode == KeyEvent.KEYCODE_M || keyCode == KeyEvent.KEYCODE_MUTE)
            {
                if(volume > 0)
                {
                    volume = 0;
                }
                else
                {
                    volume = 7;
                }
                eventFragment.updateVolumeSeekbar(volume);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
//                VolumeDto volumeDto = new VolumeDto(mServerId, volume);
//                mServer.setSyncVolume(mVolumeCallBack, volumeDto);
                return super.onKeyUp(keyCode, event);
            }
            else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN )
            {
//                VolumeDto volumeDto = new VolumeDto(mServerId, volume);
//                mServer.setSyncVolume(mVolumeCallBack, volumeDto);

                eventFragment.updateVolumeSeekbar(volume);

                return super.onKeyUp(keyCode, event);
            }
        }

        if(settingFragment.mEventIsRunning){
            settingFragment.eventStop();
        }
        else{
            settingFragment.eventStart();
        }
        return super.onKeyUp(keyCode, event);
    }

    public void initEventInfo()
    {
        if(mServerId > 0)
        {
            showProgress(MainActivity.this, true);
            mServer.getEvent(mEventCallBack, mServerId);
        }
        else{
            bottomNavigationView.setSelectedItemId(R.id.tab_setting);
        }
    }

//    TimerTask timerTask = new EventTimerTask() {
//        @Override
//        public void run() {
//            mServer.getRunEventState(mEventStateCallBack, mEventId);
//        }
//    };

    public void startEventStateCheck(long eventId)
    {
        mRunEventId = (int)eventId;
        if(mTimer != null)
        {
            mTimer.cancel();
            mTimer = null;
        }
        mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
//                if(mRunEvent != null && mRunEvent.getId() == mRunEventId)
//                {
//                    if(mRunEvent.getEventState().equalsIgnoreCase("STOP"))
//                    {
//                        mTimer.cancel();
//                        mTimer = null;
//
//                        Log.d("AAAA", "contype : " + mEventDto.getContinuityType());
//                        if(mEventDto.getContinuityType() == 1 && mEventRepeat)
//                        {
//                            long delayTime = mEventDto.getContinuityTime() * 1000;
//                            Log.d("AAAA", "delayTime : " + delayTime);
//                            runOnUiThread(() -> {
//                                new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
//                                    @Override
//                                    public void run() {
//                                        //mHandler.sendEmptyMessage(0);	// 실행이 끝난후 알림
//                                        EventStartReqDto reqDto = new EventStartReqDto(mServerId, -1, -1, -1, -1, -1);
//                                        mServer.eventStart(mEventStartCallBack, reqDto);
//                                    }
//                                }, delayTime);
//                            });
//                        }
//                        return;
//                    }
//                    else if(mRunEvent.getEventState().equalsIgnoreCase("START"))
//                    {
//
//                    }
//                }
                mServer.getRunEventState(mEventStateCallBack, mRunEventId);
            }
        };
        mTimer.schedule(timerTask, 0, 1000);

    }

    private void SettingListener() {
        //선택 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new TabSelectedListener());
    }

    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.tab_home_setting) {
                fm.beginTransaction().hide(active).show(homeSettingFragment).commit();
                active = homeSettingFragment;
                return true;
            } else if (itemId == R.id.tab_away_setting) {
                fm.beginTransaction().hide(active).show(awaySettingFragment).commit();
                active = awaySettingFragment;
                return true;
            } else if (itemId == R.id.tab_setting) {
                fm.beginTransaction().hide(active).show(settingFragment).commit();
                active = settingFragment;
                return true;
            } else if (itemId == R.id.tab_event) {
                fm.beginTransaction().hide(active).show(eventFragment).commit();
                active = eventFragment;
                return true;
            }
            return false;
        }
    }

    private void startDefaultMediaPlayer()
    {
        try{
            if(mDefaultMediaPlayer == null) {
                Uri uri = Uri.parse(mStrDefault);
                if(mDefaultMediaPlayer == null)
                {
                    mDefaultMediaPlayer = new MediaPlayer();
                }
                mDefaultMediaPlayer.setDataSource(getApplicationContext(), uri);
                mDefaultMediaPlayer.prepare();
                mDefaultMediaPlayer.setLooping(true);
                mDefaultMediaPlayer.start();
            }else {
//                if(mDefaultMediaPlayer.isPlaying()){
//                    mDefaultMediaPlayer.seekTo(psusePos);
//                    mDefaultMediaPlayer.start();
//                }
                mDefaultMediaPlayer.seekTo(psusePos);
                mDefaultMediaPlayer.start();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
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
//                try{
//                    Uri uri = Uri.parse(mStrDefault);
////                    mDefaultMediaPlayer.setScreenOnWhilePlaying(true);
////                    mDefaultMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.ON_AFTER_RELEASE);
//                    if(mDefaultMediaPlayer == null)
//                    {
//                        mDefaultMediaPlayer = new MediaPlayer();
//                    }
//                    mDefaultMediaPlayer.setDataSource(getApplicationContext(), uri);
//                    mDefaultMediaPlayer.prepare();
//                    mDefaultMediaPlayer.setLooping(true);
//                    mDefaultMediaPlayer.start();
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }

//                startDefaultMediaPlayer();
//
//                if(mTimer != null)
//                {
//                    mTimer.cancel();
//                    mTimer = null;
//                }
//                mTimer = new Timer();
//                TimerTask timerTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        Date currentDate = Calendar.getInstance().getTime();
//                        long diffTime = endDate.getTime() - currentDate.getTime();
//                        if(diffTime > 0){
//                            if(eventFragment != null){
//                                eventFragment.updateTimer();
//                            }
//                        }
//                        else {
//                            mTimer.cancel();
//                            mTimer = null;
//                            if(settingFragment != null){
//                                settingFragment.updateEventState(false);
//                            }
//                            //int serverId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_SERVER_ID, -1);
//                            Log.d("AAAA", "serverid : " + mServerId);
//                            //mServer.getLastEvent(mEventStateCallBack, mServerId);
//                        }
//                    }
//                };
//                mTimer.schedule(timerTask, 0, 1000);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getHomeMusic(RunEvent runEvent){
        int max = runEvent.getHome1Count();
        String name = homeSettingFragment.mTextHome1.getText().toString();
        if(max < runEvent.getHome2Count()){
            max = runEvent.getHome2Count();
            name = homeSettingFragment.mTextHome2.getText().toString();
        }
        if(max < runEvent.getHome3Count()){
            max = runEvent.getHome3Count();
            name = homeSettingFragment.mTextHome3.getText().toString();
        }
        if(max < runEvent.getHome4Count()){
            max = runEvent.getHome4Count();
            name = homeSettingFragment.mTextHome4.getText().toString();
        }
        if(max < runEvent.getHome5Count()){
            max = runEvent.getHome5Count();
            name = homeSettingFragment.mTextHome5.getText().toString();
        }
        return name;
    }

    public String getAwayMusic(RunEvent runEvent){
        int max = runEvent.getAway1Count();
        String name = awaySettingFragment.mTextAway1.getText().toString();
        if(max < runEvent.getAway2Count()){
            max = runEvent.getAway2Count();
            name = awaySettingFragment.mTextAway2.getText().toString();
        }
        if(max < runEvent.getAway3Count()){
            max = runEvent.getAway3Count();
            name = awaySettingFragment.mTextAway3.getText().toString();
        }
        if(max < runEvent.getAway4Count()){
            max = runEvent.getAway4Count();
            name = awaySettingFragment.mTextAway4.getText().toString();
        }
        if(max < runEvent.getAway5Count()){
            max = runEvent.getAway5Count();
            name = awaySettingFragment.mTextAway5.getText().toString();
        }
        return name;
    }

    public void playVideo(RunEvent runEvent){
        try{
            String strUri = null;
            if(runEvent.getHomeCount() >= runEvent.getAwayCount())
            {
                strUri = getHomeMusic(runEvent);
            }
            else
            {
                strUri = getAwayMusic(runEvent);
            }

//            if(!isFinish)
//                eventFragment.playVideo(strUri);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void playMusic(RunEvent runEvent){
        try{
            String strUri = null;

//            if(runEvent.getHomeCount() >= runEvent.getAwayCount())
//            {
//                strUri = getHomeMusic(runEvent);
//            }
//            else
//            {
//                strUri = getAwayMusic(runEvent);
//            }
//
//            eventFragment.playVideo(strUri);

            Intent intent = new Intent(MusicPlayService.ACTION_PLAY_START);
            intent.putExtra(MusicPlayService.EXTRA_FILE_URL, strUri);
            intent.putExtra(MusicPlayService.EXTRA_IS_NEW, true);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stopMusic()
    {
        try {
            if(mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            if(mDefaultMediaPlayer != null && mDefaultMediaPlayer.isPlaying()){
                mDefaultMediaPlayer.stop();
                mDefaultMediaPlayer.release();
                mDefaultMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePlayPos(int pos)
    {
        Intent intent = new Intent(MusicPlayService.ACTION_PLAY_MOVE_POS);
        intent.putExtra(MusicPlayService.EXTRA_UPDATE_POSITION, pos);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

    }

    public void updateVolume(RunEvent runEvent)
    {
        boolean isSync = mPreferenceUtil.getBooleanPreference(PreferenceUtil.VOLUME_SYNC);
        if(isSync)
        {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, runEvent.getVolumeValue(), 0);
        }
    }

    public void setVolume(int value)
    {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value, AudioManager.FLAG_SHOW_UI);
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

    private SECallBack<RunEventResult> mEventStateCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                try{
                    mRunEvent = response.body().getData();

                    settingFragment.updateEventState(mRunEvent);
                    eventFragment.updateEventState(mRunEvent);
                    //updateVolume(mRunEvent);

                    if(mRunEvent.getEventState().equalsIgnoreCase("STOP")){
                        //playMusic(mRunEvent);
                        playVideo(mRunEvent);
                        mRunEventId = -1;
                        AsyncCheckState async = new AsyncCheckState();
                        async.execute();
                    }
//                    if(runEvent.getHomeCount() >= runEvent.getAwayCount())
//                    {
//                        if(runEvent.getHome1Count() > runEvent.getHome2Count())
//                            strUri = mStrHome1;
//                        else
//                            strUri = mStrHome2;
//                    }
//                    else
//                    {
//                        if(runEvent.getAway1Count() > 0)
//                            strUri = mStrAway1;
//                        else
//                            strUri = mStrAway2;
//                    }
//
//                    if(strUri != null && strUri.length() > 0)
//                    {
//                        if(mDefaultMediaPlayer != null && mDefaultMediaPlayer.isPlaying()){
//                            psusePos = mDefaultMediaPlayer.getCurrentPosition();
//                            mDefaultMediaPlayer.pause();
//                        }
//
//                        Uri uri = Uri.parse(strUri);
//                        if(mMediaPlayer == null)
//                            mMediaPlayer = new MediaPlayer();
//                        mMediaPlayer.setDataSource(getApplicationContext(), uri);
//                        mMediaPlayer.prepare();
//                        mMediaPlayer.start();
//                        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//                                mMediaPlayer.release();
//                                mMediaPlayer = null;
//                                startDefaultMediaPlayer();
//                            }
//                        });
//                    }
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


    private SECallBack<RunEventResult> mFirstEventStateCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                try{
                    mRunEvent = response.body().getData();

                    settingFragment.updateEventState(mRunEvent);
                    eventFragment.updateEventState(mRunEvent);
                    //updateVolume(mRunEvent);

                    if(mRunEvent.getEventState().equalsIgnoreCase("START")){
                        startEventStateCheck(mRunEvent.getId());
                    }else{
                        AsyncCheckState async = new AsyncCheckState();
                        async.execute();
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

    /// V2
    private DownloadManager mDownloadManager;
    private Long mDownloadQueueId;
    ArrayList<DownloadItem> mDownloadList = new ArrayList<>();
    private int mDownloadPos = 0;

    private void startDownload(int position){
        if(mDownloadList.size() > position){
            DownloadItem item = mDownloadList.get(position);
            if(isExistFile(item.getName())){
                startDownload(position+1);
            }else{
                Uri uri = Uri.parse(item.getUrl());
                downloadRequest(uri, item.getName());
                mDownloadPos = position;
            }
        }
    }

    private void downloadRequest(Uri url, String name) {
        if (mDownloadManager == null) {
            mDownloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        }
        File outputFile = new File(contentDirPath+name);
//        if (!outputFile.getParentFile().exists()) {
//            outputFile.getParentFile().mkdirs();
//        }

        Uri downloadUri = url;
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        List<String> pathSegmentList = downloadUri.getPathSegments();
        request.setTitle("다운로드 항목");
        request.setDestinationUri(Uri.fromFile(outputFile));
        request.setAllowedOverMetered(true);

        mDownloadQueueId = mDownloadManager.enqueue(request);
    }

    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if(mDownloadQueueId == reference){
                DownloadManager.Query query = new DownloadManager.Query();  // 다운로드 항목 조회에 필요한 정보 포함
                query.setFilterById(reference);
                Cursor cursor = mDownloadManager.query(query);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);

                int status = cursor.getInt(columnIndex);
                int reason = cursor.getInt(columnReason);

                cursor.close();

                switch (status) {
                    case DownloadManager.STATUS_SUCCESSFUL :
                        //Toast.makeText(mContext, "다운로드를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                        // next download start
                        startDownload(mDownloadPos+1);
                        break;

                    case DownloadManager.STATUS_PAUSED :
                        //Toast.makeText(mContext, "다운로드가 중단되었습니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case DownloadManager.STATUS_FAILED :
                        //Toast.makeText(mContext, "다운로드가 취소되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };

    private boolean isExistFile(String name){
        File file = new File(contentDirPath);
        if(!file.exists())
        {
            file.mkdirs();
            return false;
        }

        String files[] = file.list();
        boolean result = false;
        for(String fileName : files){
            if(name.equalsIgnoreCase(fileName)){
                result = true;
                break;
            }
        }

        return result;
    }

    public void updateEventButton()
    {
        this.settingFragment.setEventBtnState(true);
    }

    public void updateEventInfo(RunEvent runEvent)
    {
        homeSettingFragment.setEventInfo(runEvent.getEventMusicList());
        awaySettingFragment.setEventInfo(runEvent.getEventMusicList());
        settingFragment.updateEventInfo(runEvent);
        eventFragment.updateEventInfo(runEvent);
    }

    private void setEventInfo(EventDto eventDto)
    {
//        homeSettingFragment.setEventInfo(eventDto);
//        awaySettingFragment.setEventInfo(eventDto);
        homeSettingFragment.setEventInfo(eventDto.getEventMusicList());
        homeSettingFragment.setEventColor(eventDto.getHomeColor(), eventDto.getHomeFont());
        awaySettingFragment.setEventInfo(eventDto.getEventMusicList());
        awaySettingFragment.setEventColor(eventDto.getAwayColor(), eventDto.getAwayFont());
        settingFragment.setEventInfo(eventDto);
        eventFragment.setEventInfo(eventDto);
        //eventFragment.setEventColor(eventDto.getHomeColor(), eventDto.getAwayColor());
        eventFragment.setEventColor(eventDto.getHomeColor(), eventDto.getHomeFont(),
                eventDto.getAwayColor(), eventDto.getAwayFont());
    }

    private BroadcastReceiver mVolumeChangeListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            intent.getExtras();
            if(action.equalsIgnoreCase("android.media.VOLUME_CHANGED_ACTION"))
            {
                boolean isVolumeSync = mPreferenceUtil.getBooleanPreference(PreferenceUtil.VOLUME_SYNC);
                int streamType = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
                int newVolume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", -1);
                int oldVolume = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", -1);
                if(streamType == AudioManager.STREAM_MUSIC && newVolume != oldVolume && isVolumeSync)
                {
                    VolumeDto volumeDto = new VolumeDto(mServerId, newVolume);
                    mServer.setSyncVolume(mVolumeCallBack, volumeDto);
                    //eventFragment.updateVolumeSeekbar(newVolume);
                }
                //Toast.makeText(context, "볼륨 변경: " + oldVolume + " → " + newVolume, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private SECallBack<EventResult> mEventCallBack = new SECallBack<EventResult>()
    {
        @Override
        public void onResponseResult(Response<EventResult> response)
        {
            if (response.isSuccessful())
            {
                mDownloadList.clear();

                mEventDto = response.body().getData();
                ArrayList<EventMusicDto> musicList = mEventDto.getEventMusicList();
                if(musicList != null && musicList.size() > 0){
                    List<DownloadItem> downloadItems = musicList.stream()
                            .map(x -> new DownloadItem(x.getMusicName(), x.getMusicUrl()))
                            .collect(Collectors.toList());

                    mDownloadList.addAll(downloadItems);
                }

                ArrayList<EventImageDto> imageList = mEventDto.getEventImageList();
                if(imageList != null && imageList.size() > 0){
                    List<DownloadItem> downloadItems = imageList.stream()
                            .map(x -> new DownloadItem(x.getImageName(), x.getImageUrl()))
                            .collect(Collectors.toList());

                    mDownloadList.addAll(downloadItems);
                }

                if(mDownloadList != null && mDownloadList.size() > 0)
                {
                    startDownload(0);
                }
                setEventInfo(mEventDto);
                mServer.getRunEventState(mFirstEventStateCallBack, mEventDto.getRunEvent());
            }
            else
            {
                // no event
            }
            showProgress(MainActivity.this, false);
        }
    };

    private SECallBack<RunEventResult> mEventStartCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful()) {
                mRunEvent = response.body().getData();

                settingFragment.updateEventState(mRunEvent);
                eventFragment.updateEventState(mRunEvent);

                updateEventInfo(mRunEvent);

                startEventStateCheck(mRunEvent.getId());
                //Toast.makeText(getApplicationContext(), "이벤트 를 시작 하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.d("AAAA", "----- Start Event fail : " );
            }
        }
    };

    private SECallBack<EventResult> mVolumeCallBack = new SECallBack<EventResult>()
    {
        @Override
        public void onResponseResult(Response<EventResult> response)
        {

        }
    };

    private class AsyncCheckState extends AsyncTask<String, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(String... strings) {
            try{
                Thread.sleep(CHECK_DELAY);
            }catch (Exception e){}

            //mServer.getLastEvent(mFirstEventStateCallBack, mServerId);

            if(mRunEventId < 0)
            {
                //mServer.getRunEventState(mFirstEventStateCallBack, mRunEventId);

                if(!isFinish)
                    mServer.getLastEvent(mFirstEventStateCallBack, mServerId);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equalsIgnoreCase(MusicPlayService.ACTION_PLAY_START_RESULT))
            {

            }
            else if(action.equalsIgnoreCase(MusicPlayService.ACTION_UPDATE_CURRENT_POSITION))
            {
                String title = intent.getStringExtra(MusicPlayService.EXTRA_MUSIC_TITLE);
                int total = intent.getIntExtra(MusicPlayService.EXTRA_TOTAL_DURATION, 0);
                int current = intent.getIntExtra(MusicPlayService.EXTRA_CURRENT_DURATION, 0);
//                long total = intent.getLongExtra(MusicPlayService.EXTRA_TOTAL_DURATION, 0L);
//                long current = intent.getLongExtra(MusicPlayService.EXTRA_CURRENT_DURATION, 0L);
                eventFragment.updatePlayState(title, total, current);


//                if(mEventDto.getContinuityType() == 1 )
//                {
//                    int diff = (total - current) / 1000;
//                    Log.d("AAAA", "total: "+total+" current: "+current+ " diff: " + diff);
//                    Log.d("AAAA", "getTriggerTime: "+mRunEvent.getTriggerTime());
//
//                    if(diff < mRunEvent.getTriggerTime() && mRunEvent.getEventState().equalsIgnoreCase("STOP"))
//                    {
//                        Log.d("AAAA", "getTriggerTime:--- start event ");
//                        EventStartReqDto reqDto = new EventStartReqDto(mServerId, -1, -1, -1, -1, -1, volumeValue);
//                        mServer.eventStart(mEventStartCallBack, reqDto);
//                        mRunEvent.setEventState("RESTART");
//                        // button state change
//                        updateEventButton();
//                    }
//                }
            }
            else if(action.equalsIgnoreCase(MusicPlayService.ACTION_PLAY_MOVE_POS))
            {
                int pos = intent.getIntExtra(MusicPlayService.EXTRA_UPDATE_POSITION, 0);
                if(pos > 0){

                }
            }
        }
    };


}