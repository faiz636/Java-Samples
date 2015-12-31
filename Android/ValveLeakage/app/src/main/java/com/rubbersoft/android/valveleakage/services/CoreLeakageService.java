package com.rubbersoft.android.valveleakage.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Query;
import com.rubbersoft.android.valveleakage.ValveLeakageApplication;
import com.rubbersoft.android.valveleakage.firebase.ChildEventListenerAdapter;
import com.rubbersoft.android.valveleakage.firebase.FirebaseHandler;
import com.rubbersoft.android.valveleakage.model.Data;
import com.rubbersoft.android.valveleakage.ui.MainActivity;
import com.rubbersoft.android.valveleakage.utils.ConfigConstants;
import com.rubbersoft.android.valveleakage.utils.DataBaseSource;
import com.rubbersoft.android.valveleakage.utils.SharedPreferenceManager;

import java.util.Calendar;

public class CoreLeakageService extends Service {

    private IBinder mBinder = new LocalBinder();
    private ServiceCallBacks mserviceCallBacks;     //Use this object to call populateListView method in the activity class..
    Intent node1receiverIntent = new Intent(MainActivity.RECEIVER_ACTION)
            .putExtra(ConfigConstants.INTENT_EXTRA_NODE,ConfigConstants.TABLE_NODE1);
    Intent node2receiverIntent = new Intent(MainActivity.RECEIVER_ACTION)
            .putExtra(ConfigConstants.INTENT_EXTRA_NODE,ConfigConstants.TABLE_NODE2);
    Intent node3receiverIntent = new Intent(MainActivity.RECEIVER_ACTION)
            .putExtra(ConfigConstants.INTENT_EXTRA_NODE,ConfigConstants.TABLE_NODE3);
    Intent node4receiverIntent = new Intent(MainActivity.RECEIVER_ACTION)
            .putExtra(ConfigConstants.INTENT_EXTRA_NODE, ConfigConstants.TABLE_NODE4);

    DataBaseSource dataBaseSource;
    FirebaseHandler firebaseHandler;
    SharedPreferenceManager sharedPreferenceManager;
    long startingTimeStamp;
    Query ref;
    ChildEventListener node1ChildEventListener,
            node2ChildEventListener,
            node3ChildEventListener,
            node4ChildEventListener;

    public CoreLeakageService() {
        dataBaseSource = ValveLeakageApplication.getDataBaseSource();
        firebaseHandler = ValveLeakageApplication.getFirebaseHandler();
        sharedPreferenceManager = ValveLeakageApplication.getSharedPreferenceManager();
        createChildEventListeners();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("FBLOG",  "in onStartCommand");

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

    private long findTimeToKeepData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
        char[] temp = String.valueOf(calendar.getTimeInMillis()).toCharArray();
        temp[temp.length-1]='0';
        temp[temp.length-2]='0';
        temp[temp.length-3]='0';
        temp[temp.length-4]='0';
        return Long.valueOf(String.valueOf(temp));
    }

    private void implementFirebaseListeners() {
        Log.d("FBLOG",  "in implementFirebaseListeners");
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

    public class LocalBinder extends Binder{
        CoreLeakageService getService(){
            return CoreLeakageService.this;
        }
    }

    public static interface ServiceCallBacks {
        public void populateListView();
    }

    public void setCallBacksForService(ServiceCallBacks serviceCallBacks){
        this.mserviceCallBacks = serviceCallBacks;
    }

    private void createChildEventListeners(){
        node1ChildEventListener  = new ChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Data data = dataSnapshot.getValue(Data.class);
                dataBaseSource.insertData(data, ConfigConstants.TABLE_NODE1);
                sendBroadcast(node1receiverIntent);

//            TODO: Call your notification genenartor method here

                Log.d("FBLOG", data.getTimestamp() + "");
            }
        };

        node2ChildEventListener  = new ChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Data data = dataSnapshot.getValue(Data.class);
                dataBaseSource.insertData(data, ConfigConstants.TABLE_NODE2);
                sendBroadcast(node2receiverIntent);

//            TODO: Call your notification genenartor method here

                Log.d("FBLOG", data.getTimestamp() + "");
            }
        };

        node3ChildEventListener  = new ChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Data data = dataSnapshot.getValue(Data.class);
                dataBaseSource.insertData(data, ConfigConstants.TABLE_NODE3);
                sendBroadcast(node3receiverIntent);

//            TODO: Call your notification genenartor method here

                Log.d("FBLOG", data.getTimestamp() + "");
            }
        };

        node4ChildEventListener  = new ChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Data data = dataSnapshot.getValue(Data.class);
                dataBaseSource.insertData(data, ConfigConstants.TABLE_NODE4);
                sendBroadcast(node4receiverIntent);

//            TODO: Call your notification genenartor method here

                Log.d("FBLOG", data.getTimestamp() + "");
            }
        };
    }
}
