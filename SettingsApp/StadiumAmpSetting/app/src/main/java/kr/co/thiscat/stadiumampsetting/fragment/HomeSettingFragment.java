package kr.co.thiscat.stadiumampsetting.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.thiscat.stadiumampsetting.MainActivity;
import kr.co.thiscat.stadiumampsetting.PreferenceUtil;
import kr.co.thiscat.stadiumampsetting.R;
import kr.co.thiscat.stadiumampsetting.server.SECallBack;
import kr.co.thiscat.stadiumampsetting.server.ServerManager;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEvent;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEventResult;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventMusicDto;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeSettingFragment extends Fragment {

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

    public TextView mTextHome1;
    public TextView mTextHome2;
    public TextView mTextHome3;
    public TextView mTextHome4;
    public TextView mTextHome5;
    public TextView mTextHome6;
    public TextView mTextHome7;
    public TextView mTextHome8;
    public TextView mTextHome9;
    public TextView mTextHome10;
//    Timer timer;
    private static HomeSettingFragment mInstance;

    private MainActivity mainActivity;
    public HomeSettingFragment() {
        // Required empty public constructor
    }

    public static HomeSettingFragment getInstance()
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
    public static HomeSettingFragment newInstance(String param1, String param2) {
        HomeSettingFragment fragment = new HomeSettingFragment();
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
//        timer = new Timer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_setting, container, false);
        mTextHome1 = view.findViewById(R.id.text_home_1);
        mTextHome1.setOnClickListener(mOnClickListener);
        mTextHome2 = view.findViewById(R.id.text_home_2);
        mTextHome2.setOnClickListener(mOnClickListener);
        mTextHome3 = view.findViewById(R.id.text_home_3);
        mTextHome3.setOnClickListener(mOnClickListener);
        mTextHome4 = view.findViewById(R.id.text_home_4);
        mTextHome4.setOnClickListener(mOnClickListener);
        mTextHome5 = view.findViewById(R.id.text_home_5);
        mTextHome5.setOnClickListener(mOnClickListener);
        mTextHome6 = view.findViewById(R.id.text_home_6);
        mTextHome6.setOnClickListener(mOnClickListener);
        mTextHome7 = view.findViewById(R.id.text_home_7);
        mTextHome7.setOnClickListener(mOnClickListener);
        mTextHome8 = view.findViewById(R.id.text_home_8);
        mTextHome8.setOnClickListener(mOnClickListener);
        mTextHome9 = view.findViewById(R.id.text_home_9);
        mTextHome9.setOnClickListener(mOnClickListener);
        mTextHome10 = view.findViewById(R.id.text_home_10);
        mTextHome10.setOnClickListener(mOnClickListener);

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();
//        timer.cancel();
    }

    private void setMusicSequenc(int sequenc, String name){
        switch (sequenc){
            case 0:
                mTextHome1.setText(name);
                break;
            case 1:
                mTextHome2.setText(name);
                break;
            case 2:
                mTextHome3.setText(name);
                break;
            case 3:
                mTextHome4.setText(name);
                break;
            case 4:
                mTextHome5.setText(name);
                break;
            case 5:
                mTextHome6.setText(name);
                break;
            case 6:
                mTextHome7.setText(name);
                break;
            case 7:
                mTextHome8.setText(name);
                break;
            case 8:
                mTextHome9.setText(name);
                break;
            case 9:
                mTextHome10.setText(name);
                break;
        }
    }

//    public void setEventInfo(EventDto eventDto){
//        Log.d("AAAA", "---- HomeSettingFragment setEventInfo");
//        ArrayList<EventMusicDto> musicDtos = eventDto.getEventMusicList();
//        for(EventMusicDto musicDto : musicDtos){
//            if(musicDto.getTeamType().equalsIgnoreCase("TEAM_HOME"))
//                setMusicSequenc(musicDto.getSequence(), musicDto.getMusicName());
//        }
//    }
    public void setEventInfo(ArrayList<EventMusicDto> musicDtos){
        Log.d("AAAA", "---- HomeSettingFragment setEventInfo");
        for(EventMusicDto musicDto : musicDtos){
            if(musicDto.getTeamType().equalsIgnoreCase("TEAM_HOME"))
                setMusicSequenc(musicDto.getSequence(), musicDto.getMusicName());
        }
    }


    public void startMediaActivity(int resId)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);;
        Uri uri = null;
        switch (resId){
            case R.id.text_home_1:
                uri = mainActivity.getContentUri(mTextHome1.getText().toString());
                break;
            case R.id.text_home_2:
                uri = mainActivity.getContentUri(mTextHome2.getText().toString());
                break;
            case R.id.text_home_3:
                uri = mainActivity.getContentUri(mTextHome3.getText().toString());
                break;
            case R.id.text_home_4:
                uri = mainActivity.getContentUri(mTextHome4.getText().toString());
                break;
            case R.id.text_home_5:
                uri = mainActivity.getContentUri(mTextHome5.getText().toString());
                break;
            case R.id.text_home_6:
                uri = mainActivity.getContentUri(mTextHome6.getText().toString());
                break;
            case R.id.text_home_7:
                uri = mainActivity.getContentUri(mTextHome7.getText().toString());
                break;
            case R.id.text_home_8:
                uri = mainActivity.getContentUri(mTextHome8.getText().toString());
                break;
            case R.id.text_home_9:
                uri = mainActivity.getContentUri(mTextHome9.getText().toString());
                break;
            case R.id.text_home_10:
                uri = mainActivity.getContentUri(mTextHome10.getText().toString());
                break;
        }

        if(uri != null){
            intent.setDataAndType(uri, "audio/mp3");
            startActivity(intent);
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

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startMediaActivity(v.getId());
        }
    };

}