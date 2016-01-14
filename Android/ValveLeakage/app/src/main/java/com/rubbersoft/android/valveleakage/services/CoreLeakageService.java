package com.rubbersoft.android.valveleakage.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Query;
import com.rubbersoft.android.valveleakage.R;
import com.rubbersoft.android.valveleakage.ValveLeakageApplication;
import com.rubbersoft.android.valveleakage.firebase.ChildEventListenerAdapter;
import com.rubbersoft.android.valveleakage.firebase.FirebaseHandler;
import com.rubbersoft.android.valveleakage.model.Data;
import com.rubbersoft.android.valveleakage.ui.MainActivity;
import com.rubbersoft.android.valveleakage.utils.ConfigConstants;
import com.rubbersoft.android.valveleakage.utils.DataBaseSource;
import com.rubbersoft.android.valveleakage.utils.SharedPreferenceManager;

public class CoreLeakageService extends Service {

    DataBaseSource dataBaseSource;
    FirebaseHandler firebaseHandler;
    SharedPreferenceManager sharedPreferenceManager;
    long startingTimeStamp;
    Query ref;
    PendingIntent pendingIntentNode1,
            pendingIntentNode2,
            pendingIntentNode3,
            pendingIntentNode4;
    ChildEventListener node1ChildEventListener,
            node2ChildEventListener,
            node3ChildEventListener,
            node4ChildEventListener;
    private IBinder mBinder = new LocalBinder();

    public CoreLeakageService() {
        dataBaseSource = ValveLeakageApplication.getDataBaseSource();
        firebaseHandler = ValveLeakageApplication.getFirebaseHandler();
        sharedPreferenceManager = ValveLeakageApplication.getSharedPreferenceManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("FBLOG", "in onStartCommand");

        initPendingIntents();
        createChildEventListeners();
        implementFirebaseListeners();

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("FBLOG", "in onDestroy");
        firebaseHandler.getNode1Ref().removeEventListener(node1ChildEventListener);
        firebaseHandler.getNode2Ref().removeEventListener(node2ChildEventListener);
        firebaseHandler.getNode3Ref().removeEventListener(node3ChildEventListener);
        firebaseHandler.getNode4Ref().removeEventListener(node4ChildEventListener);
    }

    private void implementFirebaseListeners() {
        Log.d("FBLOG", "in implementFirebaseListeners");
        firebaseHandler.getNode1Ref().orderByChild("timestamp").addChildEventListener(node1ChildEventListener);
        firebaseHandler.getNode2Ref().orderByChild("timestamp").addChildEventListener(node2ChildEventListener);
        firebaseHandler.getNode3Ref().orderByChild("timestamp").addChildEventListener(node3ChildEventListener);
        firebaseHandler.getNode4Ref().orderByChild("timestamp").addChildEventListener(node4ChildEventListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    private void createChildEventListeners() {
        node1ChildEventListener = new ChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Data data = dataSnapshot.getValue(Data.class);
                dataBaseSource.insertData(data, ConfigConstants.TABLE_NODE1);

                if (data.getLPGConcentration() >= 200) {
                    generateNotification(1, "Node 1", data.getTimestamp(), pendingIntentNode1);
                }
                Log.d("FBLOG", data.getTimestamp() + "");
            }
        };

        node2ChildEventListener = new ChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Data data = dataSnapshot.getValue(Data.class);
                dataBaseSource.insertData(data, ConfigConstants.TABLE_NODE2);

                if (data.getLPGConcentration() >= 200) {
                    generateNotification(2, "Node 2", data.getTimestamp(), pendingIntentNode2);
                }
                Log.d("FBLOG", data.getTimestamp() + "");
            }
        };

        node3ChildEventListener = new ChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Data data = dataSnapshot.getValue(Data.class);
                dataBaseSource.insertData(data, ConfigConstants.TABLE_NODE3);

                if (data.getLPGConcentration() >= 200) {
                    generateNotification(3, "Node 3", data.getTimestamp(), pendingIntentNode3);
                }
                Log.d("FBLOG", data.getTimestamp() + "");
            }
        };

        node4ChildEventListener = new ChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Data data = dataSnapshot.getValue(Data.class);
                dataBaseSource.insertData(data, ConfigConstants.TABLE_NODE4);

                if (data.getLPGConcentration() >= 200) {
                    generateNotification(4, "Node 4", data.getTimestamp(), pendingIntentNode4);
                }
                Log.d("FBLOG", data.getTimestamp() + "");
            }
        };
    }

    private void generateNotification(int i, String s, long when, PendingIntent pendingIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        notificationBuilder.setContentTitle(s + " Leakage")
                .setContentText("Take necessary measures")
                .setTicker(s + " Is Leaking")
                .setWhen(when)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.ic_stat)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
//                .setColor(0xF44336)
//                .setContentInfo("Information")
                .setAutoCancel(true)
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

    private void initPendingIntents() {
        pendingIntentNode1 = PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class).putExtra(ConfigConstants.INTENT_EXTRA_NODE, ConfigConstants.TABLE_NODE1), 0);
        pendingIntentNode2 = PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class).putExtra(ConfigConstants.INTENT_EXTRA_NODE, ConfigConstants.TABLE_NODE2), 0);
        pendingIntentNode3 = PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class).putExtra(ConfigConstants.INTENT_EXTRA_NODE, ConfigConstants.TABLE_NODE3), 0);
        pendingIntentNode4 = PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class).putExtra(ConfigConstants.INTENT_EXTRA_NODE, ConfigConstants.TABLE_NODE4), 0);
    }

    public static interface ServiceCallBacks {
        public void populateListView();
    }

    public class LocalBinder extends Binder {
        CoreLeakageService getService() {
            return CoreLeakageService.this;
        }
    }

}
