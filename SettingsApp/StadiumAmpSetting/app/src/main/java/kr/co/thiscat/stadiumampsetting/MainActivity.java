package kr.co.thiscat.stadiumampsetting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import kr.co.thiscat.stadiumampsetting.fragment.EventFragment;
import kr.co.thiscat.stadiumampsetting.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {
    LinearLayout home_ly;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(); //객체 정의
        SettingListener(); //리스너 등록

        //맨 처음 시작할 탭 설정
        bottomNavigationView.setSelectedItemId(R.id.tab_setting);
    }

    private void init() {
        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


    }

    private void SettingListener() {
        //선택 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new TabSelectedListener());
    }

    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.tab_setting: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new SettingFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_event: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new EventFragment())
                            .commit();
                    return true;
                }

            }

            return false;
        }
    }




}