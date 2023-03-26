package kr.co.thiscat.stadiumampsetting.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.thiscat.stadiumampsetting.PreferenceUtil;
import kr.co.thiscat.stadiumampsetting.R;
import kr.co.thiscat.stadiumampsetting.server.SECallBack;
import kr.co.thiscat.stadiumampsetting.server.ServerManager;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEvent;
import kr.co.thiscat.stadiumampsetting.server.entity.RunEventResult;
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

    private TextView mTextHome;
    private TextView mTextAway;
    private TextView mTextTime;

    private int second;

    private ImageView mImgEvent;

    public EventFragment() {
        // Required empty public constructor
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

        mPreferenceUtil = new PreferenceUtil(getContext());
        mProgress = new ProgressDialog(getContext());
        mServer = ServerManager.getInstance(getContext());
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int eventId = mPreferenceUtil.getIntPreference(PreferenceUtil.KEY_EVENT_ID, -1);
        //mEventId = 1;
        if(eventId > 0)
        {
            mServer.getEventState(mEventStateCallBack, eventId);
            showProgress(getActivity(), true);
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
        if(getActivity() != null)
        {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int min = time / 60;
                    int sec = time % 60;
                    String result = "" + min + "분" + sec + "초";
                    mTextTime.setText(result);
                }
            });
        }
    }

    private void updateTimeStr(RunEvent runEvent)
    {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date startTime = sdf.parse(runEvent.getStartDateTime());

            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            cal.add(Calendar.MINUTE, runEvent.getVoteTime());
            Date endDate = cal.getTime();

            Date nowDate = Calendar.getInstance().getTime();

            long diffTime = endDate.getTime() - nowDate.getTime();
            if(diffTime <= 0)
            {
                mTextTime.setText("이벤트 종료");
            }
            else
            {
                second = (int)(diffTime / 1000);
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if(second != 0) {
                            //1초씩 감소
                            second--;

                            // 0분 이상이면
                        }
                        updateTimerTextView(second);
                        //mTextTime.setText(getTimeStr(second));
                    }
                };
                timer.schedule(timerTask, 0, 1000);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateScore(RunEvent runEvent)
    {
        int home = runEvent.getHomeCount();
        int away = runEvent.getAwayCount();

//        int home = 300;
//        int away = 200;
        if(home == 0)
            home = 1;
        if(away == 0)
            away = 1;
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, home);
        mTextHome.setLayoutParams(params);

        ViewGroup.LayoutParams params1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, away);
        mTextAway.setLayoutParams(params1);
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

    private SECallBack<RunEventResult> mEventStateCallBack = new SECallBack<RunEventResult>()
    {
        @Override
        public void onResponseResult(Response<RunEventResult> response)
        {
            if (response.isSuccessful())
            {
                RunEvent runEvent = response.body().getData();
                updateTimeStr(runEvent);
                updateScore(runEvent);
            }
            else
            {
                // no event
            }
            showProgress(getActivity(), false);
        }
    };
}