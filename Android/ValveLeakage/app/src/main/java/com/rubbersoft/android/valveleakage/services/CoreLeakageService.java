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

/*
* service that runs in background receive data from firebase
* generate notification if data is of leakage
* add data to list which can be used to show in list
* */
public class CoreLeakageService extends Service {

    private static final String TAG = "CoreLeakageService";

    DataBaseSource dataBaseSource;//data handler
    FirebaseHandler firebaseHandler;//firebase handler used for firebase
    SharedPreferenceManager sharedPreferenceManager;//used to read and write data to Shared Preferences

    /*
    * pending intent used for notification
    * which knows what to open on clicking notification
    * */
    PendingIntent pendingIntentNode1;
    PendingIntent pendingIntentNode2;
    PendingIntent pendingIntentNode3;
    PendingIntent pendingIntentNode4;

    /*
    * child event listeners used to read data from firebase
    * this type of listener is used to read arrays
    * each one is used to read data for each node
    * */
    ChildEventListener node1ChildEventListener;
    ChildEventListener node2ChildEventListener;
    ChildEventListener node3ChildEventListener;
    ChildEventListener node4ChildEventListener;
    private IBinder mBinder = new LocalBinder();

    public CoreLeakageService() {
        dataBaseSource = ValveLeakageApplication.getDataBaseSource();
        firebaseHandler = ValveLeakageApplication.getFirebaseHandler();
        sharedPreferenceManager = ValveLeakageApplication.getSharedPreferenceManager();
    }

    /*
    * this is the method called on starting service
    * here we write what to do
    * */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AppLog.i(TAG, "in onStartCommand");

        initPendingIntents();//initializing Pending Intents for notification
        initChildEventListeners();//initializing Child Event Listeners that recieve data
        implementFirebaseListeners();//implement Firebase Listeners to start receiving data

        return START_STICKY;//this tells android how to handle this service
    }

    /*
    * this method is called when the service is being destroyed by android system
    * here we remove the listeners used to receive data
    * */
    @Override
    public void onDestroy() {
        super.onDestroy();
        AppLog.i(TAG, "in onDestroy");
        firebaseHandler.getNode1Ref().removeEventListener(node1ChildEventListener);
        firebaseHandler.getNode2Ref().removeEventListener(node2ChildEventListener);
        firebaseHandler.getNode3Ref().removeEventListener(node3ChildEventListener);
        firebaseHandler.getNode4Ref().removeEventListener(node4ChildEventListener);
    }

    /*
    * this method is used to set listeners to receive data form fireabse
    * */
    private void implementFirebaseListeners() {
        AppLog.d(TAG, "in implementFirebaseListeners");
        firebaseHandler.getNode1Ref()
                .orderByChild("timestamp")//generate a query to tell in what arrangement to send data
                .startAt(System.currentTimeMillis() - 1000 * 60 * 60 * 24)//setting starting value to a day before
                .addChildEventListener(node1ChildEventListener);//setting listener
        firebaseHandler.getNode2Ref()
                .orderByChild("timestamp")
                .startAt(System.currentTimeMillis() - 1000 * 60 * 60 * 24)
                .addChildEventListener(node2ChildEventListener);
        firebaseHandler.getNode3Ref()
                .orderByChild("timestamp")
                .startAt(System.currentTimeMillis() - 1000 * 60 * 60 * 24)
                .addChildEventListener(node3ChildEventListener);
        firebaseHandler.getNode4Ref()
                .orderByChild("timestamp")
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

    /*
    * this method initialize listeners for each node
    * */
    private void initChildEventListeners() {
        node1ChildEventListener = createChildEventListeners(ConfigConstants.TABLE_NODE1, "NODE 1", pendingIntentNode1, ConfigConstants.NODE1_NOTIFICATION_ID);
        node2ChildEventListener = createChildEventListeners(ConfigConstants.TABLE_NODE2, "NODE 2", pendingIntentNode2, ConfigConstants.NODE2_NOTIFICATION_ID);
        node3ChildEventListener = createChildEventListeners(ConfigConstants.TABLE_NODE3, "NODE 3", pendingIntentNode3, ConfigConstants.NODE3_NOTIFICATION_ID);
        node4ChildEventListener = createChildEventListeners(ConfigConstants.TABLE_NODE4, "NODE 4", pendingIntentNode4, ConfigConstants.NODE4_NOTIFICATION_ID);
    }

    /*
    * create Child Event Listeners listener with specified parameters
    * */
    private ChildEventListener createChildEventListeners(final String nodeNameConfigConstant, final String nodeNotificationString, final PendingIntent notificationPendingIntent, final int notificationID) {
        return new ChildEventListenerAdapter() {

            /*
            * this method is called if new data is added
            * */
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AppLog.d(TAG + "-CA-" + nodeNameConfigConstant, dataSnapshot.toString());
                Data data = dataSnapshot.getValue(Data.class);//retrieve data received
                data.setKey(dataSnapshot.getKey());//get the key of the object and save to data object
                dataBaseSource.insertData(data, nodeNameConfigConstant);//add data to specified list

                //check value for leakage
                //check if notification to be generated
                if (data.getLPGConcentration() >= 200 && sharedPreferenceManager.retrieveLong(nodeNameConfigConstant) < data.getTimestamp()) {
                    //generate notification for the node
                    generateNotification(notificationID, nodeNotificationString, data.getTimestamp(), notificationPendingIntent);
                    //update sharedPreferenceManager this new notification
                    sharedPreferenceManager.store(nodeNameConfigConstant, data.getTimestamp());
                }
            }

            /*
            * this method is called when already added data is removed
            * */
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                dataBaseSource.removeData(dataSnapshot.getKey(), nodeNameConfigConstant);
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
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        ;

//        code for android devices after JELLY_BEAN
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

//    initialize pending intents for notification
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
