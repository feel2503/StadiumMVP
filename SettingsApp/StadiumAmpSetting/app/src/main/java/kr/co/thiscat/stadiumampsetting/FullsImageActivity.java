package kr.co.thiscat.stadiumampsetting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
    private TextView mTextHome;
    private TextView mTextAway;

    public EventDto mEventDto = null;
    private RunEvent mRunEvent;

    private boolean isRunning = false;
    private Timer mTimer;

    private String viewImageType = "IMAGE_DEFAULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        binding = ActivityFullsImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mControlsView = binding.fullscreenContentControls;
        hide();

        mProgress = new ProgressDialog(getApplicationContext());

        mServerId = getIntent().getIntExtra("RunServerID", -1);
        mServer = ServerManager.getInstance(FullsImageActivity.this);
        initUi();
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
//        mServer.getRunEventState(mEventStateCallBack, mRunEventId);
        mServer.getEvent(mEventCallBack, mServerId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
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

//                                if(mEventDto.getContinuityType() == 1 )
//                                {
//                                    long delayTime = mEventDto.getContinuityTime() * 1000;
//                                    Log.d("AAAA", "delayTime : " + delayTime);
//                                    new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
//                                        @Override
//                                        public void run() {
//                                            //startEventStateCheck(mEventDto.getRunEvent());
//                                            mServer.getEvent(mEventCallBack, mServerId);
//                                        }
//                                    }, delayTime);
//                                }

                                setImageView01();
                            }
                        });
                        return;
                    }
                }
                mServer.getRunEventState(mEventStateCallBack, mRunEventId);
            }
        };
        mTimer.schedule(timerTask, 0, 1000);

    }

    private void updateScore(RunEvent runEvent)
    {
        try{
            int home = runEvent.getHomeCount();
            int away = runEvent.getAwayCount();
            updateScoreValue(runEvent.getServerName(), home, away);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void updateScoreValue(String name, final int home, final int away)
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
                mTextCurrent.setText("이벤트 서버 " + name + " 실시간 응원 결과");
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


    private SECallBack<EventResult> mEventCallBack = new SECallBack<EventResult>()
    {
        @Override
        public void onResponseResult(Response<EventResult> response)
        {
            if (response.isSuccessful())
            {
                mEventDto = response.body().getData();
                Log.d("AAAA", "EventState : " + mEventDto.getEventState());

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
                    Log.d("AAAA", "EventState : " + mRunEvent.getEventState());
                    if(mRunEvent.getEventState().equalsIgnoreCase("START"))
                    {
                        updateScore(mRunEvent);
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

    private SECallBack<RunEventResult> mEndEventStateCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                try{
                    mRunEvent = response.body().getData();
                    Log.d("AAAA", "EventState : " + mRunEvent.getEventState());
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
}