package com.rubbersoft.android.valveleakage.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.rubbersoft.android.valveleakage.ValveLeakageApplication;
import com.rubbersoft.android.valveleakage.firebase.FirebaseHandler;
import com.rubbersoft.android.valveleakage.model.Data;
import com.rubbersoft.android.valveleakage.utils.DataBaseSource;
import com.rubbersoft.android.valveleakage.utils.SQLiteHandler;
import com.rubbersoft.android.valveleakage.utils.SharedPreferenceManager;

import java.util.Calendar;
import java.util.Date;

public class CoreLeakageService extends Service {

    private IBinder mBinder = new LocalBinder();
    private ServiceCallBacks mserviceCallBacks;     //Use this object to call populateListView method in the activity class..

    DataBaseSource dataBaseSource;
    FirebaseHandler firebaseHandler;
    SharedPreferenceManager sharedPreferenceManager;
    long startingTimeStamp;
    public CoreLeakageService() {
        dataBaseSource = DataBaseSource.getInstance(ValveLeakageApplication.getContext());
        firebaseHandler = FirebaseHandler.getInstance();
        sharedPreferenceManager = SharedPreferenceManager.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("FBLOG",  "in onStartCommand");

        //Remove data older than a day..
        dataBaseSource.removeData(findTimeToKeepData());
        //Retrieving existing data
        populateDataNodeLists();
        //TODO: Call MainActivity's populateListView() method..
        implementFirebaseListeners();


        //TODO: Implement firebase listeners correctly
        //TODO: Add the remaining functionalities

        return START_STICKY;
    }

    private void populateDataNodeLists() {
        dataBaseSource.dataNode1 = dataBaseSource.getData(SQLiteHandler.getTableNode1());
        dataBaseSource.dataNode2 = dataBaseSource.getData(SQLiteHandler.getTableNode2());
        dataBaseSource.dataNode3 = dataBaseSource.getData(SQLiteHandler.getTableNode3());
        dataBaseSource.dataNode4 = dataBaseSource.getData(SQLiteHandler.getTableNode4());
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

        startingTimeStamp = sharedPreferenceManager.retrieveLong("timestamp");
        firebaseHandler.getNode1Ref().orderByChild("timestamp").startAt(startingTimeStamp).addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Data data = dataSnapshot.getValue(Data.class);
                        dataBaseSource.insertData(data,1);

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
                }
        );
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
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
