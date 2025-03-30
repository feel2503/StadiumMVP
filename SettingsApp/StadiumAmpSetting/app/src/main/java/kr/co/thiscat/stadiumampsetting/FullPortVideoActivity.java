package kr.co.thiscat.stadiumampsetting;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.PictureInPictureParams;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.thiscat.stadiumampsetting.databinding.ActivityPortFullVideoBinding;
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
public class FullPortVideoActivity extends AppCompatActivity {
    private View mControlsView;

    public String contentDirPath = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS) + "/StadiumAmp/";

    private ActivityPortFullVideoBinding binding;
    private ServerManager mServer;
    public int mRunEventId = -1;
    public int mServerId;

    private StyledPlayerView playerView;
    private ExoPlayer exoPlayer;
    private ImageView mImgHalf;

    private LinearLayout mRightView;

    private LinearLayout mLinearVote;
    private RelativeLayout mRelativeAd;
    private RelativeLayout mRelativeJoin;

    private TextView mTextResult1;
    private TextView mTextResult2;
    private TextView mTextResult3;

    private TextView mTextCurrent;
    private TextView mTextHomeName;
    private TextView mTextAwayName;
    private TextView mTextHome;
    private TextView mTextAway;

    private ImageView mImageAdImg;
    private ImageView mImageQr;


    public EventDto mEventDto = null;
    private RunEvent mRunEvent;

    private boolean isRunning = false;
    private boolean isPIPMode = false;
    private Timer mTimer;

    private String viewImageType = "IMAGE_DEFAULT";

    public String mTextHome1;
    public String mTextHome2;
    public String mTextHome3;
    public String mTextHome4;
    public String mTextHome5;
    public String mTextHome6;
    public String mTextHome7;
    public String mTextHome8;
    public String mTextHome9;
    public String mTextHome10;
    public String mTextHome11;
    public String mTextHome12;
    public String mTextHome13;
    public String mTextHome14;
    public String mTextHome15;
    public String mTextHome16;
    public String mTextHome17;
    public String mTextHome18;
    public String mTextHome19;
    public String mTextHome20;
    public String mTextAway1;
    public String mTextAway2;
    public String mTextAway3;
    public String mTextAway4;
    public String mTextAway5;
    public String mTextAway6;
    public String mTextAway7;
    public String mTextAway8;
    public String mTextAway9;
    public String mTextAway10;
    public String mTextAway11;
    public String mTextAway12;
    public String mTextAway13;
    public String mTextAway14;
    public String mTextAway15;
    public String mTextAway16;
    public String mTextAway17;
    public String mTextAway18;
    public String mTextAway19;
    public String mTextAway20;

    private int mVolume;

    private RelativeLayout mActivityBG;

    private WebView webView1;
    private WebView webView2;
    private WebSettings mWebSettings1;
    private WebSettings mWebSettings2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = ActivityPortFullVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mControlsView = binding.fullscreenContentControls;
        hide();

        mServerId = getIntent().getIntExtra("RunServerID", -1);
        getIntent().getBooleanExtra("EventRepeat", false);
        mVolume = getIntent().getIntExtra("VideoVolume", 0);

        mServer = ServerManager.getInstance(FullPortVideoActivity.this);

        initUi();
        setWebView();

