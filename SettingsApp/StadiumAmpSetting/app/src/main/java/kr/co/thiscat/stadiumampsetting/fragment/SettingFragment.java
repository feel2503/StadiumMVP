package kr.co.thiscat.stadiumampsetting.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private int mServerId;
    private int mEventId;
    private boolean mEventIsRunning = false;

    private ServerManager mServer;
    protected ProgressDialog mProgress = null;

    private ArrayList<StadiumServer> mServerList;

    private TextView mTextServrName;
    private TextView mTextEventStart;
    private TextView mTextEventStop;
    private RadioGroup mRadioVote;
    private RadioGroup mRadioResult;

    public SettingFragment() {
        // Required empty public constructor
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
        mServerId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_SERVER_ID, -1);
        mEventId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_EVENT_ID, -1);
        //mEventId = 1;
        if(mEventId > 0)
        {
            mServer.getEventState(mEventStateCallBack, mEventId);
            showProgress(getActivity(), true);
        }
        else if(mServerId > 0)
        {
            String name = mPreferenceUtil.getStringPreference(PreferenceUtil.KEY_SERVER_NAME);
            mTextServrName.setText("이벤트 서버 : " + name);

            mServer.getLastEvent(mEventStateCallBack, mServerId);
            showProgress(getActivity(), true);
        }
    }

    private void initView(View view)
    {
        view.findViewById(R.id.text_event_select).setOnClickListener(mOnClicklistener);
        mTextEventStart = view.findViewById(R.id.text_event_start);
        mTextEventStart.setOnClickListener(mOnClicklistener);
        mTextEventStop = view.findViewById(R.id.text_event_stop);
        mTextEventStop.setOnClickListener(mOnClicklistener);

        mTextServrName = view.findViewById(R.id.text_event_server);
        mRadioVote = view.findViewById(R.id.radio_vote);
        mRadioResult = view.findViewById(R.id.radio_result);
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

    private int getVoteTime()
    {
        if(mRadioVote.getCheckedRadioButtonId() == R.id.radio_vote_3m)
            return 3;
        else if(mRadioVote.getCheckedRadioButtonId() == R.id.radio_vote_5m)
            return 5;
        else
            return -1;
    }

    private int getResultTime()
    {
        if(mRadioResult.getCheckedRadioButtonId() == R.id.radio_result_1m)
            return 1;
        else if(mRadioResult.getCheckedRadioButtonId() == R.id.radio_result_3m)
            return 3;
        else
            return -1;
    }

    private void updateEventState(RunEvent runEvent)
    {
        if(runEvent.getVoteTime() == 3)
            mRadioVote.check(R.id.radio_vote_3m);
        else if(runEvent.getVoteTime() == 5)
            mRadioVote.check(R.id.radio_vote_5m);

        if(runEvent.getResultTime() == 1)
            mRadioResult.check(R.id.radio_result_1m);
        else if(runEvent.getVoteTime() == 3)
            mRadioResult.check(R.id.radio_result_3m);

        if(runEvent.getEventState().equalsIgnoreCase("START"))
        {
            setEventBtnState(true);
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                Date start = sdf.parse(runEvent.getStartDateTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(start);
                cal.add(Calendar.MINUTE, runEvent.getVoteTime());
                cal.add(Calendar.SECOND, 1);
                Date endDate = cal.getTime();

                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        mServer.getEventState(mEventStateCallBack, mEventId);
                    }
                };
                timer.schedule(timerTask, endDate);

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            setEventBtnState(false);
        }
    }

    private void saveEventInfo(int eventId)
    {
        mEventId = eventId;
        mPreferenceUtil.putIntPreference(PreferenceUtil.KEY_EVENT_ID, eventId);
    }

    private void showEventServerSelector()
    {
        ArrayList<String> items = new ArrayList<>();
        for(StadiumServer stadiumServer : mServerList)
        {
            items.add(stadiumServer.getName());
        }
        ArrayList<Integer> selectItem = new ArrayList<>(); // RadioButton 선택 한 값 담을 ArrayList ( TEXT )
        selectItem.clear();
        if(mServerId > 0) {
            selectItem.add(mServerId-1);
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
                        String name = mServerList.get(selIdx).getName();
                        int serverId = mServerList.get(selIdx).getId();
                        mPreferenceUtil.putIntPreference(PreferenceUtil.KEY_SERVER_ID, serverId);
                        mPreferenceUtil.putStringPrefrence(PreferenceUtil.KEY_SERVER_NAME, name);
                        mServerId = serverId;
                        mTextServrName.setText("이벤트 서버 : " + name);

                        mServer.getLastEvent(mLastEventCallBack, serverId);
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

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
                    
                if(mServerId < 0)
                {
                    new AlertDialog.Builder(getContext())
                            .setMessage("이벤트 서버가 설정되지 않았습니다.\n이벤트 서버를 선택 하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //mServer.eventStop(mEventStateCallBack, mEventId);
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
                MainActivity activity = (MainActivity)getActivity();

                StartEvent startEvent = new StartEvent();
                startEvent.setStadiumServerId(mServerId);
                startEvent.setVoteTime(getVoteTime());
                //startEvent.setVoteTime(1);

                startEvent.setResultTime(getResultTime());
                startEvent.setDefaultMusic(activity.mStrDefault);
                startEvent.setHomeMusic1(activity.mStrHome1);
                startEvent.setHomeMusic2(activity.mStrHome2);
                startEvent.setAwayMusic1(activity.mStrAway1);
                startEvent.setAwayMusic2(activity.mStrAway2);
                startEvent.setDefaultImage(activity.mStrDefaultImg);
                startEvent.setHomeImage(activity.mStrHomeImg);
                startEvent.setAwayImage(activity.mStrAwayImg);

                mServer.eventStart(mEventStartCallBack, startEvent);
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
                                mServer.eventStop(mEventStateCallBack, mEventId);
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

    private SECallBack<StadiumServerResult> mServerListCallBack = new SECallBack<StadiumServerResult>()
    {
        @Override
        public void onResponseResult(Response<StadiumServerResult> response)
        {
            if (response.isSuccessful())
            {
                mServerList = response.body().getData();
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
                mRadioVote.check(R.id.radio_vote_3m);
                mRadioResult.check(R.id.radio_result_1m);
                mEventId = -1;
                mPreferenceUtil.putIntPreference(PreferenceUtil.KEY_EVENT_ID, mEventId);
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
                RunEvent runEvent = response.body().getData();
                updateEventState(runEvent);
                saveEventInfo((int)runEvent.getId());

                MainActivity activity = (MainActivity) getActivity();
                if(activity != null)
                    activity.startEventCount();
            }

            showProgress(getActivity(), false);

            Toast.makeText(getContext(), "이벤트 를 시작 하였습니다.", Toast.LENGTH_SHORT).show();
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
                updateEventState(runEvent);
                saveEventInfo((int)runEvent.getId());

                MainActivity activity = (MainActivity) getActivity();
                activity.startEventCount();
            }
            else
            {
                // no event
                mRadioVote.check(R.id.radio_vote_3m);
                mRadioResult.check(R.id.radio_result_1m);
            }
            showProgress(getActivity(), false);
        }
    };


}