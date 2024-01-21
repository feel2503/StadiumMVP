package kr.co.thiscat.stadiumampsetting;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.File;

import kr.co.thiscat.stadiumampsetting.server.entity.RunEvent;

public class MusicPlayService extends Service {
    public final static String ACTION_PLAY_START = "android.intent.action.PLAY_START";
    public final static String ACTION_PLAY_STOP = "android.intent.action.PLAY_STOP";
    public static final String EXTRA_FILE_URL = "android.intent.EXTRA_FILE_URL";
    public static final String EXTRA_IS_NEW = "android.intent.EXTRA_IS_NEW";

    private MediaPlayer mMediaPlayer;
    private MediaPlayer mDefaultMediaPlayer;
    private int psusePos = 0;


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
                    }
                });
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



    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(action.equalsIgnoreCase(ACTION_PLAY_START))
            {
                String url = intent.getStringExtra(EXTRA_FILE_URL);
                boolean isNew = intent.getBooleanExtra(EXTRA_IS_NEW, false);
                playMusic(url, isNew);
            }
            else if(action.equalsIgnoreCase(ACTION_PLAY_STOP))
            {
                stopMusic();
            }

            Log.d("AAAA", "Got message: " + action);
        }
    };

}