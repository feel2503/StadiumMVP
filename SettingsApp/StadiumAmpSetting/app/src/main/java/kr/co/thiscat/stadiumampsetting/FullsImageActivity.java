package kr.co.thiscat.stadiumampsetting;

import android.app.Activity;
import android.app.PictureInPictureParams;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.Rational;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.thiscat.stadiumampsetting.databinding.ActivityFullsImageBinding;
import kr.co.thiscat.stadiumampsetting.server.SECallBack;
import kr.co.thiscat.stadiumampsetting.server.ServerManager;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEvent;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEventResult;
import kr.co.thiscat.stadiumampsetting.server.entity.result.EventResult;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventImageDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventMusicDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventStartReqDto;
import retrofit2.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullsImageActivity extends AppCompatActivity {
    private View mControlsView;

    public String contentDirPath = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS) + "/StadiumAmp/";

    private ActivityFullsImageBinding binding;
    private ServerManager mServer;
    public int mRunEventId = -1;
    public int mServerId;
    public boolean mEventRepeat;
    protected ProgressDialog mProgress = null;

    private ImageView mImageFull;
    private LinearLayout mLinearHalf;

    private ImageView mImgHalf;
    private LinearLayout mLinearHalfResult;
    private LinearLayout mLinearHalfJoin;

    private TextView mTextResult1;
    private TextView mTextResult2;
    private TextView mTextResult3;

    private TextView mTextCurrent;
    private TextView mTextHomeName;
    private TextView mTextAwayName;
    private TextView mTextHome;
    private TextView mTextAway;

    public EventDto mEventDto = null;
    private RunEvent mRunEvent;

    private boolean isRunning = false;
    private Timer mTimer;

    private String viewImageType = "IMAGE_DEFAULT";

    public String mTextHome1;
    public String mTextHome2;
    public String mTextHome3;
    public String mTextHome4;
    public String mTextHome5;
    public String mTextAway1;
    public String mTextAway2;
    public String mTextAway3;
    public String mTextAway4;
    public String mTextAway5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        binding = ActivityFullsImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mControlsView = binding.fullscreenContentControls;
        hide();

        mProgress = new ProgressDialog(getApplicationContext());

        mServerId = getIntent().getIntExtra("RunServerID", -1);
        getIntent().getBooleanExtra("EventRepeat", false);
        mServer = ServerManager.getInstance(FullsImageActivity.this);
        initUi();

        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicPlayService.ACTION_PLAY_START_RESULT);
        filter.addAction(MusicPlayService.ACTION_UPDATE_CURRENT_POSITION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, filter);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            PictureInPictureParams.Builder pipBuilder = new PictureInPictureParams.Builder();
