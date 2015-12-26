package com.rubbersoft.android.valveleakage.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.rubbersoft.android.valveleakage.R;
import com.rubbersoft.android.valveleakage.model.Data;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Data[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
    }

    public void populateListView(){

    }
}
