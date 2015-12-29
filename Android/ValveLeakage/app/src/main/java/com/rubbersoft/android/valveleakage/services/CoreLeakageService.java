package com.rubbersoft.android.valveleakage.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.rubbersoft.android.valveleakage.ValveLeakageApplication;
import com.rubbersoft.android.valveleakage.firebase.FirebaseHandler;
import com.rubbersoft.android.valveleakage.model.Data;
import com.rubbersoft.android.valveleakage.ui.MainActivity;
import com.rubbersoft.android.valveleakage.utils.DataBaseSource;
import com.rubbersoft.android.valveleakage.utils.SQLiteHandler;
import com.rubbersoft.android.valveleakage.utils.SharedPreferenceManager;

import java.util.Calendar;
import java.util.Date;

public class CoreLeakageService extends Service {

    private IBinder mBinder = new LocalBinder();
    private ServiceCallBacks mserviceCallBacks;     //Use this object to call populateListView method in the activity class..
    Intent receiverIntent = new Intent(MainActivity.RECEIVER_ACTION);

    DataBaseSource dataBaseSource;
    FirebaseHandler firebaseHandler;
    SharedPreferenceManager sharedPreferenceManager;
    long startingTimeStamp;
    Query ref;
    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Data data = dataSnapshot.getValue(Data.class);
            dataBaseSource.insertData(data, 1);
//            sharedPreferenceManager.store("timestamp",data.getTimestamp());

            dataBaseSource.populateDataNodeLists();

            sendBroadcast(receiverIntent);


            Log.d("FBLOG", data.getTimestamp() + "");

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    };

    public CoreLeakageService() {
        dataBaseSource = ValveLeakageApplication.getDataBaseSource();
        firebaseHandler = ValveLeakageApplication.getFirebaseHandler();
        sharedPreferenceManager = ValveLeakageApplication.getSharedPreferenceManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("FBLOG",  "in onStartCommand");

        //Remove data older than a day..
        dataBaseSource.removeData(findTimeToKeepData());
//        dataBaseSource.populateDataNodeLists();
//        sendBroadcast(receiverIntent);
        implementFirebaseListeners();


        //TODO: Implement firebase listeners correctly
        //TODO: Add the remaining functionalities

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("FBLOG", "in onDestroy");
        ref.removeEventListener(childEventListener);

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

//        startingTimeStamp = sharedPreferenceManager.retrieveLong("timestamp");
        ref = firebaseHandler.getNode1Ref().orderByChild("timestamp");
        ref.addChildEventListener(childEventListener);
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
}