//            enterPictureInPictureMode(pipBuilder.build());
//        }

    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        if (isInPictureInPictureMode) {
            Toast.makeText(this, "PIP Mode", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "not PIP Mode", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUi()
    {
        mImageFull = (ImageView) findViewById(R.id.image_full_view);
        mLinearHalf = findViewById(R.id.linear_half_content);

        mImgHalf = findViewById(R.id.image_half_view);
        mLinearHalfResult = findViewById(R.id.linear_result);
        mLinearHalfJoin = findViewById(R.id.linear_join);

        mTextResult1 = findViewById(R.id.text_result_01);
        mTextResult2 = findViewById(R.id.text_result_02);
        mTextResult3 = findViewById(R.id.text_result_03);

        mTextCurrent = findViewById(R.id.text_current_server);
        mTextHomeName = findViewById(R.id.text_result_home_count);
        mTextAwayName = findViewById(R.id.text_result_away_count);
        mTextHome = findViewById(R.id.text_result_home);
        mTextAway = findViewById(R.id.text_result_away);

        findViewById(R.id.screen_full_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
//        showProgress(FullsImageActivity.this, true);
        mServer.getEvent(mEventCallBack, mServerId);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams.Builder pipBuilder = new PictureInPictureParams.Builder();
//            Rational aspectRatio = new Rational(16, 9);
//            pipBuilder.setAspectRatio(aspectRatio).build();
            enterPictureInPictureMode(pipBuilder.build());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    private void updateViewState(int state)
    {
        if(state == 1 || state == 2)
        {
            mLinearHalf.setVisibility(View.VISIBLE);
            mImageFull.setVisibility(View.GONE);
            mImgHalf.setVisibility(View.VISIBLE);
            if(state == 1)
            {
                mLinearHalfResult.setVisibility(View.VISIBLE);
                mLinearHalfJoin.setVisibility(View.GONE);
            }
            else if(state == 2)
            {
                mLinearHalfResult.setVisibility(View.GONE);
                mLinearHalfJoin.setVisibility(View.VISIBLE);
            }
        }
        else if(state == 3 || state == 4)
        {
            mLinearHalf.setVisibility(View.GONE);
            mImageFull.setVisibility(View.VISIBLE);
        }
    }


    private void setImageView01()
    {

        updateViewState(1);

        String defImage = getTypeImage(mEventDto.getEventImageList(), viewImageType);
        if(defImage != null && isRunning)
        {
            //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
            Uri uri = getContentUri(defImage);
            mImgHalf.setImageURI(uri);
            Glide.with(this).load(uri).into(mImgHalf);
        }
        if(isRunning){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isRunning)
                        setImageView02();
                }
            }, 5000);
        }
    }
    private void setImageView02()
    {
        Log.d("AAAA", "setImageView02 : ");
        updateViewState(2);
        String defImage = getTypeImage(mEventDto.getEventImageList(), "IMAGE_QR");
        if(defImage != null && isRunning)
        {
            //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
            Uri uri = getContentUri(defImage);
            mImgHalf.setImageURI(uri);
            Glide.with(this).load(uri).into(mImgHalf);
        }
        if(isRunning){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isRunning)
                        setImageView03();
                }
            }, 5000);
        }
    }
    private void setImageView03()
    {
        Log.d("AAAA", "setImageView03 : ");
        updateViewState(3);
        mImageFull.setScaleType(ImageView.ScaleType.FIT_CENTER);

        String defImage = getTypeImage(mEventDto.getEventImageList(), viewImageType);
        if(defImage != null && isRunning)
        {
            //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
            Uri uri = getContentUri(defImage);
            mImageFull.setImageURI(uri);
            Glide.with(this).load(uri).into(mImageFull);
        }
        if(isRunning){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setImageView04();
                }
            }, 15000);
        }
    }
    private void setImageView04()
    {
        Log.d("AAAA", "setImageView04 : ");
        updateViewState(4);
        mImageFull.setScaleType(ImageView.ScaleType.FIT_CENTER);
        String defImage = getTypeImage(mEventDto.getEventImageList(), "IMAGE_ADV");
        if(defImage != null && isRunning)
        {
            //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
            Uri uri = getContentUri(defImage);
            mImageFull.setImageURI(uri);
            Glide.with(this).load(uri).into(mImageFull);
        }

        if(isRunning){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mImageFull.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    String defImage = getTypeImage(mEventDto.getEventImageList(), viewImageType);
                    if(defImage != null && isRunning)
                    {
                        //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
                        Uri uri = getContentUri(defImage);
                        mImageFull.setImageURI(uri);
                        Glide.with(FullsImageActivity.this).load(uri).into(mImageFull);
                    }
                }
            }, 5000);
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        if (Build.VERSION.SDK_INT >= 30) {
            mControlsView.getWindowInsetsController().hide(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mControlsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }


    public Uri getContentUri(String name){
        File outputFile = new File(contentDirPath+name);
//        if (!outputFile.getParentFile().exists()) {
//            outputFile.getParentFile().mkdirs();
//        }

        Uri contUri  = Uri.fromFile(outputFile);
        return contUri;
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


    public void startEventStateCheck(long eventId)
    {
        updateViewState(1);
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
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                mTimer.cancel();
                                mTimer = null;

                                setImageView01();
                            }
                        });
                        return;
                    }
                    else if(mRunEvent.getEventState().equalsIgnoreCase("START"))
                    {

                    }
                }
                if( isRunning)
                {
                    Log.d("AAAA", "--- 111mRunEventId : " + mRunEventId);
                    mServer.getRunEventState(mEventStateCallBack, mRunEventId);
                }
            }
        };
        mTimer.schedule(timerTask, 0, 1000);

    }

    private void updateScore(RunEvent runEvent)
    {
        try{
            int home = runEvent.getHomeCount();
            int away = runEvent.getAwayCount();
            updateScoreValue(runEvent, home, away);
            updateMusicRanking(runEvent);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void updateMusicRanking(RunEvent runEvent)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run() {
                if(runEvent.getHomeCount() >= runEvent.getAwayCount()){
                    int[] nums = {runEvent.getHome1Count(), runEvent.getHome2Count(), runEvent.getHome3Count(), runEvent.getHome4Count(), runEvent.getHome5Count()};
                    int[] ranks = getRank(nums);

                    mTextResult1.setText(getHomeRankMusic(ranks, 1));
                    mTextResult2.setText(getHomeRankMusic(ranks, 2));
                    mTextResult3.setText(getHomeRankMusic(ranks, 3));
                }
                else
                {
                    int[] nums = {runEvent.getAway1Count(), runEvent.getAway2Count(), runEvent.getAway3Count(), runEvent.getAway4Count(), runEvent.getAway5Count()};
                    int[] ranks = getRank(nums);

                    mTextResult1.setText(getAwayRankMusic(ranks, 1));
                    mTextResult2.setText(getAwayRankMusic(ranks, 2));
                    mTextResult3.setText(getAwayRankMusic(ranks, 3));
                }
            }
        });

    }

    private int[] getRank(int[] nums)
    {
        int[] ranks = new int[5];
        for (int i = 0; i < 5; i++) {
            int rank = 1;
            for (int j = 0; j < 5; j++) {
                if (nums[i] < nums[j]) {
                    rank++;
                }
            }
            ranks[i] = rank;
        }

        return ranks;
    }

    private String getHomeRankMusic(int[] arrRank, int rank)
    {
        String[] homeMusics = {mTextHome1, mTextHome2, mTextHome3, mTextHome4, mTextHome5};
        String result = "";
        for(int i = 0; i < 5; i++)
        {
            if(arrRank[i] == rank)
            {
                result = homeMusics[i];
                break;
            }
        }
        return result;
    }

    private String getAwayRankMusic(int[] arrRank, int rank)
    {
        String[] awayMusics = {mTextAway1, mTextAway2, mTextAway3, mTextAway4, mTextAway5};
        String result = "";
        for(int i = 0; i < 5; i++)
        {
            if(arrRank[i] == rank)
            {
                result = awayMusics[i];
                break;
            }
        }
        return result;
    }

    private void updateScoreValue(RunEvent runEvent, final int home, final int away)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run() {
                mTextHome.setText("" + home);
                mTextAway.setText("" + away);
                int homwVal = home;
                int awayVal = away;
                if (homwVal == awayVal && homwVal == 0) {
                    homwVal = awayVal = 1;
                }
                //mTextCurrent.setText("이벤트 서버 " + name + " 실시간 응원 결과");
                mTextCurrent.setText(runEvent.getServerName());
                mTextHomeName.setText(runEvent.getHomeName());
                mTextAwayName.setText(runEvent.getAwayName());

                ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, homwVal);
                mTextHome.setLayoutParams(params);

                ViewGroup.LayoutParams params1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, awayVal);
                mTextAway.setLayoutParams(params1);

                String imageType = "IMAGE_DEFAULT";
                if (home == away) {
                    viewImageType = "IMAGE_DEFAULT";
                    imageType = "IMAGE_DEFAULT";
                } else if (home > away) {
                    viewImageType = "IMAGE_HOME";
                    imageType = "IMAGE_HOME";
                } else if (home < away) {
                    viewImageType = "IMAGE_AWAY";
                    imageType = "IMAGE_AWAY";
                }
                String viewImage = getTypeImage(mEventDto.getEventImageList(), imageType);
                if (viewImage != null) {
                    //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
                    Uri uri = getContentUri(viewImage);
                    mImgHalf.setImageURI(uri);
                    Glide.with(FullsImageActivity.this).load(uri).into(mImgHalf);
                }
            }
        });
    }

    public void playMusic(RunEvent runEvent){
        Log.d("AAAA", "full - playMusic");
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

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getHomeMusic(RunEvent runEvent){
        int max = runEvent.getHome1Count();
        String name = mTextHome1;
        if(max < runEvent.getHome2Count()){
            max = runEvent.getHome2Count();
            name = mTextHome2;
        }
        if(max < runEvent.getHome3Count()){
            max = runEvent.getHome3Count();
            name = mTextHome3;
        }
        if(max < runEvent.getHome4Count()){
            max = runEvent.getHome4Count();
            name = mTextHome4;
        }
        if(max < runEvent.getHome5Count()){
            max = runEvent.getHome5Count();
            name = mTextHome5;
        }
        return name;
    }

    public String getAwayMusic(RunEvent runEvent){
        int max = runEvent.getAway1Count();
        String name = mTextAway1;
        if(max < runEvent.getAway2Count()){
            max = runEvent.getAway2Count();
            name = mTextAway2;
        }
        if(max < runEvent.getAway3Count()){
            max = runEvent.getAway3Count();
            name = mTextAway3;
        }
        if(max < runEvent.getAway4Count()){
            max = runEvent.getAway4Count();
            name = mTextAway4;
        }
        if(max < runEvent.getAway5Count()){
            max = runEvent.getAway5Count();
            name = mTextAway5;
        }
        return name;
    }

    public void setEventInfo(ArrayList<EventMusicDto> musicDtos){
        for(EventMusicDto musicDto : musicDtos){
            if(musicDto.getTeamType().equalsIgnoreCase("TEAM_HOME"))
                setHomeMusicSequenc(musicDto.getSequence(), musicDto.getMusicName());
            else if(musicDto.getTeamType().equalsIgnoreCase("TEAM_AWAY"))
                setAwayMusicSequenc(musicDto.getSequence(), musicDto.getMusicName());

        }
    }

    private void setHomeMusicSequenc(int sequenc, String name){
        switch (sequenc){
            case 0:
                mTextHome1 = name;
                break;
            case 1:
                mTextHome2 = name;
                break;
            case 2:
                mTextHome3 = name;
                break;
            case 3:
                mTextHome4 = name;
                break;
            case 4:
                mTextHome5 = name;
                break;
        }
    }
    private void setAwayMusicSequenc(int sequenc, String name){
        switch (sequenc){
            case 0:
                mTextAway1 = name;
                break;
            case 1:
                mTextAway2 = name;
                break;
            case 2:
                mTextAway3 = name;
                break;
            case 3:
                mTextAway4 = name;
                break;
            case 4:
                mTextAway5 = name;
                break;
        }
    }

    public void setEventColor(String colorHome, String colorAway){

        try{
            int iColorHome = 0xff000000 | Integer.parseUnsignedInt(colorHome, 16);
            mTextHome.setBackgroundColor(iColorHome);

            int iColorAway = 0xff000000 | Integer.parseUnsignedInt(colorAway, 16);
            mTextAway.setBackgroundColor(iColorAway);
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

    private class AsyncRestartCheck extends AsyncTask<String, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(String... strings) {
            try{
                Thread.sleep(MainActivity.CHECK_DELAY);
            }catch (Exception e){}

            if(isRunning)
                mServer.getLastEvent(mRestartEventStateCallBack, mServerId);

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }
    }

    private SECallBack<RunEventResult> mRestartEventStateCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                try{
                    mRunEvent = response.body().getData();
                    Log.d("AAAA", "mRestartEventStateCallBack : " + mRunEvent.getEventState());
                    if(mRunEvent.getEventState().equalsIgnoreCase("START"))
                    {
                        mRunEventId = (int)mRunEvent.getId();
                    }
                    else if(mRunEvent.getEventState().equalsIgnoreCase("STOP"))
                    {
                        if(mRunEventId > 0)
                        {
                            mRunEventId = -1;
                            updateScore(mRunEvent);

                            int home = mRunEvent.getHomeCount();
                            int away = mRunEvent.getAwayCount();
                            if (home == away) {
                                viewImageType = "IMAGE_DEFAULT";
                            } else if (home > away) {
                                viewImageType = "IMAGE_HOME";
                            } else if (home < away) {
                                viewImageType = "IMAGE_AWAY";
                            }
                            setImageView01();
                        }
                        AsyncRestartCheck async = new AsyncRestartCheck();
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

            //showProgress(FullsImageActivity.this, false);
        }
    };



    private SECallBack<EventResult> mEventCallBack = new SECallBack<EventResult>()
    {
        @Override
        public void onResponseResult(Response<EventResult> response)
        {
            if (response.isSuccessful())
            {
                mEventDto = response.body().getData();
                Log.d("AAAA", "1 EventState : " + mEventDto.getEventState());
                setEventInfo(mEventDto.getEventMusicList());
                setEventColor(mEventDto.getHomeColor(), mEventDto.getAwayColor());
                if(mEventDto.getEventState().equalsIgnoreCase("START"))
                {
                    startEventStateCheck(mEventDto.getRunEvent());
                }
                else
                {
                    //setImageView01();
                    mServer.getRunEventState(mEndEventStateCallBack, mEventDto.getRunEvent());
                }

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
                    mRunEvent = response.body().getData();
                    Log.d("AAAA", "2 EventState : " + mRunEvent.getEventState());
                    if(mRunEvent.getEventState().equalsIgnoreCase("START"))
                    {
                        updateScore(mRunEvent);
                    }
                    else if(mRunEvent.getEventState().equalsIgnoreCase("STOP") && mRunEventId != -1)
                    {
                        mRunEventId = -1;
                        playMusic(mRunEvent);

                        mServer.getRunEventState(mEndEventStateCallBack, mEventDto.getRunEvent());

//                        AsyncCheckState async = new AsyncCheckState();
//                        async.execute();
                    }
                    //setImageView01();

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            else
            {
                // no event
            }

            //showProgress(FullsImageActivity.this, false);
        }
    };

    private SECallBack<RunEventResult> mEndEventStateCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                try{
                    mRunEvent = response.body().getData();
                    Log.d("AAAA", "3 EventState : " + mRunEvent.getEventState());
                    if(mRunEvent.getEventState().equalsIgnoreCase("STOP"))
                    {
                        updateScore(mRunEvent);

                        int home = mRunEvent.getHomeCount();
                        int away = mRunEvent.getAwayCount();
                        if (home == away) {
                            viewImageType = "IMAGE_DEFAULT";
                        } else if (home > away) {
                            viewImageType = "IMAGE_HOME";
                        } else if (home < away) {
                            viewImageType = "IMAGE_AWAY";
                        }
                        setImageView01();

                        AsyncRestartCheck async = new AsyncRestartCheck();
                        async.execute();
                    }
                    else
                    {

                    }
                    //setImageView01();

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            else
            {
                // no event
            }

            //showProgress(FullsImageActivity.this, false);
        }
    };

    private SECallBack<RunEventResult> mRunEventStateCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                try{
                    mRunEvent = response.body().getData();
                    if(mRunEvent.getEventState().equalsIgnoreCase("START"))
                    {
                        startEventStateCheck(mRunEvent.getId());
                    }
                    else if(mRunEvent.getEventState().equalsIgnoreCase("STOP"))
                    {
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

            //showProgress(FullsImageActivity.this, false);
        }
    };

    private class AsyncCheckState extends AsyncTask<String, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(String... strings) {
            try{
                Thread.sleep(MainActivity.CHECK_DELAY);
            }catch (Exception e){}

            if(mRunEventId < 0)
            {
                //mServer.getRunEventState(mFirstEventStateCallBack, mRunEventId);
                mServer.getLastEvent(mRunEventStateCallBack, mServerId);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }
    }


    private SECallBack<RunEventResult> mEventStartCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                mRunEvent = response.body().getData();

                mRunEventId = (int)mRunEvent.getId();

                Log.d("AAAA", "--- 222mRunEventId : " + mRunEventId);
            }
        }
    };

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
                if(mEventDto != null && mEventDto.getContinuityType() == 1 )
                {
                    int diff = (total - current) / 1000;
                    Log.d("AAAA", "full total: "+total+" current: "+current+ " diff: " + diff);
                    Log.d("AAAA", "full getTriggerTime: "+mRunEvent.getTriggerTime());
                    if(diff < mRunEvent.getTriggerTime() && mRunEvent.getEventState().equalsIgnoreCase("STOP"))
                    {
                        Log.d("AAAA", "-------------- full getTriggerTime: "+mRunEvent.getTriggerTime());

                        EventStartReqDto reqDto = new EventStartReqDto(mServerId, -1, -1, -1, -1, -1);
                        mServer.eventStart(mEventStartCallBack, reqDto);
                        mRunEvent.setEventState("RESTART");

                        updateViewState(1);
                    }
                }
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