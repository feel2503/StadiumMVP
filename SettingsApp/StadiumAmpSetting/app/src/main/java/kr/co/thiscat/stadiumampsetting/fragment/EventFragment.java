package kr.co.thiscat.stadiumampsetting.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kr.co.thiscat.stadiumampsetting.FullPortVideoActivity;
import kr.co.thiscat.stadiumampsetting.FullVideoActivity;
import kr.co.thiscat.stadiumampsetting.FullsImageActivity;
import kr.co.thiscat.stadiumampsetting.MainActivity;
import kr.co.thiscat.stadiumampsetting.MusicPlayService;
import kr.co.thiscat.stadiumampsetting.PreferenceUtil;
import kr.co.thiscat.stadiumampsetting.R;
import kr.co.thiscat.stadiumampsetting.server.SECallBack;
import kr.co.thiscat.stadiumampsetting.server.ServerManager;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEvent;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEventResult;
import kr.co.thiscat.stadiumampsetting.server.entity.result.EventResult;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventStartReqDto;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ServerManager mServer;
    protected ProgressDialog mProgress = null;
    private PreferenceUtil mPreferenceUtil;

    private TextView mTextCurrent;
    private TextView mTextHome;
    private TextView mTextAway;
    private TextView mTextTime;

    private TextView mTextHomeCount;
    private TextView mTextAwayCount;

    private int second;
    private boolean isRunning = false;

    private ImageView mImgEvent;

    private LinearLayout mLinearEvent;
    private WebView mWebView;
    private FloatingActionButton mFab;
    private String mLoadUrl;

    private TextView songTitle;
    private TextView textCurrentTime;
    private TextView textTotalDuration;
    private SeekBar mSeekBarTime;

//    Timer timer;
    private static EventFragment mInstance;

    private MainActivity mainActivity;

    private StyledPlayerView playerView;
    private ExoPlayer exoPlayer;
    private String mMusicTitle;

    private SeekBar mSeekVolume;
    private TextView mTextVolume;

    private RadioButton mRadioBlack;
    private RadioButton mRadioGreen;
    private RadioButton mRadioBlue;

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private CheckBox mCheckSoundSync;

    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment getInstance()
    {
        if(mInstance == null)
            mInstance = newInstance(null, null);
        return mInstance;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mainActivity = (MainActivity)getActivity();
        mPreferenceUtil = new PreferenceUtil(getContext());
        mProgress = new ProgressDialog(getContext());
        mServer = ServerManager.getInstance(getContext());

        mPref = mainActivity.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        mEditor = mPref.edit();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        mTextHome = view.findViewById(R.id.text_result_home);
        mTextAway = view.findViewById(R.id.text_result_away);
        mTextTime = view.findViewById(R.id.text_event_remain);
        mImgEvent = view.findViewById(R.id.imageView);
        mTextCurrent = view.findViewById(R.id.text_current_server);

        mTextHomeCount = view.findViewById(R.id.text_result_home_count);
        mTextAwayCount = view.findViewById(R.id.text_result_away_count);

        mLinearEvent = view.findViewById(R.id.linear_event);
//        mWebView = view.findViewById(R.id.web_view);
//        initWebView();

        mFab = view.findViewById(R.id.fab_web);
        mFab.setOnClickListener(mOnClickListener);

        songTitle = view.findViewById(R.id.songTitle);
        textCurrentTime = view.findViewById(R.id.textCurrentTime);
        textTotalDuration = view.findViewById(R.id.textTotalDuration);

        mSeekBarTime = view.findViewById(R.id.seekBar);
        mSeekBarTime.setMax(100);
        mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Log.d("AAAA", "onProgressChanged: "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int pos = seekBar.getProgress();
                //Log.d("AAAA", "onStartTrackingTouch: "+pos);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int pos = seekBar.getProgress();
                //Log.d("AAAA", "onStopTrackingTouch: "+pos);
                //mainActivity.updatePlayPos(pos);
            }
        });
        //mSeekBarTime.setOnTouchListener(mOntouchListener);

        //mSeekBarTime.setEnabled(false);

        mSeekVolume = view.findViewById(R.id.seekBar_volume);
        mSeekVolume.setMax(15);
        mSeekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateVolumeValue(progress);
                mainActivity.volumeValue = progress;
                mainActivity.setVolume(progress);
                //mServer.setEventVolume(mSetVolumeCallBack, mainActivity.mServerId, progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mTextVolume = view.findViewById(R.id.text_volume);

        view.findViewById(R.id.screen_full_view).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.screen_full_port_view).setOnClickListener(mOnClickListener);
