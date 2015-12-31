package com.rubbersoft.android.valveleakage.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.rubbersoft.android.valveleakage.R;
import com.rubbersoft.android.valveleakage.ValveLeakageApplication;
import com.rubbersoft.android.valveleakage.model.ListAdapter;
import com.rubbersoft.android.valveleakage.utils.ConfigConstants;
import com.rubbersoft.android.valveleakage.utils.DataBaseSource;
import com.rubbersoft.android.valveleakage.utils.SharedPreferenceManager;

public class MainActivity extends AppCompatActivity {

    public static final String RECEIVER_ACTION = "com.rubbersoft.android.valveleakage.ui.MainActivity.RECEIVER_ACTION";
    ListView listView;
    ListAdapter listAdapter;
    DataBaseSource dataBaseSource;
    SharedPreferenceManager sharedPreferenceManager;
//    PendingIntent pendingIntent;
    int j;
    int i;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("FBLOG", "in onReceive");
            String s = (String) intent.getExtras().get(ConfigConstants.INTENT_EXTRA_NODE);
            if (s != null) {
                notifyDataChanged(s);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        dataBaseSource = ValveLeakageApplication.getDataBaseSource();
        sharedPreferenceManager = ValveLeakageApplication.getSharedPreferenceManager();

        listAdapter = new ListAdapter(MainActivity.this, dataBaseSource.dataNode1, listView);
        listView.setAdapter(listAdapter);


//        Intent intent = new Intent(getApplicationContext(), CoreLeakageService.class);
//       startService(intent);
//
//        pendingIntent = PendingIntent.getActivity(MainActivity.this, 1,
//                new Intent(MainActivity.this, MainActivity.class), 0);
//        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "generating notification" + i, Toast.LENGTH_SHORT).show();
//                generateNotification("", pendingIntent);
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(MainActivity.RECEIVER_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    public void notifyDataChanged(String s) {
        switch (s) {
            case ConfigConstants.TABLE_NODE1:
                listAdapter.notifyDataSetChanged();
                break;
            case ConfigConstants.TABLE_NODE2:
                break;
            case ConfigConstants.TABLE_NODE3:
                break;
            case ConfigConstants.TABLE_NODE4:
                break;
        }
    }

    private void generateNotification(String s, PendingIntent pendingIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        notificationBuilder.setContentTitle("TITLE")
                .setContentText("Content text " + j++)
                .setTicker("ticker")
                .setWhen(System.currentTimeMillis())
//                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.drawable.ic_stat)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
//                .setColor(0xF44336)
//                .setContentInfo("Information")
                .setAutoCancel(true)
                .addPerson("Faizan")
//                .addAction(R.drawable.icon_small, "First Action", pendingIntent)
        ;
/*
//        set Visibility of your notification
        notificationBuilder.setVisibility(Notification.VISIBILITY_PRIVATE)
//                if it is set to PRIVATE this notification will be shown on lock screen
//                then set a public version of your notification which will be shown on
//                unlocked device which will contain all information
                .setPublicVersion(publicNotification);
*/

/*
//        building expanded inbox style notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Now This BIG Content Title");
        inboxStyle.setSummaryText("This Is Summary Text");
//        adding line for each message
        for(int i=0;i<5;i++){
            inboxStyle.addLine("Inbox Message "+i);
        }
//        setting expanded notification style
        notificationBuilder.setStyle(inboxStyle);
*/

//        code for android device older then JELLY_BEAN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }


//        obtaining system
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

//        notifying on same notification id
//        if there is no previous notification of this id a new will generate
//        else the previous will be updated to newer notification
//        so there will be no duplicate notification
        notificationManager.notify(i, notificationBuilder.build());
    }

}
