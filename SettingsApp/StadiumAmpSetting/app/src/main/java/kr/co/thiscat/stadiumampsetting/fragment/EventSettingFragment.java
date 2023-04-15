package kr.co.thiscat.stadiumampsetting.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.thiscat.stadiumampsetting.MainActivity;
import kr.co.thiscat.stadiumampsetting.PreferenceUtil;
import kr.co.thiscat.stadiumampsetting.R;
import kr.co.thiscat.stadiumampsetting.server.SECallBack;
import kr.co.thiscat.stadiumampsetting.server.ServerManager;
import kr.co.thiscat.stadiumampsetting.server.entity.ResultMsg;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEvent;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEventResult;
import kr.co.thiscat.stadiumampsetting.server.entity.StadiumServer;
import kr.co.thiscat.stadiumampsetting.server.entity.StadiumServerResult;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventSettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText mEditDefaultMedia;
    private TextView mTextDefaultLocal;
    private EditText mEditHomeMedia01;
    private TextView mTextHomeLocal01;
    private EditText mEditHomeMedia02;
    private TextView mTextHomeLocal02;
    private EditText mEditAwayMedia01;
    private TextView mTextAwayLocal01;
    private EditText mEditAwayMedia02;
    private TextView mTextAwayLocal02;
    private TextView mTextEventDefaultImg;
    private TextView mTextEventHomeImg;
    private TextView mTextEventAwayImg;

    private RadioGroup mRadioDefault;
    private RadioGroup mRadioHome1;
    private RadioGroup mRadioHome2;
    private RadioGroup mRadioAway1;
    private RadioGroup mRadioAway2;

    private TextView mTextConfirm;

    private ServerManager mServer;
    protected ProgressDialog mProgress = null;
    private PreferenceUtil mPreferenceUtil;

    private int mServerId;

    // file
    private String mStrDefault;
    private String mStrHome1;
    private String mStrHome2;
    private String mStrAway1;
    private String mStrAway2;
    private String mStrDefaultImg;
    private String mStrHomeImg;
    private String mStrAwayImg;

    public EventSettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventSettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventSettingFragment newInstance(String param1, String param2) {
        EventSettingFragment fragment = new EventSettingFragment();
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

        mPreferenceUtil = new PreferenceUtil(getContext());
        mProgress = new ProgressDialog(getContext());
        mServer = ServerManager.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_setting, container, false);
        init(view);

        mServerId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_SERVER_ID, -1);
        if(mServerId > 0)
        {
            showProgress(getActivity(), true);
            mServer.getServer(mServerCallBack, mServerId);
        }

        return view;
    }

    private void init(View view)
    {
        mEditDefaultMedia = view.findViewById(R.id.edit_default_media);
        mTextDefaultLocal = view.findViewById(R.id.text_default_local);
        mTextDefaultLocal.setOnClickListener(mOnClickListener);

        mEditHomeMedia01 = view.findViewById(R.id.edit_home_media_1);
        mTextHomeLocal01 = view.findViewById(R.id.text_home_local_1);
        mTextHomeLocal01.setOnClickListener(mOnClickListener);

        mEditHomeMedia02 = view.findViewById(R.id.edit_home_media_2);
        mTextHomeLocal02 = view.findViewById(R.id.text_home_local_2);
        mTextHomeLocal02.setOnClickListener(mOnClickListener);

        mEditAwayMedia01 = view.findViewById(R.id.edit_away_media_1);
        mTextAwayLocal01 = view.findViewById(R.id.text_away_local_1);
        mTextAwayLocal01.setOnClickListener(mOnClickListener);

        mEditAwayMedia02 = view.findViewById(R.id.edit_away_media_2);
        mTextAwayLocal02 = view.findViewById(R.id.text_away_local_2);
        mTextAwayLocal02.setOnClickListener(mOnClickListener);

        mTextEventDefaultImg = view.findViewById(R.id.text_event_default_image);
        mTextEventDefaultImg.setOnClickListener(mOnClickListener);
        mTextEventHomeImg = view.findViewById(R.id.text_event_home_image);
        mTextEventHomeImg.setOnClickListener(mOnClickListener);
        mTextEventAwayImg = view.findViewById(R.id.text_event_away_image);
        mTextEventAwayImg.setOnClickListener(mOnClickListener);

        mRadioDefault = view.findViewById(R.id.radio_default_music);
        mRadioDefault.check(R.id.radio_default_local);

        mRadioHome1 = view.findViewById(R.id.radio_home_music_1);
        mRadioHome1.check(R.id.radio_home_local_1);

        mRadioHome2 = view.findViewById(R.id.radio_home_music_2);
        mRadioHome2.check(R.id.radio_home_local_2);

        mRadioAway1 = view.findViewById(R.id.radio_away_music_1);
        mRadioAway1.check(R.id.radio_away_local_1);

        mRadioAway2 = view.findViewById(R.id.radio_away_music_2);
        mRadioAway2.check(R.id.radio_away_local_2);

        mTextConfirm = view.findViewById(R.id.text_event_setting_confirm);
        mTextConfirm.setOnClickListener(mOnClickListener);

        MainActivity activity = (MainActivity)getActivity();
        mStrDefault = activity.mStrDefault;
        mStrHome1 = activity.mStrHome1;
        mStrHome2 = activity.mStrHome2;
        mStrAway1 = activity.mStrAway1;
        mStrAway2 = activity.mStrAway2;
        mStrDefaultImg = activity.mStrDefaultImg;
        mStrHomeImg = activity.mStrHomeImg;
        mStrAwayImg = activity.mStrAwayImg;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mStrDefault != null)
            mTextDefaultLocal.setText(getFileNameFromUri(mStrDefault));
        if(mStrHome1 != null)
            mTextHomeLocal01.setText(getFileNameFromUri(mStrHome1));
        if(mStrHome2 != null)
            mTextHomeLocal02.setText(getFileNameFromUri(mStrHome2));
        if(mStrAway1 != null)
            mTextAwayLocal01.setText(getFileNameFromUri(mStrAway1));
        if(mStrAway2 != null)
            mTextAwayLocal02.setText(getFileNameFromUri(mStrAway2));
        if(mStrDefaultImg != null)
            mTextEventDefaultImg.setText(getFileNameFromUri(mStrDefaultImg));
        if(mStrHomeImg != null)
            mTextEventHomeImg.setText(getFileNameFromUri(mStrHomeImg));
        if(mStrAwayImg != null)
            mTextEventAwayImg.setText(getFileNameFromUri(mStrAwayImg));
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity activity = (MainActivity)getActivity();

        activity.mStrDefault = mStrDefault;
        activity.mStrHome1 = mStrHome1;
        activity.mStrHome2 = mStrHome2;
        activity.mStrAway1 = mStrAway1;
        activity.mStrAway2 = mStrAway2;
        activity.mStrDefaultImg = mStrDefaultImg;
        activity.mStrHomeImg = mStrHomeImg;
        activity.mStrAwayImg = mStrAwayImg;

    }

    private void initData(StadiumServer stadiumServer)
    {
        if(stadiumServer.getDefaultMusic() != null)
        {
            if(stadiumServer.getDefaultMusic().startsWith("http"))
            {
                mEditDefaultMedia.setText(stadiumServer.getDefaultMusic());
                mRadioDefault.check(R.id.radio_default_media);
            }
            else
            {
                mStrDefault = stadiumServer.getDefaultMusic();
                mTextDefaultLocal.setText(getFileNameFromUri(mStrDefault));
                mRadioDefault.check(R.id.radio_default_local);
            }

        }
        if(stadiumServer.getHomeMusic1() != null)
        {
            if(stadiumServer.getHomeMusic1().startsWith("http"))
            {
                mEditHomeMedia01.setText(stadiumServer.getHomeMusic1());
                mRadioHome1.check(R.id.radio_home_media_1);
            }
            else
            {
                mStrHome1 = stadiumServer.getHomeMusic1();
                mTextHomeLocal01.setText(getFileNameFromUri(mStrHome1));
                mRadioHome1.check(R.id.radio_home_local_1);
            }
        }
        if(stadiumServer.getHomeMusic2() != null)
        {
            if(stadiumServer.getHomeMusic2().startsWith("http"))
            {
                mEditHomeMedia02.setText(stadiumServer.getHomeMusic2());
                mRadioHome2.check(R.id.radio_home_media_2);
            }
            else
            {
                mStrHome2 = stadiumServer.getHomeMusic2();
                mTextHomeLocal02.setText(getFileNameFromUri(mStrHome2));
                mRadioHome2.check(R.id.radio_home_local_2);
            }
        }
        if(stadiumServer.getAwayMusic1() != null)
        {
            if(stadiumServer.getAwayMusic1().startsWith("http"))
            {
                mEditAwayMedia01.setText(stadiumServer.getAwayMusic1());
                mRadioAway1.check(R.id.radio_away_media_1);
            }
            else
            {
                mStrAway1 = stadiumServer.getAwayMusic1();
                mTextAwayLocal01.setText(getFileNameFromUri(mStrAway1));
                mRadioAway1.check(R.id.radio_away_media_1);
            }
        }
        if(stadiumServer.getAwayMusic2() != null)
        {
            if(stadiumServer.getAwayMusic2().startsWith("http"))
            {
                mEditAwayMedia02.setText(stadiumServer.getAwayMusic2());
                mRadioAway2.check(R.id.radio_away_media_2);
            }
            else
            {
                mStrAway2 = stadiumServer.getAwayMusic2();
                mTextAwayLocal02.setText(getFileNameFromUri(mStrAway2));
                mRadioAway2.check(R.id.radio_away_media_2);
            }
        }
        if(stadiumServer.getDefaultImage() != null)
        {
            mStrDefaultImg = stadiumServer.getDefaultImage();
            mTextEventDefaultImg.setText(getFileNameFromUri(mStrDefaultImg));
        }
        if(stadiumServer.getHomeImage() != null)
        {
            mStrHomeImg = stadiumServer.getHomeImage();
            mTextEventHomeImg.setText(getFileNameFromUri(mStrHomeImg));
        }
        if(stadiumServer.getAwayImage() != null)
        {
            mStrAwayImg = stadiumServer.getAwayImage();
            mTextEventAwayImg.setText(getFileNameFromUri(mStrAwayImg));
        }


    }

    private void openFileDialog(int reqCode, String type)
    {
        Intent intent = new Intent().setType(type)
                .setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select a file"), reqCode);
    }

    private String getFileNameFromUri(String strUri) {
        Uri uri = Uri.parse(strUri);
        return getFileNameFromUri(uri);
    }
    @SuppressLint("Range")
    private String getFileNameFromUri(Uri uri) {
        try{
            String fileName = "";
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                //Log.i(TAG, "Display Name: " + fileName);
            }
            cursor.close();

            return fileName;
        }catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            Uri selectedfile = data.getData(); //The uri with the location of the file
            getActivity().getContentResolver().takePersistableUriPermission(selectedfile, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if(requestCode == R.id.text_default_local) {
                mStrDefault = selectedfile.toString();
                mTextDefaultLocal.setText(getFileNameFromUri(selectedfile));
                mRadioDefault.check(R.id.radio_default_local);
//                try{
//                    MediaPlayer mediaPlayer = new MediaPlayer();
//
//                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    mediaPlayer.setDataSource(getActivity(), selectedfile);
//                    mediaPlayer.prepare();
//                    mediaPlayer.start();
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
            }
            else if(requestCode == R.id.text_home_local_1) {
                mStrHome1 = selectedfile.toString();
                mTextHomeLocal01.setText(getFileNameFromUri(selectedfile));
                mRadioHome1.check(R.id.radio_home_local_1);
            }
            else if(requestCode == R.id.text_home_local_2) {
                mStrHome2 = selectedfile.toString();
                mTextHomeLocal02.setText(getFileNameFromUri(selectedfile));
                mRadioHome2.check(R.id.radio_home_local_2);
            }
            else if(requestCode == R.id.text_away_local_1) {
                mStrAway1 = selectedfile.toString();
                mTextAwayLocal01.setText(getFileNameFromUri(selectedfile));
                mRadioAway1.check(R.id.radio_away_local_1);
            }
            else if(requestCode == R.id.text_away_local_2) {
                mStrAway2 = selectedfile.toString();
                mTextAwayLocal02.setText(getFileNameFromUri(selectedfile));
                mRadioAway2.check(R.id.radio_away_local_2);
            }
            else if(requestCode == R.id.text_event_default_image) {
                mStrDefaultImg = selectedfile.toString();
                mTextEventDefaultImg.setText(getFileNameFromUri(selectedfile));
            }
            else if(requestCode == R.id.text_event_home_image) {
                mStrHomeImg = selectedfile.toString();
                mTextEventHomeImg.setText(getFileNameFromUri(selectedfile));
            }
            else if(requestCode == R.id.text_event_away_image) {
                mStrAwayImg = selectedfile.toString();
                mTextEventAwayImg.setText(getFileNameFromUri(selectedfile));
            }
        }