//        IntentFilter filter = new IntentFilter();
//        filter.addAction(MusicPlayService.ACTION_PLAY_START_RESULT);
//        filter.addAction(MusicPlayService.ACTION_UPDATE_CURRENT_POSITION);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, filter);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            PictureInPictureParams.Builder pipBuilder = new PictureInPictureParams.Builder();
//            enterPictureInPictureMode(pipBuilder.build());
//        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //setContentView(R.layout.activity_full_video);
        binding = ActivityPortFullVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, @NonNull Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (isInPictureInPictureMode) {
            isPIPMode = true;
        } else {
            isPIPMode = false;
        }
    }

    private void initUi()
    {
        playerView = findViewById(R.id.video_view);
        exoPlayer = new ExoPlayer.Builder(FullPortVideoActivity.this).build();
        exoPlayer.addListener(mPlayerListener);
        playerView.setPlayer(exoPlayer);
        exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);


        mImgHalf = findViewById(R.id.image_half_view);

        mRightView = findViewById(R.id.linear_right_view);
        mLinearVote = findViewById(R.id.linear_vote);
        mRelativeAd = findViewById(R.id.relative_ad_img);
        mRelativeJoin = findViewById(R.id.relative_join);

        mTextCurrent = findViewById(R.id.text_current_server);
        mTextHomeName = findViewById(R.id.text_result_home_count);
        mTextAwayName = findViewById(R.id.text_result_away_count);
        mTextHome = findViewById(R.id.text_result_home);
        mTextAway = findViewById(R.id.text_result_away);

        mTextResult1 = findViewById(R.id.text_result_01);
        mTextResult2 = findViewById(R.id.text_result_02);
        mTextResult3 = findViewById(R.id.text_result_03);

        mImageAdImg = findViewById(R.id.image_ad);
        mImageQr = findViewById(R.id.image_qr);

        mActivityBG = findViewById(R.id.fullscreen_content_controls);

        SharedPreferences mPref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        int iBgColor = mPref.getInt("bgcolor", 0);
        switch (iBgColor){
            case 0:
                mActivityBG.setBackgroundColor(0xff000000);
                break;
            case 1:
                mActivityBG.setBackgroundColor(0xff00b140);
                break;
            case 2:
                mActivityBG.setBackgroundColor(0xff0047bb);
                break;
        }
    }

    private void setWebView()
    {
        /// webview1
        webView1 = findViewById(R.id.webview1);

        webView1.setBackgroundColor(0); // 완전 투명
        webView1.setLayerType(View.LAYER_TYPE_SOFTWARE, null); // 소프트웨어 렌더링 사용
//        webView1.setScaleX(-1);

        webView1.setWebViewClient(new WebViewClient()); // 현재 앱을 나가서 새로운 브라우저를 열지 않도록 함.

        mWebSettings1 = webView1.getSettings(); // 웹뷰에서 webSettings를 사용할 수 있도록 함.
        mWebSettings1.setJavaScriptEnabled(true); //웹뷰에서 javascript를 사용하도록 설정
        mWebSettings1.setJavaScriptCanOpenWindowsAutomatically(false); //멀티윈도우 띄우는 것
        mWebSettings1.setAllowFileAccess(true); //파일 엑세스
        mWebSettings1.setLoadWithOverviewMode(true); // 메타태그
        mWebSettings1.setUseWideViewPort(true); //화면 사이즈 맞추기
        mWebSettings1.setSupportZoom(true); // 화면 줌 사용 여부
        mWebSettings1.setBuiltInZoomControls(true); //화면 확대 축소 사용 여부
        mWebSettings1.setDisplayZoomControls(true); //화면 확대 축소시, webview에서 확대/축소 컨트롤 표시 여부
        mWebSettings1.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 사용 재정의 value : LOAD_DEFAULT, LOAD_NORMAL, LOAD_CACHE_ELSE_NETWORK, LOAD_NO_CACHE, or LOAD_CACHE_ONLY
        mWebSettings1.setDefaultFixedFontSize(14); //기본 고정 글꼴 크기, value : 1~72 사이의 숫자

        /// webview2
        webView2 = findViewById(R.id.webview2);

        webView2.setBackgroundColor(0); // 완전 투명
        webView2.setLayerType(View.LAYER_TYPE_SOFTWARE, null); // 소프트웨어 렌더링 사용
//        webView2.setScaleX(-1);

        webView2.setWebViewClient(new WebViewClient()); // 현재 앱을 나가서 새로운 브라우저를 열지 않도록 함.

        mWebSettings2 = webView2.getSettings(); // 웹뷰에서 webSettings를 사용할 수 있도록 함.
        mWebSettings2.setJavaScriptEnabled(true); //웹뷰에서 javascript를 사용하도록 설정
        mWebSettings2.setJavaScriptCanOpenWindowsAutomatically(false); //멀티윈도우 띄우는 것
        mWebSettings2.setAllowFileAccess(true); //파일 엑세스
        mWebSettings2.setLoadWithOverviewMode(true); // 메타태그
        mWebSettings2.setUseWideViewPort(true); //화면 사이즈 맞추기
        mWebSettings2.setSupportZoom(true); // 화면 줌 사용 여부
        mWebSettings2.setBuiltInZoomControls(true); //화면 확대 축소 사용 여부
        mWebSettings2.setDisplayZoomControls(true); //화면 확대 축소시, webview에서 확대/축소 컨트롤 표시 여부
        mWebSettings2.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 사용 재정의 value : LOAD_DEFAULT, LOAD_NORMAL, LOAD_CACHE_ELSE_NETWORK, LOAD_NO_CACHE, or LOAD_CACHE_ONLY
        mWebSettings2.setDefaultFixedFontSize(14); //기본 고정 글꼴 크기, value : 1~72 사이의 숫자
    }

    private boolean isRunningState()
    {
        return isRunning | isPIPMode;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        if(isPIPMode){
            //Toast.makeText(this, "isPIP", Toast.LENGTH_SHORT).show();
        }
        else{
            //Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
        }
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
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);

        if(exoPlayer.isPlaying()){
            exoPlayer.stop();
        }
    }

    Player.Listener mPlayerListener = new Player.Listener() {
        @Override
        public void onEvents(Player player, Player.Events events) {
            Player.Listener.super.onEvents(player, events);
        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            Player.Listener.super.onIsPlayingChanged(isPlaying);
            if(isPlaying)
            {

            }
            else
            {
                mServer.nextRunEvent(mNextEventCallBack, mServerId, mRunEvent.getId());
                //updateViewState(0);
                //mServer.getRunEventState(mEndEventStateCallBack, mEventDto.getRunEvent());
            }
        }

    };

    private void updateViewState(int state)
    {
        if(state == 0){
            mRightView.setVisibility(View.VISIBLE);
            mLinearVote.setVisibility(View.VISIBLE);
            mRelativeAd.setVisibility(View.GONE);
            mRelativeJoin.setVisibility(View.GONE);

            mImgHalf.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
        }
        else if(state == 1){
            mRightView.setVisibility(View.VISIBLE);
            mLinearVote.setVisibility(View.VISIBLE);
            mRelativeAd.setVisibility(View.GONE);
            mRelativeJoin.setVisibility(View.GONE);

            mImgHalf.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
        }
        else if(state == 2){
            mRightView.setVisibility(View.VISIBLE);
            mLinearVote.setVisibility(View.GONE);
            mRelativeAd.setVisibility(View.VISIBLE);
            mRelativeJoin.setVisibility(View.GONE);
        }
        else if(state == 3){
            mRightView.setVisibility(View.VISIBLE);
            mLinearVote.setVisibility(View.GONE);
            mRelativeAd.setVisibility(View.GONE);
            mRelativeJoin.setVisibility(View.VISIBLE);
        }
        else if(state == 4){
            mRightView.setVisibility(View.GONE);
        }
    }

    private void setImageView00()
    {
        updateViewState(0);

        String defImage = getTypeImage(mEventDto.getEventImageList(), viewImageType);
        if(defImage != null && isRunningState())
        {
            //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
            Uri uri = getContentUri(defImage);
            mImgHalf.setImageURI(uri);
            Glide.with(this).load(uri).into(mImgHalf);
        }
    }

    private void setImageView01()
    {
        updateViewState(1);

//        String defImage = getTypeImage(mEventDto.getEventImageList(), viewImageType);
//        if(defImage != null && isRunning)
//        {
//            //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
//            Uri uri = getContentUri(defImage);
//            mImgHalf.setImageURI(uri);
//            Glide.with(this).load(uri).into(mImgHalf);
//        }
        if(isRunningState()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isRunningState())
                        setImageView02();
                }
            }, 5000);
        }
    }
    private void setImageView02()
    {
        Log.d("AAAA", "setImageView02 : ");
        if(exoPlayer != null && exoPlayer.isPlaying())
            updateViewState(2);

        String defImage = getTypeImage(mEventDto.getEventImageList(), "IMAGE_ADV");
        if(defImage != null && isRunningState())
        {
            //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
            Uri uri = getContentUri(defImage);
            mImageAdImg.setImageURI(uri);
            Glide.with(this).load(uri).into(mImageAdImg);
        }
        if(isRunningState()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isRunningState())
                        setImageView03();
                }
            }, 5000);
        }
    }
    private void setImageView03()
    {
        Log.d("AAAA", "setImageView03 : ");
        if(exoPlayer != null && exoPlayer.isPlaying())
            updateViewState(3);

        String defImage = getTypeImage(mEventDto.getEventImageList(), "IMAGE_QR");
        if(defImage != null && isRunningState())
        {
            //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
            Uri uri = getContentUri(defImage);
            mImageQr.setImageURI(uri);
            Glide.with(this).load(uri).into(mImageQr);
        }
        if(isRunningState()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setImageView04();
                }
            }, 5000);
        }
    }

    private void setImageView04()
    {
        Log.d("AAAA", "setImageView04 : ");
        if(exoPlayer != null && exoPlayer.isPlaying())
            updateViewState(4);

//        mImageFull.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        String defImage = getTypeImage(mEventDto.getEventImageList(), "IMAGE_ADV");
//        if(defImage != null && isRunning)
//        {
//            //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
//            Uri uri = getContentUri(defImage);
//            mImageFull.setImageURI(uri);
//            Glide.with(this).load(uri).into(mImageFull);
//        }
//
//        if(isRunning){
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mImageFull.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                    String defImage = getTypeImage(mEventDto.getEventImageList(), viewImageType);
//                    if(defImage != null && isRunning)
//                    {
//                        //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
//                        Uri uri = getContentUri(defImage);
//                        mImageFull.setImageURI(uri);
//                        Glide.with(FullVideoActivity.this).load(uri).into(mImageFull);
//                    }
//                }
//            }, 5000);
//        }
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
        if(!exoPlayer.isPlaying())
            updateViewState(0);

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

                                //setImageView01();
                            }
                        });
                        return;
                    }
                    else if(mRunEvent.getEventState().equalsIgnoreCase("START"))
                    {

                    }
                }
                if( isRunningState())
                {
                    Log.d("AAAA", "--- startEventStateCheck : " + mRunEventId);
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
        String[] homeMusics = {mTextHome1, mTextHome2, mTextHome3, mTextHome4, mTextHome5,
                mTextHome6, mTextHome7, mTextHome8, mTextHome8, mTextHome10,
                mTextHome11, mTextHome12, mTextHome13, mTextHome14, mTextHome15,
                mTextHome16, mTextHome17, mTextHome18, mTextHome19, mTextHome20};
        String result = "";
        for(int i = 0; i < arrRank.length; i++)
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
        String[] awayMusics = {mTextAway1, mTextAway2, mTextAway3, mTextAway4, mTextAway5,
                mTextAway6, mTextAway7, mTextAway8, mTextAway9, mTextAway10,
                mTextAway10, mTextAway12, mTextAway13, mTextAway14, mTextAway15,
                mTextAway16, mTextAway17, mTextAway18, mTextAway19, mTextAway20 };
        String result = "";
        for(int i = 0; i < arrRank.length; i++)
        {
            if(arrRank[i] == rank)
            {
                result = awayMusics[i];
                break;
            }
        }
        return result;
    }

    private void animateTextSize(TextView textView) {
        ValueAnimator animator = ValueAnimator.ofFloat(16f, 26f, 16f);  // 텍스트 크기 변경 (sp)
        animator.setDuration(300);
        animator.addUpdateListener(animation -> {
            float textSize = (float) animation.getAnimatedValue();
            textView.setTextSize(textSize);
        });
        animator.start();
    }

    private void updateScoreValue(RunEvent runEvent, final int home, final int away)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run() {
                String oldHome = mTextHome.getText().toString();
                String oldAway = mTextAway.getText().toString();
                mTextHome.setText("" + home);
                mTextAway.setText("" + away);
                String nowHome = mTextHome.getText().toString();
                String nowAway = mTextAway.getText().toString();
                if(!oldHome.equalsIgnoreCase(nowHome))
                    animateTextSize(mTextHome);
                if(!oldAway.equalsIgnoreCase(nowAway))
                    animateTextSize(mTextAway);

                int homwVal = home;
                int awayVal = away;
                if (homwVal == awayVal && homwVal == 0) {
                    homwVal = awayVal = 1;
                }
                //mTextCurrent.setText("이벤트 서버 " + name + " 실시간 응원 결과");
                mTextCurrent.setText(runEvent.getServerName());
                mTextHomeName.setText(runEvent.getHomeName());
                mTextAwayName.setText(runEvent.getAwayName());

                ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, homwVal);
                mTextHome.setLayoutParams(params);

                ViewGroup.LayoutParams params1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, awayVal);
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
                    Glide.with(FullPortVideoActivity.this).load(uri).into(mImgHalf);
                }
            }
        });
    }

    public void playVideo(RunEvent runEvent){
        try{
            if(exoPlayer.isPlaying()){
                exoPlayer.stop();
            }

            String strUri = null;
            if(runEvent.getHomeCount() >= runEvent.getAwayCount())
            {
                strUri = getHomeMusic(runEvent);
            }
            else
            {
                strUri = getAwayMusic(runEvent);
            }
            //test
            //strUri = "test.mp4";
            Uri videoUri = getContentUri(strUri);
            MediaItem mediaItem = MediaItem.fromUri(videoUri);
            exoPlayer.setMediaItem(mediaItem);
            //exoPlayer.setVolume((runEvent.getVolumeValue()*0.1f));
            exoPlayer.setVolume(mVolume * 0.1f);
            exoPlayer.prepare();
            exoPlayer.play(); //자동으로 로딩완료까지 기다렸다가 재생함

        }catch (Exception e){
            e.printStackTrace();
        }

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
            max = runEvent.getHome2Count(); name = mTextHome2;
        }
        if(max < runEvent.getHome3Count()){
            max = runEvent.getHome3Count(); name = mTextHome3;
        }
        if(max < runEvent.getHome4Count()){
            max = runEvent.getHome4Count(); name = mTextHome4;
        }
        if(max < runEvent.getHome5Count()){
            max = runEvent.getHome5Count(); name = mTextHome5;
        }
        if(max < runEvent.getHome6Count()){
            max = runEvent.getHome6Count(); name = mTextHome6;
        }
        if(max < runEvent.getHome7Count()){
            max = runEvent.getHome7Count(); name = mTextHome7;
        }
        if(max < runEvent.getHome8Count()){
            max = runEvent.getHome8Count(); name = mTextHome8;
        }
        if(max < runEvent.getHome9Count()){
            max = runEvent.getHome9Count(); name = mTextHome9;
        }
        if(max < runEvent.getHome10Count()){
            max = runEvent.getHome10Count(); name = mTextHome10;
        }
        if(max < runEvent.getHome11Count()){
            max = runEvent.getHome11Count(); name = mTextHome11;
        }
        if(max < runEvent.getHome12Count()){
            max = runEvent.getHome12Count(); name = mTextHome12;
        }
        if(max < runEvent.getHome13Count()){
            max = runEvent.getHome13Count(); name = mTextHome13;
        }
        if(max < runEvent.getHome14Count()){
            max = runEvent.getHome14Count(); name = mTextHome14;
        }
        if(max < runEvent.getHome15Count()){
            max = runEvent.getHome15Count(); name = mTextHome15;
        }
        if(max < runEvent.getHome16Count()){
            max = runEvent.getHome16Count(); name = mTextHome16;
        }
        if(max < runEvent.getHome17Count()){
            max = runEvent.getHome17Count(); name = mTextHome17;
        }
        if(max < runEvent.getHome18Count()){
            max = runEvent.getHome18Count(); name = mTextHome18;
        }
        if(max < runEvent.getHome19Count()){
            max = runEvent.getHome19Count(); name = mTextHome19;
        }
        if(max < runEvent.getHome20Count()){
            max = runEvent.getHome20Count(); name = mTextHome20;
        }

        return name;
    }

    public String getAwayMusic(RunEvent runEvent){
        int max = runEvent.getAway1Count();
        String name = mTextAway1;
        if(max < runEvent.getAway2Count()){
            max = runEvent.getAway2Count(); name = mTextAway2;
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
        if(max < runEvent.getAway6Count()){
            max = runEvent.getAway6Count(); name = mTextAway6;
        }
        if(max < runEvent.getAway7Count()){
            max = runEvent.getAway7Count(); name = mTextAway7;
        }
        if(max < runEvent.getAway8Count()){
            max = runEvent.getAway8Count(); name = mTextAway8;
        }
        if(max < runEvent.getAway9Count()){
            max = runEvent.getAway9Count(); name = mTextAway9;
        }
        if(max < runEvent.getAway10Count()){
            max = runEvent.getAway10Count(); name = mTextAway10;
        }
        if(max < runEvent.getAway11Count()){
            max = runEvent.getAway11Count(); name = mTextAway11;
        }
        if(max < runEvent.getAway12Count()){
            max = runEvent.getAway12Count(); name = mTextAway12;
        }
        if(max < runEvent.getAway13Count()){
            max = runEvent.getAway13Count(); name = mTextAway13;
        }
        if(max < runEvent.getAway14Count()){
            max = runEvent.getAway14Count(); name = mTextAway14;
        }
        if(max < runEvent.getAway15Count()){
            max = runEvent.getAway15Count(); name = mTextAway15;
        }
        if(max < runEvent.getAway16Count()){
            max = runEvent.getAway16Count(); name = mTextAway16;
        }
        if(max < runEvent.getAway17Count()){
            max = runEvent.getAway17Count(); name = mTextAway17;
        }
        if(max < runEvent.getAway18Count()){
            max = runEvent.getAway18Count(); name = mTextAway18;
        }
        if(max < runEvent.getAway19Count()){
            max = runEvent.getAway19Count(); name = mTextAway19;
        }
        if(max < runEvent.getAway20Count()){
            max = runEvent.getAway20Count(); name = mTextAway20;
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
            case 5:
                mTextHome6 = name;
                break;
            case 6:
                mTextHome7 = name;
                break;
            case 7:
                mTextHome8 = name;
                break;
            case 8:
                mTextHome9 = name;
                break;
            case 9:
                mTextHome10 = name;
                break;
            case 10:
                mTextHome11 = name;
                break;
            case 11:
                mTextHome12 = name;
                break;
            case 12:
                mTextHome13 = name;
                break;
            case 13:
                mTextHome14 = name;
                break;
            case 14:
                mTextHome15 = name;
                break;
            case 15:
                mTextHome16 = name;
                break;
            case 16:
                mTextHome17 = name;
                break;
            case 17:
                mTextHome18 = name;
                break;
            case 18:
                mTextHome19 = name;
                break;
            case 19:
                mTextHome20 = name;
                break;
        }
    }
    private void setAwayMusicSequenc(int sequenc, String name){
        switch (sequenc){
            case 0:
                mTextAway1 = name; break;
            case 1:
                mTextAway2 = name; break;
            case 2:
                mTextAway3 = name; break;
            case 3:
                mTextAway4 = name; break;
            case 4:
                mTextAway5 = name; break;
            case 5:
                mTextAway6 = name; break;
            case 6:
                mTextAway7 = name; break;
            case 7:
                mTextAway8 = name; break;
            case 8:
                mTextAway9 = name; break;
            case 9:
                mTextAway10 = name; break;
            case 10:
                mTextAway11 = name; break;
            case 11:
                mTextAway12 = name; break;
            case 12:
                mTextAway13 = name; break;
            case 13:
                mTextAway14 = name; break;
            case 14:
                mTextAway15 = name; break;
            case 15:
                mTextAway16 = name; break;
            case 16:
                mTextAway17 = name; break;
            case 17:
                mTextAway18 = name; break;
            case 18:
                mTextAway19 = name; break;
            case 19:
                mTextAway20 = name; break;
        }
    }

    public void setEventColor(String colorHome, String colorAway){

        try{
            int iColorHome = 0xff000000 | Integer.parseUnsignedInt(colorHome, 16);
            mTextHome.setBackgroundColor(iColorHome);
            //mTextHomeName.setTextColor(iColorHome);

            int iColorAway = 0xff000000 | Integer.parseUnsignedInt(colorAway, 16);
            mTextAway.setBackgroundColor(iColorAway);
            //mTextAwayName.setTextColor(iColorAway);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setEventColor(String colorHome, String fontHome, String colorAway, String fontAway){

        try{
            int iColorHome = 0xff000000 | Integer.parseUnsignedInt(colorHome, 16);
            mTextHome.setBackgroundColor(iColorHome);

            int iFontHome = 0xff000000 | Integer.parseUnsignedInt(fontHome, 16);
            mTextHome.setTextColor(iFontHome);

            //mTextHomeName.setTextColor(iColorHome);

            int iColorAway = 0xff000000 | Integer.parseUnsignedInt(colorAway, 16);
            mTextAway.setBackgroundColor(iColorAway);
            //mTextAwayName.setTextColor(iColorAway);

            int iFontAway = 0xff000000 | Integer.parseUnsignedInt(fontAway, 16);
            mTextAway.setTextColor(iFontAway);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_BACK)
            return super.onKeyDown(keyCode, event);

        Log.d("BBBB", "onKeyDown : " + mRunEvent.getEventState()+ " id: "+mRunEvent.getId());
        if(exoPlayer.isPlaying()) {
            stopEvent();
        } if(mRunEvent.getEventState().equalsIgnoreCase("START")){
            stopVoteEvent();
        } else {
            startEvent();
        }

        return super.onKeyDown(keyCode, event);
    }

    public void startEvent()
    {
        if(mEventDto != null  )
        {
            if(mRunEvent.getEventState().equalsIgnoreCase("STOP"))
            {
                Log.d("AAAA", "-------------- full getTriggerTime: "+mRunEvent.getTriggerTime());

                EventStartReqDto reqDto = new EventStartReqDto(mServerId, -1, -1, -1, 1, -1, -1);
                mServer.eventStart(mEventStartCallBack, reqDto);
                mRunEvent.setEventState("RESTART");

                updateViewState(1);
            }
        }
    }

    public void stopEvent()
    {
        Log.d("BBBB", "stopEvent : " + mRunEvent.getEventState());
        mServer.stopLastEvent(mLastEventStoptCallBack, mServerId);

//        if(mRunEvent.getEventState().equalsIgnoreCase("START")){
//            mServer.eventStop(mEventStoptCallBack, mRunEvent.getId());
//        }
    }

    public void stopVoteEvent()
    {
        Log.d("BBBB", "stopVoteEvent : " + mRunEvent.getEventState());
        mServer.eventStop(mEventStoptCallBack, mRunEvent.getId());
    }

    private void loadCheerWeb(EventDto eventDto)
    {
        if(eventDto.getCheerUrl1() != null && eventDto.getCheerUrl1().length() > 1)
        {
            webView1.loadUrl(eventDto.getCheerUrl1());
        }

        if(eventDto.getCheerUrl2() != null && eventDto.getCheerUrl2().length() > 1)
        {
            webView2.loadUrl(eventDto.getCheerUrl2());
        }
    }

    private SECallBack<RunEventResult> mEventStoptCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {


//                RunEvent runEvent = response.body().getData();
            }
        }
    };

    private SECallBack<RunEventResult> mLastEventStoptCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            Log.d("BBBB", "11 mLastEventStoptCallBack : " +response);
            if (response.isSuccessful())
            {
                Log.d("BBBB", "mLastEventStoptCallBack : " + mRunEvent.getEventState());
                mRunEvent = response.body().getData();
                exoPlayer.stop();
            }
            else
            {
                Log.d("BBBB", "mLastEventStoptCallBack : !response.isSuccessful() ");
            }
        }
    };

    private class AsyncRestartCheck extends AsyncTask<String, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(String... strings) {
            try{
                Thread.sleep(MainActivity.CHECK_DELAY);
            }catch (Exception e){}

            if(isRunningState())
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
                        startEventStateCheck(mRunEventId);
                    }
                    else if(mRunEvent.getEventState().equalsIgnoreCase("STOP"))
                    {
                        if(mRunEventId > 0)
                        {
                            mRunEventId = -1;

                            if(!exoPlayer.isPlaying()){
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
                            }
                            //setImageView01();
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

    private SECallBack<RunEventResult> mNextEventCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                try{
                    if(response.body().getData() != null)
                    {
                        try{
                            mRunEvent = response.body().getData();
                            Log.d("BBBB", "mNextEventCallBack : " + mRunEvent.getId());
                            if(mRunEvent.getEventState().equalsIgnoreCase("START"))
                            {
                                mRunEventId = (int)mRunEvent.getId();
                                startEventStateCheck(mRunEventId);

//                                if(!exoPlayer.isPlaying()) {
//                                    updateViewState(0);
//                                    updateScore(mRunEvent);
//                                }
                            }
                            else if(mRunEvent.getEventState().equalsIgnoreCase("STOP") )
                            {
                                mRunEventId = -1;
                                updateScore(mRunEvent);
                                setImageView01();
                                playVideo(mRunEvent);
//                                AsyncCheckState async = new AsyncCheckState();
//                                async.execute();
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Log.d("BBBB", "mNextEventCallBack : else " + mRunEvent.getId());
                        setImageView00();
                        mServer.getRunEventState(mEndEventStateCallBack, mEventDto.getRunEvent());

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
                //setEventColor(mEventDto.getHomeColor(), mEventDto.getAwayColor());
                setEventColor(mEventDto.getHomeColor(), mEventDto.getHomeFont(), mEventDto.getAwayColor(), mEventDto.getAwayFont());
                loadCheerWeb(mEventDto);
                if(mEventDto.getEventState().equalsIgnoreCase("START"))
                {
                    startEventStateCheck(mEventDto.getRunEvent());
                }
                else
                {
                    setImageView00();
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
                    Log.d("AAAA", "mEventStateCallBack : " + mRunEvent.getEventState());
                    if(mRunEvent.getEventState().equalsIgnoreCase("START"))
                    {
                        if(!exoPlayer.isPlaying()) {
                            updateViewState(0);
                            updateScore(mRunEvent);
                        }
                    }
                    else if(mRunEvent.getEventState().equalsIgnoreCase("STOP") && mRunEventId != -1)
                    {
                        mRunEventId = -1;

                        updateScore(mRunEvent);

                        setImageView01();
                        playVideo(mRunEvent);
                        //playMusic(mRunEvent);


                        //mServer.getRunEventState(mEndEventStateCallBack, mEventDto.getRunEvent());

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

    private SECallBack<RunEventResult> mFirstEventStateCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                try{
                    mRunEvent = response.body().getData();
                    Log.d("AAAA", "mFirstEventStateCallBack : " + mRunEvent.getEventState());
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
//                        setImageView00();
//
                        AsyncRestartCheck async = new AsyncRestartCheck();
                        async.execute();
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
                    Log.d("AAAA", "mEndEventStateCallBack : " + mRunEvent.getEventState());
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
                        //setImageView01();

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
                    Log.d("AAAA", "111 mEventStateCallBack : " + mRunEvent.getEventState());
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


}