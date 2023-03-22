package kr.co.thiscat.stadiumampsetting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class DialogServerSelect extends Activity {

    private TextView mTextTitle;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog_server_select);

        setFinishOnTouchOutside(false);

        mTextTitle = (TextView)findViewById(R.id.text_title);
        mRadioGroup = (RadioGroup)findViewById(R.id.radio_contents);

        findViewById(R.id.btn_ok).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_cancel).setOnClickListener(mOnClickListener);
        init();
    }

    private void init() {

//        Intent intent = getIntent();
//        String title = intent.getStringExtra(CommonUtil.EXTRA_TITLE);
//        mTextTitle.setText(title);
//
//        int selected = intent.getIntExtra(CommonUtil.EXTRA_SELECTED_INT_VALUE, 0);
//        String[] contents = intent.getStringArrayExtra(CommonUtil.EXTRA_CONTENT_LIST);
//        for(int i = 0; i < contents.length; i++)
//        {
//            RadioButton radioButton  = new RadioButton(this);
//            radioButton.setText(contents[i] );
//            radioButton.setTextColor(0xff000000);
//            radioButton.setTextSize(16);
//            radioButton.setId(i);
//            if(i == selected)
//                radioButton.setChecked(true);
//            mRadioGroup.addView(radioButton);
//        }

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(v.getId() == R.id.btn_ok)
            {
                Intent data = new Intent();
                data.putExtra("EXTRA_INT_RESULT", mRadioGroup.getCheckedRadioButtonId());
                setResult(Activity.RESULT_OK, data);
                finish();
                overridePendingTransition(0, 0);
            }
            else if(v.getId() == R.id.btn_cancel )
            {
                setResult(Activity.RESULT_CANCELED, null);
                finish();
                overridePendingTransition(0, 0);
            }
        }
    };
}