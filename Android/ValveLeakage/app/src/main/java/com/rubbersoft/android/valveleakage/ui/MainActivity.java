package com.rubbersoft.android.valveleakage.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.rubbersoft.android.valveleakage.R;

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
    }

}
