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
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
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
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private HomeSettingFragment homeSettingFragment;
    private AwaySettingFragment awaySettingFragment;
    private EventFragment eventFragment;
    private SettingFragment settingFragment;
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
//    public String mStrWebUrl;

    public boolean mWebViewState = false;

    protected ProgressDialog mProgress = null;

    public int mServerId;
    public int mRunEventId = -1;
    private Timer mTimer;

    public EventDto mEventDto = null;
    public RunEvent mRunEvent = null;
    public boolean mEventRepeat = false;

    public static String contentDirPath = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS) + "/StadiumAmp/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mPermUtil = new PermissionUtil(MainActivity.this, REQUIRED_PERMISSIONS);
        mPermUtil.onSetPermission();

        mProgress = new ProgressDialog(this);
        //mTimer = new Timer();
        initEventInfo();

        IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadCompleteReceiver, completeFilter);

        Intent servIntent = new Intent(getApplicationContext(), MusicPlayService.class);
        getApplicationContext().startService(servIntent);
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
        if(mRunEventId > 0)
        {
            //mTimer.schedule(timerTask, 0, 1000);
            //startEventStateCheck(mRunEventId);
            mServer.getRunEventState(mFirstEventStateCallBack, mRunEventId);
            //mServer.getEvent(mResumeEventCallBack, mRunEventId);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mTimer != null)
        {
            mTimer.cancel();
            mTimer = null;
        }
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
                if(mRunEvent != null && mRunEvent.getId() == mRunEventId)
                {
                    if(mRunEvent.getEventState().equalsIgnoreCase("STOP"))
                    {
                        mTimer.cancel();
                        mTimer = null;

                        Log.d("AAAA", "contype : " + mEventDto.getContinuityType());
                        if(mEventDto.getContinuityType() == 1 && mEventRepeat)
                        {
                            long delayTime = mEventDto.getContinuityTime() * 1000;
                            Log.d("AAAA", "delayTime : " + delayTime);
                            runOnUiThread(() -> {
                                new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                                    @Override
                                    public void run() {
                                        //mHandler.sendEmptyMessage(0);	// 실행이 끝난후 알림
                                        EventStartReqDto reqDto = new EventStartReqDto(mServerId, -1, -1, -1, -1, -1);
                                        mServer.eventStart(mEventStartCallBack, reqDto);
                                    }
                                }, delayTime);
                            });
//                            new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
//                                @Override
//                                public void run() {
//                                    //mHandler.sendEmptyMessage(0);	// 실행이 끝난후 알림
//                                    EventStartReqDto reqDto = new EventStartReqDto(mServerId, -1, -1, -1, -1, -1);
//                                    mServer.eventStart(mEventStartCallBack, reqDto);
//                                }
//                            }, delayTime);

//                            Thread.sleep();
//
//                            mTimerTask = new EventTimerTask();
//                            long delayTime = mEventDto.getContinuityTime() * 1000;
//                            mTimer.schedule(mTimerTask, delayTime, 1000);

                        }
                        return;
                    }
                }
                mServer.getRunEventState(mEventStateCallBack, mRunEventId);
            }
        };
        mTimer.schedule(timerTask, 0, 1000);

//        mTimerTask = new EventTimerTask();
//        mTimer.schedule(mTimerTask, 0, 1000);
    }

    private void SettingListener() {
        //선택 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new TabSelectedListener());
    }

    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.tab_home_setting: {
                    fm.beginTransaction().hide(active).show(homeSettingFragment).commit();
                    active = homeSettingFragment;
                    return true;
                }
                case R.id.tab_away_setting: {
                    fm.beginTransaction().hide(active).show(awaySettingFragment).commit();
                    active = awaySettingFragment;
                    return true;
                }
                case R.id.tab_setting: {
                    fm.beginTransaction().hide(active).show(settingFragment).commit();
                    active = settingFragment;
                    return true;
                }
                case R.id.tab_event: {
                    fm.beginTransaction().hide(active).show(eventFragment).commit();
                    active = eventFragment;
                    return true;
                }
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

    public void playMusic(RunEvent runEvent){
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

            Intent intent = new Intent(MusicPlayService.ACTION_PLAY_START);
            intent.putExtra(MusicPlayService.EXTRA_FILE_URL, strUri);
            intent.putExtra(MusicPlayService.EXTRA_IS_NEW, true);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);


//            if(strUri != null && strUri.length() > 0)
//            {
//                if(mDefaultMediaPlayer != null && mDefaultMediaPlayer.isPlaying()){
//                    psusePos = mDefaultMediaPlayer.getCurrentPosition();
//                    mDefaultMediaPlayer.pause();
//                }
//
//                if(mMediaPlayer == null)
//                    mMediaPlayer = new MediaPlayer();
//
//                //strUri = "도시인.mp3";
//                Uri uri = getContentUri(strUri);
//                mMediaPlayer.setDataSource(getApplicationContext(), uri);
//
//                mMediaPlayer.prepare();
//                mMediaPlayer.start();
//                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        mMediaPlayer.release();
//                        mMediaPlayer = null;
//                        startDefaultMediaPlayer();
//                    }
//                });
//            }
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

                    if(mRunEvent.getEventState().equalsIgnoreCase("STOP")){
                        playMusic(mRunEvent);
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
                    if(mRunEvent.getEventState().equalsIgnoreCase("START")){
                        startEventStateCheck(mRunEvent.getId());
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

    private SECallBack<EventResult> mResumeEventCallBack = new SECallBack<EventResult>()
    {
        @Override
        public void onResponseResult(Response<EventResult> response)
        {
            if (response.isSuccessful())
            {
                mEventDto = response.body().getData();

                if(mEventDto.getEventState().equalsIgnoreCase("START"))
                {
                    startEventStateCheck(mRunEventId);
                }
                else
                {
                    //setImageView01();

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
        eventFragment.setEventColor(eventDto.getHomeColor(), eventDto.getAwayColor());
    }

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
                //startEventStateCheck(mEventDto.getRunEvent());
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
                Toast.makeText(getApplicationContext(), "이벤트 를 시작 하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.d("AAAA", "----- Start Event fail : " );
            }
        }
    };

}