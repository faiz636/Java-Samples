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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.rubbersoft.android.valveleakage.R;
import com.rubbersoft.android.valveleakage.ValveLeakageApplication;
import com.rubbersoft.android.valveleakage.firebase.ChildEventListenerAdapter;
import com.rubbersoft.android.valveleakage.firebase.FirebaseHandler;
import com.rubbersoft.android.valveleakage.model.Data;
import com.rubbersoft.android.valveleakage.ui.MainActivity;
import com.rubbersoft.android.valveleakage.utils.AppLog;
import com.rubbersoft.android.valveleakage.utils.ConfigConstants;
import com.rubbersoft.android.valveleakage.utils.DataBaseSource;
import com.rubbersoft.android.valveleakage.utils.SharedPreferenceManager;

public class CoreLeakageService extends Service {

    private static final String TAG = "CoreLeakageService";

    Intent node1receiverIntent = new Intent(ConfigConstants.RECEIVER_ACTION_NODE1);
    Intent node2receiverIntent = new Intent(ConfigConstants.RECEIVER_ACTION_NODE2);
    Intent node3receiverIntent = new Intent(ConfigConstants.RECEIVER_ACTION_NODE3);
    Intent node4receiverIntent = new Intent(ConfigConstants.RECEIVER_ACTION_NODE4);
    DataBaseSource dataBaseSource;
    FirebaseHandler firebaseHandler;
    SharedPreferenceManager sharedPreferenceManager;
    long startingTimeStamp;
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

        AppLog.i(TAG, "in onStartCommand");

        initPendingIntents();
        initChildEventListeners();
        implementFirebaseListeners();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppLog.i(TAG, "in onDestroy");
        firebaseHandler.getNode1Ref().removeEventListener(node1ChildEventListener);
        firebaseHandler.getNode2Ref().removeEventListener(node2ChildEventListener);
        firebaseHandler.getNode3Ref().removeEventListener(node3ChildEventListener);
        firebaseHandler.getNode4Ref().removeEventListener(node4ChildEventListener);
    }

    private void implementFirebaseListeners() {
        AppLog.d(TAG, "in implementFirebaseListeners");
        firebaseHandler.getNode1Ref().orderByChild("timestamp")
                .startAt(System.currentTimeMillis() - 1000 * 60 * 60 * 24)
                .addChildEventListener(node1ChildEventListener);
        firebaseHandler.getNode2Ref().orderByChild("timestamp")
                .startAt(System.currentTimeMillis() - 1000 * 60 * 60 * 24)
                .addChildEventListener(node2ChildEventListener);
        firebaseHandler.getNode3Ref().orderByChild("timestamp")
                .startAt(System.currentTimeMillis() - 1000 * 60 * 60 * 24)
                .addChildEventListener(node3ChildEventListener);
        firebaseHandler.getNode4Ref().orderByChild("timestamp")
                .startAt(System.currentTimeMillis() - 1000 * 60 * 60 * 24)
                .addChildEventListener(node4ChildEventListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    private void initChildEventListeners() {
        node1ChildEventListener = createChildEventListeners(ConfigConstants.TABLE_NODE1, "NODE 1", node1receiverIntent, pendingIntentNode1, ConfigConstants.NODE1_NOTIFICATION_ID);
        node2ChildEventListener = createChildEventListeners(ConfigConstants.TABLE_NODE2, "NODE 2", node2receiverIntent, pendingIntentNode2, ConfigConstants.NODE2_NOTIFICATION_ID);
        node3ChildEventListener = createChildEventListeners(ConfigConstants.TABLE_NODE3, "NODE 3", node3receiverIntent, pendingIntentNode3, ConfigConstants.NODE3_NOTIFICATION_ID);
        node4ChildEventListener = createChildEventListeners(ConfigConstants.TABLE_NODE4, "NODE 4", node4receiverIntent, pendingIntentNode4, ConfigConstants.NODE4_NOTIFICATION_ID);
    }

    private ChildEventListener createChildEventListeners(final String nodeNameConfigConstant, final String nodeNotificationString, final Intent nodeBroadcastIntent, final PendingIntent notificationPendingIntent, final int notificationID) {
        return new ChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AppLog.d(TAG + "-CA-" + nodeNameConfigConstant, dataSnapshot.toString());
                Data data = dataSnapshot.getValue(Data.class);
                data.setKey(dataSnapshot.getKey());
                dataBaseSource.insertData(data, nodeNameConfigConstant);
                sendBroadcast(nodeBroadcastIntent);

                if (sharedPreferenceManager.retrieveLong(nodeNameConfigConstant) < data.getTimestamp() && data.getLPGConcentration() >= 200) {
                    if (!ValveLeakageApplication.isActivityResumed(ConfigConstants.ui.MainActivity)) {
                        generateNotification(notificationID, nodeNotificationString, data.getTimestamp(), notificationPendingIntent);
                    }
                    sharedPreferenceManager.store(nodeNameConfigConstant, data.getTimestamp());
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                dataBaseSource.removeData(dataSnapshot.getKey(), nodeNameConfigConstant);
                sendBroadcast(nodeBroadcastIntent);
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
        pendingIntentNode1 = PendingIntent.getActivity(this,
                ConfigConstants.NODE1_NOTIFICATION_ID,//for requesting pending intent using notification id as it unique for each node
                new Intent(this, MainActivity.class)
                        .putExtra(ConfigConstants.INTENT_EXTRA_NODE, ConfigConstants.TABLE_NODE1)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                , PendingIntent.FLAG_UPDATE_CURRENT);

        pendingIntentNode2 = PendingIntent.getActivity(this,
                ConfigConstants.NODE2_NOTIFICATION_ID,//for requesting pending intent using notification id as it unique for each node
                new Intent(this, MainActivity.class)
                        .putExtra(ConfigConstants.INTENT_EXTRA_NODE, ConfigConstants.TABLE_NODE2)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                , PendingIntent.FLAG_UPDATE_CURRENT);

        pendingIntentNode3 = PendingIntent.getActivity(this,
                ConfigConstants.NODE3_NOTIFICATION_ID,//for requesting pending intent using notification id as it unique for each node
                new Intent(this, MainActivity.class)
                        .putExtra(ConfigConstants.INTENT_EXTRA_NODE, ConfigConstants.TABLE_NODE3)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                , PendingIntent.FLAG_UPDATE_CURRENT);

        pendingIntentNode4 = PendingIntent.getActivity(this,
                ConfigConstants.NODE4_NOTIFICATION_ID,//for requesting pending intent using notification id as it unique for each node
                new Intent(this, MainActivity.class)
                        .putExtra(ConfigConstants.INTENT_EXTRA_NODE, ConfigConstants.TABLE_NODE4)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                , PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public class LocalBinder extends Binder {
        CoreLeakageService getService() {
            return CoreLeakageService.this;
        }
    }

}