//        if(mainActivity.mEventDto.getWebUrl() != null){
//            mFab.setVisibility(View.VISIBLE);
//            mWebView.loadUrl(mainActivity.mEventDto.getWebUrl());
//        } else{
//            mFab.setVisibility(View.GONE);
//        }

//        if(mainActivity.mWebViewState){
//            mWebView.setVisibility(View.VISIBLE);
//            mLinearEvent.setVisibility(View.GONE);
//
//        }else{
//            mWebView.setVisibility(View.GONE);
//            mLinearEvent.setVisibility(View.VISIBLE);
//        }

        playerView = view.findViewById(R.id.video_view);
        exoPlayer = new ExoPlayer.Builder(getContext()).build();
        exoPlayer.addListener(mPlayerListener);
        playerView.setPlayer(exoPlayer);

        mRadioBlack = view.findViewById(R.id.radio_chromakey_black);
        mRadioGreen = view.findViewById(R.id.radio_chromakey_green);
        mRadioBlue = view.findViewById(R.id.radio_chromakey_blue);

        RadioGroup radioGroup = view.findViewById(R.id.radio_chromakey);
        radioGroup.setOnCheckedChangeListener(mOnCheckedChangedListner);

        int iBgColor = mPref.getInt("bgcolor", 0);
        switch (iBgColor){
            case 0:
                mRadioBlack.setChecked(true);
                break;
            case 1:
                mRadioGreen.setChecked(true);
                break;
            case 2:
                mRadioBlue.setChecked(true);
                break;
        }

        mCheckSoundSync = view.findViewById(R.id.check_sound_sync);
        mCheckSoundSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPreferenceUtil.putBooleanPreference(PreferenceUtil.VOLUME_SYNC, isChecked);
            }
        });


        return view;
    }



    private void initWebView()
    {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        settings.setDatabaseEnabled(true); // 데이터베이스 접근 허용 여부
        settings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
        settings.setDefaultTextEncodingName("UTF-8"); // encoding 설정
        settings.setDisplayZoomControls(true); // 돋보기 없애기
        settings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        settings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false); // 화면 줌 허용 여부
        settings.setAllowFileAccessFromFileURLs(true); // 파일 URL로부터 파일 접근 허용
        settings.setAllowContentAccess(true); // 컨텐츠 접근 허용
        settings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        settings.setAllowFileAccess(true); // 파일 접근 허용 여부
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부

        mWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게 (알림 및 요청 관련 설정)

    }

    @Override
    public void onResume() {
        super.onResume();
        AudioManager audioManager = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSeekVolume.setProgress(volume);

        boolean isChecked = mPreferenceUtil.getBooleanPreference(PreferenceUtil.VOLUME_SYNC);
        mCheckSoundSync.setChecked(isChecked);

        if(mainActivity.mRunEvent != null){
            updateScore(mainActivity.mRunEvent);
            updateTimer(mainActivity.mRunEvent);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
//        timer.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        exoPlayer.release();
        exoPlayer = null;
    }

    public void playVideo(String url){
        mMusicTitle = url;

        playerView.setVisibility(View.VISIBLE);
        mImgEvent.setVisibility(View.GONE);

        Uri videoUri = getContentUri(url);
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.setVolume(mainActivity.volumeValue);
        
        exoPlayer.prepare();
        exoPlayer.play(); //자동으로 로딩완료까지 기다렸다가 재생함
    }

    public Uri getContentUri(String name){
        File outputFile = new File(MainActivity.contentDirPath+name);
//        if (!outputFile.getParentFile().exists()) {
//            outputFile.getParentFile().mkdirs();
//        }
        if(outputFile.exists())
            return Uri.fromFile(outputFile);
        else
            return null;
    }
    public void updateVolumeValue(int volume) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextVolume.setText(""+volume);
            }
        });
    }

    public void updateVolumeSeekbar(final int volume) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSeekVolume.setProgress(volume);
            }
        });
    }

    public void updateEventState(RunEvent runEvent) {
        if( getActivity() != null) {
            Log.d("AAAA", "----- eventFragment is visible");
            //updateTimeStr(runEvent);
            updateScore(runEvent);
            if(runEvent.getTriggerType() == 0){
                updateTimer(runEvent);    
            } else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (runEvent.getEventState().equalsIgnoreCase("START")) {
                            mTextTime.setText("투표중");
                            isRunning = true;
                        } else {
                            mTextTime.setText("이벤트 종료");
                            isRunning = false;
                        }
                    }
                });
            }
        }else{
            Log.d("AAAA", "----- eventFragment is invisible");
        }
    }


    public void updateEventInfo(RunEvent eventDto) {
//        if(eventDto.getWebUrl() != null){
//            mFab.setVisibility(View.VISIBLE);
//            mWebView.loadUrl(eventDto.getWebUrl());
//
//        } else{
//            mFab.setVisibility(View.GONE);
//        }
        if(eventDto.getOpenchatUrl() != null){
            mFab.setVisibility(View.VISIBLE);
            mLoadUrl = eventDto.getOpenchatUrl();

        } else{
            mFab.setVisibility(View.GONE);
        }
        String defImage = mainActivity.getTypeImage(eventDto.getEventImageList(), "IMAGE_DEFAULT");
        if(defImage != null)
        {
            //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
            Uri uri = mainActivity.getContentUri(defImage);
            mImgEvent.setImageURI(uri);
            Glide.with(this).load(uri).into(mImgEvent);
        }
        //mSeekVolume.setProgress(eventDto.getVolumeValue());
    }

    public void setEventInfo(EventDto eventDto) {
        Log.d("AAAA", "---- SettingFragment setEventInfo");
//        if(eventDto.getWebUrl() != null){
//            mFab.setVisibility(View.VISIBLE);
//            mWebView.loadUrl(eventDto.getWebUrl());
//        } else{
//            mFab.setVisibility(View.GONE);
//        }
        if(eventDto.getOpenchatUrl() != null){
            mFab.setVisibility(View.VISIBLE);
            mLoadUrl = eventDto.getOpenchatUrl();
        } else{
            mFab.setVisibility(View.GONE);
        }

        String defImage = mainActivity.getTypeImage(eventDto.getEventImageList(), "IMAGE_DEFAULT");
        if(defImage != null)
        {
            //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
            Uri uri = mainActivity.getContentUri(defImage);
            mImgEvent.setImageURI(uri);
            Glide.with(this).load(uri).into(mImgEvent);
        }

        //mSeekVolume.setProgress(eventDto.getVolumeValue());
    }

    public void setEventColor(String colorHome, String colorAway){

        try{
            int iColorHome = 0xff000000 | Integer.parseUnsignedInt(colorHome, 16);
            mTextHome.setBackgroundColor(iColorHome);
            //mTextHomeCount.setTextColor(iColorHome);
            mTextHomeCount.setTextColor(0xff000000);

            int iColorAway = 0xff000000 | Integer.parseUnsignedInt(colorAway, 16);
            mTextAway.setBackgroundColor(iColorAway);
            //mTextAwayCount.setTextColor(iColorAway);
            mTextAwayCount.setTextColor(0xff000000);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setEventColor(String colorHome, String fontHome, String colorAway, String fontAway){

        try{
            int iColorHome = 0xff000000 | Integer.parseUnsignedInt(colorHome, 16);
            mTextHome.setBackgroundColor(iColorHome);
            //mTextHomeCount.setTextColor(iColorHome);
            mTextHomeCount.setTextColor(0xff000000);

            int iFontHome = 0xff000000 | Integer.parseUnsignedInt(fontHome, 16);
            mTextHome.setTextColor(iFontHome);

            int iColorAway = 0xff000000 | Integer.parseUnsignedInt(colorAway, 16);
            mTextAway.setBackgroundColor(iColorAway);
            //mTextAwayCount.setTextColor(iColorAway);
            mTextAwayCount.setTextColor(0xff000000);

            int iFontAway = 0xff000000 | Integer.parseUnsignedInt(fontAway, 16);
            mTextAway.setTextColor(iFontAway);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.fab_web){
//                if(mainActivity.mWebViewState){
//                    mainActivity.mWebViewState = false;
//                    mWebView.setVisibility(View.GONE);
//                    mLinearEvent.setVisibility(View.VISIBLE);
//                }else{
//                    mainActivity.mWebViewState = true;
//                    mWebView.setVisibility(View.VISIBLE);
//                    mLinearEvent.setVisibility(View.GONE);
//                }

                String url = mLoadUrl;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

            }
            else if(v.getId() == R.id.screen_full_view){
                //mainActivity.setFullView(true);
                Intent intent = new Intent(mainActivity, FullVideoActivity.class);
                intent.putExtra("RunServerID", mainActivity.mServerId);
                intent.putExtra("EventRepeat", mainActivity.mEventRepeat);
                intent.putExtra("VideoVolume", mSeekVolume.getProgress());
                startActivity(intent);
            }
            else if(v.getId() == R.id.screen_full_port_view){
                //mainActivity.setFullView(true);
                Intent intent = new Intent(mainActivity, FullPortVideoActivity.class);
                intent.putExtra("RunServerID", mainActivity.mServerId);
                intent.putExtra("EventRepeat", mainActivity.mEventRepeat);
                intent.putExtra("VideoVolume", mSeekVolume.getProgress());
                startActivity(intent);
            }
        }
    };

    private View.OnTouchListener mOntouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            SeekBar seekBar = (SeekBar) view;
            int pos = seekBar.getProgress();
            mainActivity.updatePlayPos(pos);

//                int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
//                mediaPlayer.seekTo(playPosition);
//                textCurrentTime.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
            return false;
        }
    };



    View.OnKeyListener mOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            return false;
        }
    };

    public void updateTimer(RunEvent runEvent)
    {
        try{
            if(runEvent.getEventState().equalsIgnoreCase("STOP"))
            {
                updateTimerTextView(0);
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date startTime = sdf.parse(runEvent.getStartDateTime());

            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            //cal.add(Calendar.MINUTE, runEvent.getTriggerTime());
            cal.add(Calendar.SECOND, runEvent.getTriggerTime());
            Date endDate = cal.getTime();

            Date nowDate = Calendar.getInstance().getTime();

            long diffTime = endDate.getTime() - nowDate.getTime();
            if(diffTime <= 0) {
                updateTimerTextView(0);
            }else {
                second = (int)(diffTime / 1000);
                updateTimerTextView(second);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private String getTimeStr(int time)
    {
        Log.d("AAAA", "tiem : " + time);
        int min = time / 60;
        int sec = time % 60;
        String result = "" + min + "분" + sec + "초";
        return result;
    }

    private void updateTimerTextView(int time)
    {

        if(isVisible() && getActivity() != null)
        {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int min = time / 60;
                    int sec = time % 60;
                    if(min <= 0 && sec <= 0)
                    {
                        mTextTime.setText("이벤트 종료");
                        isRunning = false;
                    }
                    else {
                        mTextTime.setText("" + min + "분" + sec + "초");
                        isRunning = true;
                    }
                }
            });
        }
    }

    private void updateResultImageView(int home, int away)
    {
        if(isVisible() && getActivity() != null)
        {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String imageType = "IMAGE_DEFAULT";
                    if(home == away)
                    {
                        imageType = "IMAGE_DEFAULT";
                    }
                    else if(home > away)
                    {
                        imageType = "IMAGE_HOME";
                    }
                    else if(home < away)
                    {
                        imageType = "IMAGE_AWAY";
                    }
                    String viewImage = mainActivity.getTypeImage(mainActivity.mEventDto.getEventImageList(), imageType);
                    if(viewImage != null)
                    {
                        //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
                        Uri uri = mainActivity.getContentUri(viewImage);
                        mImgEvent.setImageURI(uri);
                        Glide.with(mainActivity).load(uri).into(mImgEvent);
                    }
                }
            });
        }
    }

