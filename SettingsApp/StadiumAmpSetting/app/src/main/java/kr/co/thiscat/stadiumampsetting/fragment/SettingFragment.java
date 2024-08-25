package kr.co.thiscat.stadiumampsetting.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.thiscat.stadiumampsetting.DialogServerSelect;
import kr.co.thiscat.stadiumampsetting.MainActivity;
import kr.co.thiscat.stadiumampsetting.PreferenceUtil;
import kr.co.thiscat.stadiumampsetting.R;
import kr.co.thiscat.stadiumampsetting.server.SECallBack;
import kr.co.thiscat.stadiumampsetting.server.ServerManager;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEvent;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEventResult;
import kr.co.thiscat.stadiumampsetting.server.entity.StadiumServer;
import kr.co.thiscat.stadiumampsetting.server.entity.StadiumServerResult;
import kr.co.thiscat.stadiumampsetting.server.entity.StartEvent;
import kr.co.thiscat.stadiumampsetting.server.entity.result.EventListResult;
import kr.co.thiscat.stadiumampsetting.server.entity.result.EventResult;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventImageDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventStartReqDto;
import kr.co.thiscat.stadiumampsetting.server.entity.v2.EventcontinuityTypeDto;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PreferenceUtil mPreferenceUtil;
    //private int mServerId;
    //private int mEventId;
    private boolean mEventIsRunning = false;

    private ServerManager mServer;
    protected ProgressDialog mProgress = null;

    private ArrayList<EventDto> mServerList;

    private TextView mTextServrName;
    private RadioGroup mRadioTrigger;
    private EditText mEditTriggerTime;
    private EditText mEditTriggerVote;
    private TextView mTextDefaultImage;
    private TextView mTextHomeImage;
    private TextView mTextAwayImage;
    private TextView mEditWebUrl;
    private TextView mEditOpenChat;
    private TextView mTextQrImage;
    private TextView mTextAdvImage;

    private CheckBox mCheckContinuous;
    //private EditText mEditEventDelay;

    private TextView mTextEventStart;
    private TextView mTextEventStop;

    private static SettingFragment mInstance;
    private MainActivity mainActivity;
    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment getInstance()
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
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        initView(view);

        mServer.getServerList(mServerListCallBack);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        mServerId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_SERVER_ID, -1);
//        mEventId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_EVENT_ID, -1);
//        if(mainActivity.mServerId > 0)
//        {
//            String name = mPreferenceUtil.getStringPreference(PreferenceUtil.KEY_SERVER_NAME);
//            mTextServrName.setText("이벤트 서버 : " + name);
//            if(mainActivity.mEventId > 0)
//            {
//                mServer.getEventState(mEventStateCallBack, mainActivity.mEventId);
//                showProgress(getActivity(), true);
//            }
//            else
//            {
//                mServer.getLastEvent(mEventStateCallBack, mainActivity.mServerId);
//                showProgress(getActivity(), true);
//            }
//        }
    }

    private void initView(View view)
    {
        mTextServrName = view.findViewById(R.id.text_event_select);
        mTextServrName.setOnClickListener(mOnClicklistener);

        mRadioTrigger = view.findViewById(R.id.radio_vote);
        mRadioTrigger.setOnCheckedChangeListener(mOnCheckChangeLister);
        mEditTriggerTime = view.findViewById(R.id.edit_only_sec);
        mEditTriggerVote = view.findViewById(R.id.edit_only_count);

        mTextDefaultImage = view.findViewById(R.id.text_event_default_image);
        mTextDefaultImage.setOnClickListener(mOnClickListener);

        mTextHomeImage = view.findViewById(R.id.text_event_home_image);
        mTextHomeImage.setOnClickListener(mOnClickListener);

        mTextAwayImage = view.findViewById(R.id.text_event_away_image);
        mTextAwayImage.setOnClickListener(mOnClickListener);

        mEditWebUrl = view.findViewById(R.id.text_web_stat_url);
        mEditWebUrl.setOnClickListener(mOnClickListener);
        mEditOpenChat = view.findViewById(R.id.text_open_chat_url);
        mEditOpenChat.setOnClickListener(mOnClickListener);

        mTextQrImage = view.findViewById(R.id.text_event_qr_image);
        mTextQrImage.setOnClickListener(mOnClickListener);

        mTextAdvImage = view.findViewById(R.id.text_event_adv_image);
        mTextAdvImage.setOnClickListener(mOnClickListener);

        mCheckContinuous = view.findViewById(R.id.check_event_continuous);
        mCheckContinuous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int type = isChecked ? 1 : 0;
                EventcontinuityTypeDto eventcontinuityTypeDto = new EventcontinuityTypeDto(mainActivity.mServerId, type);
                mServer.setContinuityType(mContinuityTypeCallBack, eventcontinuityTypeDto);
            }
        });
        //mEditEventDelay = view.findViewById(R.id.edit_delay_sec);

        mTextEventStart = view.findViewById(R.id.text_event_start);
        mTextEventStart.setOnClickListener(mOnClicklistener);
        mTextEventStop = view.findViewById(R.id.text_event_stop);
        mTextEventStop.setOnClickListener(mOnClicklistener);

