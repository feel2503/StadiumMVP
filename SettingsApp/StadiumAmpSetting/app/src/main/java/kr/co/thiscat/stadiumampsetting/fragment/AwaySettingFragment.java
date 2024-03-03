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
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import kr.co.thiscat.stadiumampsetting.MainActivity;
import kr.co.thiscat.stadiumampsetting.PreferenceUtil;
import kr.co.thiscat.stadiumampsetting.R;
import kr.co.thiscat.stadiumampsetting.server.ServerManager;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventMusicDto;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AwaySettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AwaySettingFragment extends Fragment {

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


//    Timer timer;
    private static AwaySettingFragment mInstance;
    public TextView mTextAwayColor;
    public TextView mTextAwayFont;
    public TextView mTextAway1;
    public TextView mTextAway2;
    public TextView mTextAway3;
    public TextView mTextAway4;
    public TextView mTextAway5;
    public TextView mTextAway6;
    public TextView mTextAway7;
    public TextView mTextAway8;
    public TextView mTextAway9;
    public TextView mTextAway10;

    private MainActivity mainActivity;
    public AwaySettingFragment() {
        // Required empty public constructor
    }

    public static AwaySettingFragment getInstance()
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
    public static AwaySettingFragment newInstance(String param1, String param2) {
        AwaySettingFragment fragment = new AwaySettingFragment();
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
        View view = inflater.inflate(R.layout.fragment_awsy_setting, container, false);
        mTextAwayColor = view.findViewById(R.id.text_away_color);
        mTextAwayFont = view.findViewById(R.id.text_away_font);
        mTextAway1 = view.findViewById(R.id.text_away_1);
        mTextAway1.setOnClickListener(mOnClickListener);
        mTextAway2 = view.findViewById(R.id.text_away_2);
        mTextAway2.setOnClickListener(mOnClickListener);
        mTextAway3 = view.findViewById(R.id.text_away_3);
        mTextAway3.setOnClickListener(mOnClickListener);
        mTextAway4 = view.findViewById(R.id.text_away_4);
        mTextAway4.setOnClickListener(mOnClickListener);
        mTextAway5 = view.findViewById(R.id.text_away_5);
        mTextAway5.setOnClickListener(mOnClickListener);
        mTextAway6 = view.findViewById(R.id.text_away_6);
        mTextAway6.setOnClickListener(mOnClickListener);
        mTextAway7 = view.findViewById(R.id.text_away_7);
        mTextAway7.setOnClickListener(mOnClickListener);
        mTextAway8 = view.findViewById(R.id.text_away_8);
        mTextAway8.setOnClickListener(mOnClickListener);
        mTextAway9 = view.findViewById(R.id.text_away_9);
        mTextAway9.setOnClickListener(mOnClickListener);
        mTextAway10 = view.findViewById(R.id.text_away_10);
        mTextAway10.setOnClickListener(mOnClickListener);

        Log.d("AAAA", "---- AwaySettingFragment onCreateView");
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
                mTextAway1.setText(name);
                break;
            case 1:
                mTextAway2.setText(name);
                break;
            case 2:
                mTextAway3.setText(name);
                break;
            case 3:
                mTextAway4.setText(name);
                break;
            case 4:
                mTextAway5.setText(name);
                break;
            case 5:
                mTextAway6.setText(name);
                break;
            case 6:
                mTextAway7.setText(name);
                break;
            case 7:
                mTextAway8.setText(name);
                break;
            case 8:
                mTextAway9.setText(name);
                break;
            case 9:
                mTextAway10.setText(name);
                break;
        }
    }

//    public void setEventInfo(EventDto eventDto){
//        Log.d("AAAA", "---- AwaySettingFragment setEventInfo");
//        ArrayList<EventMusicDto> musicDtos = eventDto.getEventMusicList();
//        for(EventMusicDto musicDto : musicDtos){
//            if(musicDto.getTeamType().equalsIgnoreCase("TEAM_AWAY"))
//                setMusicSequenc(musicDto.getSequence(), musicDto.getMusicName());
//        }
//    }
    public void setEventInfo(ArrayList<EventMusicDto> musicDtos) {
        Log.d("AAAA", "---- AwaySettingFragment setEventInfo");
        for (EventMusicDto musicDto : musicDtos) {
            if (musicDto.getTeamType().equalsIgnoreCase("TEAM_AWAY"))
                setMusicSequenc(musicDto.getSequence(), musicDto.getMusicName());
        }
    }

    public void setEventColor(String color, String font){
        Log.d("AAAA", "---- HomeSettingFragment updateEventColor");
        try{
            int iColor = 0xff000000 | Integer.parseUnsignedInt(color, 16);
            mTextAwayColor.setTextColor(iColor);
            mTextAwayColor.setText(color);

//            int iFont = 0xff000000 | Integer.parseUnsignedInt(font, 16);
//            mTextAwayFont.setTextColor(iFont);
            mTextAwayFont.setText(font);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void startMediaActivity(int resId)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);;
        Uri uri = null;
        switch (resId){
            case R.id.text_away_1:
                uri = mainActivity.getContentUri(mTextAway1.getText().toString());
                break;
            case R.id.text_away_2:
                uri = mainActivity.getContentUri(mTextAway2.getText().toString());
                break;
            case R.id.text_away_3:
                uri = mainActivity.getContentUri(mTextAway3.getText().toString());
                break;
            case R.id.text_away_4:
                uri = mainActivity.getContentUri(mTextAway4.getText().toString());
                break;
            case R.id.text_away_5:
                uri = mainActivity.getContentUri(mTextAway5.getText().toString());
                break;
            case R.id.text_away_6:
                uri = mainActivity.getContentUri(mTextAway6.getText().toString());
                break;
            case R.id.text_away_7:
                uri = mainActivity.getContentUri(mTextAway7.getText().toString());
                break;
            case R.id.text_away_8:
                uri = mainActivity.getContentUri(mTextAway8.getText().toString());
                break;
            case R.id.text_away_9:
                uri = mainActivity.getContentUri(mTextAway9.getText().toString());
                break;
            case R.id.text_away_10:
                uri = mainActivity.getContentUri(mTextAway10.getText().toString());
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