//    private void updateTimeStr(RunEvent runEvent)
//    {
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//            Date startTime = sdf.parse(runEvent.getStartDateTime());
//
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(startTime);
//            cal.add(Calendar.MINUTE, runEvent.getVoteTime());
//            Date endDate = cal.getTime();
//
//            Date nowDate = Calendar.getInstance().getTime();
//
//            long diffTime = endDate.getTime() - nowDate.getTime();
//            if(diffTime <= 0)
//            {
//                mTextTime.setText("이벤트 종료");
//            }
//            else
//            {
//                isRunning = true;
//                second = (int)(diffTime / 1000);
//
//                TimerTask timerTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        if(second >= 0) {
//                            //1초씩 감소
//                            second--;
//                            updateTimerTextView(second);
//                            // 0분 이상이면
//                        }
//                        else
//                        {
//                            isRunning = false;
//                        }
//
//                        //mTextTime.setText(getTimeStr(second));
//                    }
//                };
//                timer.schedule(timerTask, 0, 1000);
//            }
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    private void updateScoreValue(RunEvent runEvent, final int home, final int away)
    {
        if(getActivity() != null)
        {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //mSeekVolume.setProgress(runEvent.getVolumeValue());

                    mTextHome.setText(""+home);
                    mTextAway.setText(""+away);
                    int homwVal = home;
                    int awayVal = away;
                    if(homwVal == awayVal && homwVal == 0)
                    {
                        homwVal = awayVal = 1;
                    }
                    //mTextCurrent.setText("이벤트 서버 "+name+" 실시간 응원 결과");
                    mTextCurrent.setText(runEvent.getServerName());
                    mTextHomeCount.setText(runEvent.getHomeName());
                    mTextAwayCount.setText(runEvent.getAwayName());

                    ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, homwVal);
                    mTextHome.setLayoutParams(params);

                    ViewGroup.LayoutParams params1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, awayVal);
                    mTextAway.setLayoutParams(params1);

                    String imageType = "IMAGE_DEFAULT";
                    if(home == away)
                    {
                        imageType = "IMAGE_DEFAULT";
                    }
                    else if(home > away)
                    {
                        imageType = "IMAGE_HOME";
                    }
                    else if(home < away)
                    {
                        imageType = "IMAGE_AWAY";
                    }
                    String viewImage = mainActivity.getTypeImage(mainActivity.mEventDto.getEventImageList(), imageType);
                    if(viewImage != null)
                    {
                        //Uri uri = Uri.parse(mainActivity.getContentUri(defImage));
                        Uri uri = mainActivity.getContentUri(viewImage);
                        mImgEvent.setImageURI(uri);
                        Glide.with(mainActivity).load(uri).into(mImgEvent);
                    }

