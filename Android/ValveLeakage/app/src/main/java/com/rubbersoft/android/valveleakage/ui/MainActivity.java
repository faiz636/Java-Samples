package com.rubbersoft.android.valveleakage.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.rubbersoft.android.valveleakage.R;
import com.rubbersoft.android.valveleakage.utils.AppLog;
import com.rubbersoft.android.valveleakage.utils.ConfigConstants;

public class MainActivity extends AppCompatActivity {

//    ListView listView;
//    ListAdapter listAdapter;
//    DataBaseSource dataBaseSource;
//    SharedPreferenceManager sharedPreferenceManager;
    private static final String TAG = "MainActivity";

    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        changeTabForIntentExtras(getIntent());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                cancleNotification(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        changeTabForIntentExtras(intent);
        AppLog.d("dsa", "onNewIntent called with -- "+intent.getStringExtra(ConfigConstants.INTENT_EXTRA_NODE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cancleNotification(viewPager.getCurrentItem());
    }

    private void cancleNotification(int position){
        switch (position){
            case 0:
                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(ConfigConstants.NODE1_NOTIFICATION_ID);
                return;
            case 1:
                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(ConfigConstants.NODE2_NOTIFICATION_ID);
                return;
            case 2:
                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(ConfigConstants.NODE3_NOTIFICATION_ID);
                return;
            case 3:
                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(ConfigConstants.NODE4_NOTIFICATION_ID);
                return;
        }
    }



    private void changeTabForIntentExtras(Intent intent){

        String nodeName = intent.getStringExtra(ConfigConstants.INTENT_EXTRA_NODE);
        if (nodeName!=null) {
            switch (nodeName) {
                case ConfigConstants.TABLE_NODE1:
                    viewPager.setCurrentItem(0);
                    break;
                case ConfigConstants.TABLE_NODE2:
                    viewPager.setCurrentItem(1);
                    break;
                case ConfigConstants.TABLE_NODE3:
                    viewPager.setCurrentItem(2);
                    break;
                case ConfigConstants.TABLE_NODE4:
                    viewPager.setCurrentItem(3);
                    break;
            }
        }
    }

}
