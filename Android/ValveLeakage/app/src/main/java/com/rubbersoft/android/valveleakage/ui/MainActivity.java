package com.rubbersoft.android.valveleakage.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.rubbersoft.android.valveleakage.R;
import com.rubbersoft.android.valveleakage.utils.AppLog;
import com.rubbersoft.android.valveleakage.utils.ConfigConstants;

public class MainActivity extends AppCompatActivity {

//    ListView listView;
//    ListAdapter listAdapter;
//    DataBaseSource dataBaseSource;
//    SharedPreferenceManager sharedPreferenceManager;

    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TabLayout tabLayout;
    int j;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        changeTabForIntentExtras();
    }

    private void changeTabForIntentExtras(){

        String nodeName = getIntent().getStringExtra(ConfigConstants.INTENT_EXTRA_NODE);
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
                default:
                    AppLog.e("MuzammilQadri", "insertData() called with wrong Node");
            }
        }
    }

}