//                    MainActivity activity = (MainActivity)getActivity();
//                    if(activity.mStrDefaultImg != null)
//                    {
//                        Uri uri = Uri.parse(activity.mStrDefaultImg);
//                        if(home > away)
//                        {
//                            uri = Uri.parse(activity.mStrHomeImg);
//                        }
//                        else if(home < away)
//                        {
//                            uri = Uri.parse(activity.mStrAwayImg);
//                        }
//                        mImgEvent.setImageURI(uri);
//                        Glide.with(activity).load(uri).into(mImgEvent);
//                    }

                }
            });
        }
    }

    private void updateScore(RunEvent runEvent)
    {
        Log.d("AAAA", "------- updateScore");
        try{
            int home = runEvent.getHomeCount();
            int away = runEvent.getAwayCount();
//            if(home == away && home == 0)
//            {
//                home = away = 1;
//            }
            Log.d("AAAA", "home : " + home);
            updateScoreValue(runEvent, home, away);

//            updateResultImageView(runEvent.getHomeCount(), runEvent.getAwayCount());
//            Timer timer = new Timer();
//            TimerTask timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    int eventId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_EVENT_ID, -1);
//                    mServer.eventNowResult(mEventResultCallBack, eventId);
//                }
//            };
//            if(runEvent.getEventState().equalsIgnoreCase("START")) {
//                timer.schedule(timerTask, 1000);
//            }else {
//                updateResultImageView(runEvent.getHomeCount(), runEvent.getAwayCount());
//                timer.cancel();
//            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private String milliSecondsToTimer(long milliSeconds) {

        String timerString = "";
        String secondsString;

        int hours = (int) (milliSeconds / (1000 * 60 * 60));
        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            timerString = hours + ":";
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        timerString = timerString + minutes + ":" + secondsString;
        return timerString;
    }

    public void updatePlayState(String title, long total, long current) {
        if( getActivity() != null) {
            //Log.d("AAAA", "----- updatePlayState is visible");
            //updateTimeStr(runEvent);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    songTitle.setText(title);
                    textCurrentTime.setText(milliSecondsToTimer(current));
                    textTotalDuration.setText(milliSecondsToTimer(total));
                    mSeekBarTime.setProgress((int) (((float) current / total) * 100));
                }
            });
        }else{
            Log.d("AAAA", "----- eventFragment is invisible");
        }
    }

    public void showProgress(final Activity act, final boolean bShow)
    {
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

//    private SECallBack<RunEventResult> mEventStateCallBack = new SECallBack<RunEventResult>()
//    {
//        @Override
//        public void onResponseResult(Response<RunEventResult> response)
//        {
//            if (response.isSuccessful())
//            {
//                RunEvent runEvent = response.body().getData();
//                updateTimeStr(runEvent);
//                updateScore(runEvent);
//            }
//            else
//            {
//                // no event
//            }
//            showProgress(getActivity(), false);
//        }
//    };


    private SECallBack<RunEventResult> mEventResultCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                if(isResumed())
                {
                    RunEvent runEvent = response.body().getData();
                    updateScore(runEvent);
                }
            }
            else
            {
                // no event
            }
        }
    };

    Player.Listener mPlayerListener = new Player.Listener() {
        @Override
        public void onEvents(Player player, Player.Events events) {
            Player.Listener.super.onEvents(player, events);
        }

        @Override
        public void onSeekProcessed() {
            Player.Listener.super.onSeekProcessed();
        }

        @Override
        public void onPlaybackStateChanged(int playbackState) {
            Player.Listener.super.onPlaybackStateChanged(playbackState);
//            if(playbackState == ExoPlayer.STATE_ENDED){
//                if(mainActivity.currentTriggerType == 1 && mainActivity.currentContType == 1){
//                    EventStartReqDto reqDto = new EventStartReqDto(mainActivity.mServerId, -1, -1, -1, -1, -1, -1);
//                    mServer.eventStart(mEventStartCallBack, reqDto);
//                    mainActivity.mRunEvent.setEventState("START");
//
//                    // update event button
//                    mainActivity.updateEventButton();
//                }
//            }
        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            Player.Listener.super.onIsPlayingChanged(isPlaying);
            if(isPlaying) {
                updateSeekBar();
            }
            else{
                handler.removeCallbacks(updater);

//                if(mainActivity.mRunEvent.getTriggerType() == 1 && mainActivity.mRunEvent.getContinuityType() == 1)
//                {
//                    try{
//                        EventStartReqDto reqDto = new EventStartReqDto(mainActivity.mServerId, -1, -1, -1, -1, -1, mainActivity.volumeValue);
//                        mServer.eventStart(mEventStartCallBack, reqDto);
//                    }catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
            }
        }

    };



    /// seekbar update
    private Handler handler = new Handler();
    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
        }
    };

    private void updateSeekBar() {
        if (exoPlayer != null && exoPlayer.isPlaying()) {
//            String title = intent.getStringExtra(MusicPlayService.EXTRA_MUSIC_TITLE);
//            int total = intent.getIntExtra(MusicPlayService.EXTRA_TOTAL_DURATION, 0);
//            int current = intent.getIntExtra(MusicPlayService.EXTRA_CURRENT_DURATION, 0);
            long total = exoPlayer.getDuration();
            long current = exoPlayer.getCurrentPosition();

            updatePlayState(mMusicTitle, total, current);
            handler.postDelayed(updater, 1000);

//            if(mainActivity.mEventDto.getTriggerType() == 0
//                    && mainActivity.mEventDto.getContinuityType() == 1 )

//            if(mainActivity.currentTriggerType == 0
//                    && mainActivity.currentContType == 1 )
//            {
//                long diff = (total - current) / 1000;
//                Log.d("AAAA", "total: "+total+" current: "+current+ " diff: " + diff);
//                Log.d("AAAA", "getTriggerTime: "+ mainActivity.mRunEvent.getTriggerTime());
//
//                if(diff < (mainActivity.mRunEvent.getTriggerTime() -1)
//                        && mainActivity.mRunEvent.getEventState().equalsIgnoreCase("STOP"))
//                {
//                    Log.d("AAAA", "getTriggerTime:--- start event ");
//                    EventStartReqDto reqDto = new EventStartReqDto(mainActivity.mServerId, -1, -1, -1, -1, -1, mainActivity.volumeValue);
//                    mServer.eventStart(mEventStartCallBack, reqDto);
//                    mainActivity.mRunEvent.setEventState("RESTART");
//
//                    // update Event Button
//                    mainActivity.updateEventButton();
//                }
//            }
        }
    }

    private SECallBack<RunEventResult> mEventStartCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful()) {
                RunEvent runEvent = response.body().getData();;
                mainActivity.mRunEvent = runEvent;

                mainActivity.settingFragment.updateEventState(runEvent);
                mainActivity.eventFragment.updateEventState(runEvent);

                mainActivity.updateEventInfo(runEvent);

                mainActivity.startEventStateCheck(runEvent.getId());
                //Toast.makeText(getApplicationContext(), "이벤트 를 시작 하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.d("AAAA", "----- Start Event fail : " );
            }
        }
    };

    private SECallBack<EventResult> mSetVolumeCallBack = new SECallBack<EventResult>()
    {
        @Override
        public void onResponseResult(Response<EventResult> response)
        {

        }
    };

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangedListner = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId == R.id.radio_chromakey_black)
                mEditor.putInt("bgcolor", 0);
            else if(checkedId == R.id.radio_chromakey_green)
                mEditor.putInt("bgcolor", 1);
            if(checkedId == R.id.radio_chromakey_blue)
                mEditor.putInt("bgcolor", 2);

            mEditor.apply();
        }

    };


}