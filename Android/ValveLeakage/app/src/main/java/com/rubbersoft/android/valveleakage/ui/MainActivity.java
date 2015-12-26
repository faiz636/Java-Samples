package com.rubbersoft.android.valveleakage.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.rubbersoft.android.valveleakage.R;
import com.rubbersoft.android.valveleakage.model.Data;
import com.rubbersoft.android.valveleakage.services.CoreLeakageService;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        Intent intent = new Intent(getApplicationContext(), CoreLeakageService.class);
        startService(intent);
    }

    public void populateListView(){

    }
}