//        mTextServrName = view.findViewById(R.id.text_event_server);
//        mRadioVote = view.findViewById(R.id.radio_vote);
    }


    public void setEventInfo(EventDto eventDto){
        Log.d("AAAA", "---- SettingFragment setEventInfo");
        mTextServrName.setText(eventDto.getEventName());
        //trigger type
        if(eventDto.getTriggerType() == 0){
            mRadioTrigger.check(R.id.radio_only_time);
        }else{
            mRadioTrigger.check(R.id.radio_only_count);
        }
        mEditTriggerTime.setText(""+eventDto.getTriggerTime());
        mEditTriggerVote.setText(""+eventDto.getTriggerVote());
        // 이벤트 연속진행
        if(eventDto.getContinuityType() == 0)
            mCheckContinuous.setChecked(false);
        else
            mCheckContinuous.setChecked(true);
//        mEditEventDelay.setText(""+eventDto.getContinuityTime());

        // image
        ArrayList<EventImageDto> imgList = eventDto.getEventImageList();
        for(EventImageDto imageDto : imgList){
            if(imageDto.getImageType().equalsIgnoreCase("IMAGE_DEFAULT"))
                mTextDefaultImage.setText(imageDto.getImageName());
            else if(imageDto.getImageType().equalsIgnoreCase("IMAGE_HOME"))
                mTextHomeImage.setText(imageDto.getImageName());
            else if(imageDto.getImageType().equalsIgnoreCase("IMAGE_AWAY"))
                mTextAwayImage.setText(imageDto.getImageName());
            else if(imageDto.getImageType().equalsIgnoreCase("IMAGE_QR"))
                mTextQrImage.setText(imageDto.getImageName());
            else if(imageDto.getImageType().equalsIgnoreCase("IMAGE_ADV"))
                mTextAdvImage.setText(imageDto.getImageName());
        }
        mEditWebUrl.setText(eventDto.getWebUrl());
        mEditOpenChat.setText(eventDto.getOpenchatUrl());
    }

    private void setEventBtnState(boolean isRunning)
    {
        mEventIsRunning = isRunning;
        if(isRunning)
        {
            mTextEventStart.setBackgroundResource(R.drawable.bg_round_gray);
            mTextEventStart.setTextColor(0xFFC2C2C2);

            mTextEventStop.setBackgroundResource(R.drawable.bg_round_06141f);
            mTextEventStop.setTextColor(0xFFFFFFFF);
        }
        else
        {
            mTextEventStart.setBackgroundResource(R.drawable.bg_round_06141f);
            mTextEventStart.setTextColor(0xFFFFFFFF);

            mTextEventStop.setBackgroundResource(R.drawable.bg_round_gray);
            mTextEventStop.setTextColor(0xFFC2C2C2);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startMediaActivity(v.getId());
        }
    };

    public void startMediaActivity(int resId)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);;
        Uri uri = null;
        if (resId == R.id.text_event_default_image) {
            uri = mainActivity.getContentUri(mTextDefaultImage.getText().toString());
            intent.setDataAndType(uri, "image/*");
        } else if (resId == R.id.text_event_home_image) {
            uri = mainActivity.getContentUri(mTextHomeImage.getText().toString());
            intent.setDataAndType(uri, "image/*");
        } else if (resId == R.id.text_event_away_image) {
            uri = mainActivity.getContentUri(mTextAwayImage.getText().toString());
            intent.setDataAndType(uri, "image/*");
        } else if (resId == R.id.text_event_qr_image) {
            uri = mainActivity.getContentUri(mTextQrImage.getText().toString());
            intent.setDataAndType(uri, "image/*");
        } else if (resId == R.id.text_event_adv_image) {
            uri = mainActivity.getContentUri(mTextAdvImage.getText().toString());
            intent.setDataAndType(uri, "image/*");
        } else if (resId == R.id.text_web_stat_url) {
            uri = Uri.parse(mEditWebUrl.getText().toString());
            intent.setData(uri);
        } else if (resId == R.id.text_open_chat_url) {
            uri = Uri.parse(mEditOpenChat.getText().toString());
            intent.setData(uri);
        }

        if(uri != null){
            startActivity(intent);
        }

    }

    private int getVoteTime()
    {
//        if(mRadioVote.getCheckedRadioButtonId() == R.id.radio_vote_3m)
//            return 3;
//        else if(mRadioVote.getCheckedRadioButtonId() == R.id.radio_vote_5m)
//            return 5;
//        else
//            return -1;
        return -1;
    }

    private int getResultTime()
    {
//        if(mRadioResult.getCheckedRadioButtonId() == R.id.radio_result_1m)
//            return 1;
//        else if(mRadioResult.getCheckedRadioButtonId() == R.id.radio_result_3m)
//            return 3;
//        else
//            return -1;
        return -1;
    }

    public void updateEventInfo(RunEvent runEvent)
    {
        //trigger type
        if(runEvent.getTriggerType() == 0){
            mRadioTrigger.check(R.id.radio_only_time);
        }else{
            mRadioTrigger.check(R.id.radio_only_count);
        }
        mEditTriggerTime.setText(""+runEvent.getTriggerTime());
        mEditTriggerVote.setText(""+runEvent.getTriggerVote());
        // 이벤트 연속진행
        if(runEvent.getContinuityType() == 0)
            mCheckContinuous.setChecked(false);
        else
            mCheckContinuous.setChecked(true);
//        mEditEventDelay.setText(""+runEvent.getContinuityTime());
        // image
        ArrayList<EventImageDto> imgList = runEvent.getEventImageList();
        for(EventImageDto imageDto : imgList){
            if(imageDto.getImageType().equalsIgnoreCase("IMAGE_DEFAULT"))
                mTextDefaultImage.setText(imageDto.getImageName());
            else if(imageDto.getImageType().equalsIgnoreCase("IMAGE_HOME"))
                mTextHomeImage.setText(imageDto.getImageName());
            else if(imageDto.getImageType().equalsIgnoreCase("IMAGE_AWAY"))
                mTextAwayImage.setText(imageDto.getImageName());
            else if(imageDto.getImageType().equalsIgnoreCase("IMAGE_QR"))
                mTextQrImage.setText(imageDto.getImageName());
            else if(imageDto.getImageType().equalsIgnoreCase("IMAGE_ADV"))
                mTextAdvImage.setText(imageDto.getImageName());
        }
        mEditWebUrl.setText(runEvent.getWebUrl());
    }
    public void updateEventState(RunEvent runEvent)
    {
        if(getActivity() != null)
        {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(runEvent.getEventState().equalsIgnoreCase("START")){
                        setEventBtnState(true);
                    }else{
                        setEventBtnState(false);
                    }
                }
            });
        }
    }

    private void saveEventInfo(int eventId)
    {
        mainActivity.mRunEventId = eventId;
        mPreferenceUtil.putIntPreference(PreferenceUtil.KEY_EVENT_ID, eventId);
    }

    private void showEventServerSelector()
    {
        ArrayList<String> items = new ArrayList<>();
        for(EventDto eventDto : mServerList)
        {
            items.add(eventDto.getEventName());
        }
        ArrayList<Integer> selectItem = new ArrayList<>(); // RadioButton 선택 한 값 담을 ArrayList ( TEXT )
        selectItem.clear();
        if(mainActivity.mServerId > 0) {
            selectItem.add(mainActivity.mServerId-1);
        } else {
            selectItem.add(0);
        }

        // Alert 선언
        new AlertDialog.Builder(getContext())
                .setTitle("서버 선택")
                .setSingleChoiceItems(items.toArray(new String[items.size()]), selectItem.get(0), new DialogInterface.OnClickListener() {
                    // setSingleChoiceItems 가 RadioGroup, RadioButton 을 해준다.
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectItem.clear(); // 하나의 값만 저장 되도록 모든 값 지워준다.
                        selectItem.add(i); // 선택한 값 저장.
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    // 확인 버튼을 클릭 했을때 이벤트
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int selIdx = selectItem.get(0);
                        String name = mServerList.get(selIdx).getEventName();
                        int serverId = mServerList.get(selIdx).getEventId();
                        mPreferenceUtil.putIntPreference(PreferenceUtil.KEY_SERVER_ID, serverId);
                        mPreferenceUtil.putStringPrefrence(PreferenceUtil.KEY_SERVER_NAME, name);
                        mainActivity.mServerId = serverId;
                        mTextServrName.setText("이벤트 서버 : " + name);

                        //mServer.getLastEvent(mLastEventCallBack, serverId);
                        mainActivity.initEventInfo();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    public RadioGroup.OnCheckedChangeListener mOnCheckChangeLister = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

        }
    };
    public View.OnClickListener mOnClicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.text_event_select)
            {
                showEventServerSelector();
            }
            else if(v.getId() == R.id.text_event_start)
            {
                if(mEventIsRunning)
                {
                    Toast.makeText(getContext(), "이벤트가 진행중 입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //mainActivity.stopMusic();
                if(mainActivity.mServerId < 0)
                {
                    new AlertDialog.Builder(getContext())
                            .setMessage("이벤트 서버가 설정되지 않았습니다.\n이벤트 서버를 선택 하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showEventServerSelector();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .create()
                            .show();

                    //Toast.makeText(getContext(), "이벤트 서버를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgress(getActivity(), true);

                try{
                    int eventId = mainActivity.mServerId;
                    int triggerType = mRadioTrigger.getCheckedRadioButtonId() == R.id.radio_only_time ? 0 : 1;
                    int triggerTime = Integer.parseInt(mEditTriggerTime.getText().toString());
                    int triggerVote = Integer.parseInt(mEditTriggerVote.getText().toString());;
                    int continuityType = mCheckContinuous.isChecked() ? 1 : 0;
                    //int continuityTime = Integer.parseInt(mEditEventDelay.getText().toString());
                    int continuityTime = -1;
                    int volumeValeu = mainActivity.volumeValue;
                    EventStartReqDto reqDto = new EventStartReqDto(eventId, triggerType, triggerTime,
                            triggerVote, continuityType, continuityTime, volumeValeu);

                    mServer.eventStart(mEventStartCallBack, reqDto);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            else if(v.getId() == R.id.text_event_stop)
            {
                if(!mEventIsRunning)
                    return;

                new AlertDialog.Builder(getContext())
                        .setTitle("투표 이벤트 종료")
                        .setMessage("투표 이벤트를 종료 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mServer.eventStop(mEventStoptCallBack, mainActivity.mRunEventId);
                                mainActivity.currentContType = 0;
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
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

    private SECallBack<EventListResult> mServerListCallBack = new SECallBack<EventListResult>()
    {
        @Override
        public void onResponseResult(Response<EventListResult> response)
        {
            if (response.isSuccessful())
            {
                mServerList = response.body().getData();
                int i = 0;
            }
        }
    };

    private SECallBack<RunEventResult> mLastEventCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                RunEvent runEvent = response.body().getData();
                updateEventState(runEvent);
                saveEventInfo((int)runEvent.getId());
            }
            else
            {
                // no event
//                mRadioVote.check(R.id.radio_vote_3m);
//                mRadioResult.check(R.id.radio_result_1m);
//                mainActivity.mEventId = -1;
//                mPreferenceUtil.putIntPreference(PreferenceUtil.KEY_EVENT_ID, mainActivity.mEventId);
            }
            showProgress(getActivity(), false);
        }
    };

    private SECallBack<RunEventResult> mEventStartCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                mainActivity.mEventRepeat = true;
                RunEvent runEvent = response.body().getData();
                updateEventState(runEvent);
//                saveEventInfo((int)runEvent.getId());
                mainActivity.startEventStateCheck(runEvent.getId());

                mainActivity.currentContType = runEvent.getContinuityType();
                mainActivity.currentTriggerType = runEvent.getTriggerType();

//                MainActivity activity = (MainActivity) getActivity();
//                if(activity != null)
//                    activity.startEventCount();

//                Toast.makeText(getContext(), "이벤트 를 시작 하였습니다.", Toast.LENGTH_SHORT).show();
            }

            showProgress(getActivity(), false);
        }
    };

    private SECallBack<RunEventResult> mEventStoptCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                mainActivity.mEventRepeat = false;

                RunEvent runEvent = response.body().getData();
//                updateEventState(runEvent);
//                saveEventInfo((int)runEvent.getId());
//                mainActivity.startEventStateCheck(runEvent.getId());

//                MainActivity activity = (MainActivity) getActivity();
//                if(activity != null)
//                    activity.startEventCount();

//                Toast.makeText(getContext(), "이벤트 를 종료 하였습니다.", Toast.LENGTH_SHORT).show();
            }

            showProgress(getActivity(), false);
        }
    };
    
    private SECallBack<RunEventResult> mEventStateCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                RunEvent runEvent = response.body().getData();
                mainActivity.updateEventInfo(runEvent);
                saveEventInfo((int)runEvent.getId());
            }
            else
            {
                // no event
//                mRadioVote.check(R.id.radio_vote_3m);
//                mRadioResult.check(R.id.radio_result_1m);
            }
            showProgress(getActivity(), false);
        }
    };

    private SECallBack<EventResult> mContinuityTypeCallBack = new SECallBack<EventResult>()
    {
        @Override
        public void onResponseResult(Response<EventResult> response)
        {

        }
    };

}