package com.rubbersoft.android.valveleakage.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.rubbersoft.android.valveleakage.R;
import com.rubbersoft.android.valveleakage.model.Data;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Data[] data;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
/*
        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"generating notification" + i, Toast.LENGTH_SHORT).show();
                generateNotification("",
                        PendingIntent.getActivity(MainActivity.this,
                                1,
                                new Intent(MainActivity.this, MainActivity.class)
                                , 0));
            }
        });
*/
    }

    public void populateListView(){


    }

    private void generateNotification(String s,PendingIntent pendingIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("TITLE")
                .setContentText("Content text")
                .setTicker("ticker")

                .setWhen(System.currentTimeMillis())
//                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.drawable.icon_small)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_small))
                .setContentIntent(pendingIntent)
                .setContentInfo("Information")
                .setAutoCancel(true)
                .addPerson("Faizan")
                .addPerson("Demon")
                ;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(i++,notificationBuilder.build());
    }
}
