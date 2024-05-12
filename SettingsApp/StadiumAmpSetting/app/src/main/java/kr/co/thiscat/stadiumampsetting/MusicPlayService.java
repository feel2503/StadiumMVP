package kr.co.thiscat.stadiumampsetting;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.File;

import kr.co.thiscat.stadiumampsetting.server.entity.RunEvent;

public class MusicPlayService extends Service {
    public final static String ACTION_PLAY_START = "android.intent.action.PLAY_START";
    public final static String ACTION_PLAY_STOP = "android.intent.action.PLAY_STOP";
    public final static String ACTION_PLAY_MOVE_POS = "android.intent.action.PLAY_MOVE_POS";
    public static final String EXTRA_FILE_URL = "android.intent.EXTRA_FILE_URL";
    public static final String EXTRA_IS_NEW = "android.intent.EXTRA_IS_NEW";
    public static final String EXTRA_UPDATE_POSITION = "android.intent.EXTRA_UPDATE_POSITION";

    public final static String ACTION_PLAY_START_RESULT = "android.intent.action.PLAY_START_RESULT";
    public final static String ACTION_UPDATE_CURRENT_POSITION = "android.intent.action.ACTION_UPDATE_CURRENT_POSITION";
    public static final String EXTRA_CURRENT_DURATION = "android.intent.EXTRA_CURRENT_DURATION";
    public static final String EXTRA_TOTAL_DURATION = "android.intent.EXTRA_TOTAL_DURATION";
    public static final String EXTRA_MUSIC_TITLE = "android.intent.EXTRA_MUSIC_TITLE";

    private MediaPlayer mMediaPlayer;
    private MediaPlayer mDefaultMediaPlayer;
    private int psusePos = 0;

    private Handler handler = new Handler();

    private String mMusicTitle;
    public MusicPlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PLAY_START);
        filter.addAction(ACTION_PLAY_STOP);
        filter.addAction(ACTION_PLAY_MOVE_POS);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageReceiver, filter);
        //getApplicationContext().registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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

    public void playMusic(String strUri, boolean isNew){
        Log.d("AAAA", "---- playMusic : " + strUri);
        try{
            if(strUri != null && strUri.length() > 0)
            {
                if(mDefaultMediaPlayer != null && mDefaultMediaPlayer.isPlaying()){
                    psusePos = mDefaultMediaPlayer.getCurrentPosition();
                    mDefaultMediaPlayer.pause();
                }

                if(isNew && mMediaPlayer != null && mMediaPlayer.isPlaying())
                {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }

                if(mMediaPlayer == null)
                    mMediaPlayer = new MediaPlayer();
                //strUri = "도시인.mp3";
                Uri uri = getContentUri(strUri);
                mMediaPlayer.setDataSource(getApplicationContext(), uri);

//                File medFile = new File(contentDirPath+strUri);
//                FileInputStream fs = new FileInputStream(medFile);
//                FileDescriptor fd = fs.getFD();
//                mMediaPlayer.reset();
//                mMediaPlayer.setDataSource(fd);

                mMediaPlayer.prepare();
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                        //startDefaultMediaPlayer();

                        handler.removeCallbacks(updater);
                    }
                });

                Intent intent = new Intent(MusicPlayService.ACTION_PLAY_START_RESULT);
                intent.putExtra(MusicPlayService.EXTRA_TOTAL_DURATION, mMediaPlayer.getDuration());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                updateSeekBar();
            }
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

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
        }
    };

    private void updateSeekBar() {
        if (mMediaPlayer.isPlaying()) {
            Intent intent = new Intent(MusicPlayService.ACTION_UPDATE_CURRENT_POSITION);
            intent.putExtra(MusicPlayService.EXTRA_TOTAL_DURATION, mMediaPlayer.getDuration());
            intent.putExtra(MusicPlayService.EXTRA_CURRENT_DURATION, mMediaPlayer.getCurrentPosition());
            intent.putExtra(MusicPlayService.EXTRA_MUSIC_TITLE, mMusicTitle);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

            handler.postDelayed(updater, 1000);
        }
    }



    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(action.equalsIgnoreCase(ACTION_PLAY_START))
            {
                String url = intent.getStringExtra(EXTRA_FILE_URL);
                boolean isNew = intent.getBooleanExtra(EXTRA_IS_NEW, false);
                mMusicTitle = url;
                playMusic(url, isNew);
            }
            else if(action.equalsIgnoreCase(ACTION_PLAY_STOP))
            {
                stopMusic();
            }
            else if(action.equalsIgnoreCase(ACTION_PLAY_MOVE_POS))
            {
                int pos = intent.getIntExtra(EXTRA_UPDATE_POSITION, 0);
                int playPosition = (mMediaPlayer.getDuration() / 100) * pos;
                mMediaPlayer.seekTo(playPosition);
                updateSeekBar();
            }

            Log.d("AAAA", "Got message: " + action);
        }
    };

}