//        if(requestCode == R.id.text_default_local && requestCode == Activity.RESULT_OK) {
//            Uri selectedfile = data.getData(); //The uri with the location of the file
//            mTextDefaultLocal.setText(selectedfile.toString());
//            // 경로 정보:  selectedfile.getPath()
//            // 전체 URI 정보: selectedfile.toString()
//            Toast.makeText(getContext(), getFileNameFromUri(selectedfile), Toast.LENGTH_LONG).show();
//            //Log.d(TAG, "Selected: " + selectedfile.toString());
//        }

    }

    private void updateServer(int serverId)
    {
        StadiumServer stadiumServer = new StadiumServer();
        stadiumServer.setId(serverId);
        if(mRadioDefault.getCheckedRadioButtonId() == R.id.radio_default_media)
            stadiumServer.setDefaultMusic(mEditDefaultMedia.getText().toString());
        else
            stadiumServer.setDefaultMusic(mStrDefault);

        if(mRadioHome1.getCheckedRadioButtonId() == R.id.radio_home_media_1)
            stadiumServer.setHomeMusic1(mEditHomeMedia01.getText().toString());
        else
            stadiumServer.setHomeMusic1(mStrHome1);

        if(mRadioHome2.getCheckedRadioButtonId() == R.id.radio_home_media_2)
            stadiumServer.setHomeMusic2(mEditHomeMedia02.getText().toString());
        else
            stadiumServer.setHomeMusic2(mStrHome2);

        if(mRadioAway1.getCheckedRadioButtonId() == R.id.radio_away_media_1)
            stadiumServer.setAwayMusic1(mEditAwayMedia01.getText().toString());
        else
            stadiumServer.setAwayMusic1(mStrAway1);

        if(mRadioAway2.getCheckedRadioButtonId() == R.id.radio_away_media_2)
            stadiumServer.setAwayMusic2(mEditAwayMedia02.getText().toString());
        else
            stadiumServer.setAwayMusic2(mStrAway2);

        if(mStrDefaultImg.startsWith("content"))
            stadiumServer.setDefaultImage(mStrDefaultImg);
        if(mStrHomeImg.startsWith("content"))
            stadiumServer.setHomeImage(mStrHomeImg);
        if(mStrAwayImg.startsWith("content"))
            stadiumServer.setAwayImage(mStrAwayImg);

        showProgress(getActivity(), true);
        mServer.serverUpdate(mServerUpdateCallBack, stadiumServer);
    }
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.text_event_setting_confirm)
            {
                if(mServerId < 1)
                {
                    Toast.makeText(getContext(), "서버를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateServer(mServerId);
            }
            else if(v.getId() == R.id.text_default_local || v.getId() == R.id.text_home_local_1 || v.getId() == R.id.text_home_local_2
                || v.getId() == R.id.text_away_local_1 || v.getId() == R.id.text_away_local_2 )
            {
                openFileDialog(v.getId(), "audio/*");
            }
            else if(v.getId() == R.id.text_event_default_image || v.getId() == R.id.text_event_home_image
                || v.getId() == R.id.text_event_away_image)
            {
                openFileDialog(v.getId(), "image/*");
            }

        }
    };

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

    private SECallBack<ResultMsg> mServerUpdateCallBack = new SECallBack<ResultMsg>()
    {
        @Override
        public void onResponseResult(Response<ResultMsg> response)
        {
            if (response.isSuccessful())
            {
                //RunEvent runEvent = response.body().getData();

            }
            else
            {
                // no event
            }
            showProgress(getActivity(), false);
        }
    };

    private SECallBack<StadiumServerResult> mServerCallBack = new SECallBack<StadiumServerResult>()
    {
        @Override
        public void onResponseResult(Response<StadiumServerResult> response)
        {
            if (response.isSuccessful())
            {
                //StadiumServer stadiumServer = response.body().getData();

                ArrayList<StadiumServer>  serverArrayList = response.body().getData();
                initData(serverArrayList.get(0));
            }
            else
            {
                // no event
            }
            showProgress(getActivity(), false);
        }
    };
}