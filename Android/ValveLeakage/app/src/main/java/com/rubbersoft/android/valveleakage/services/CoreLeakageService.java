package com.rubbersoft.android.valveleakage.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.rubbersoft.android.valveleakage.firebase.FirebaseHandler;
import com.rubbersoft.android.valveleakage.utils.DataBaseSource;
import com.rubbersoft.android.valveleakage.utils.SQLiteHandler;
import com.rubbersoft.android.valveleakage.utils.SharedPreferenceManager;

import java.util.Calendar;
import java.util.Date;

public class CoreLeakageService extends Service {

    DataBaseSource dataBaseSource;
    FirebaseHandler firebaseHandler;
    SharedPreferenceManager sharedPreferenceManager;
    long startingTimeStamp;
    public CoreLeakageService() {
        dataBaseSource = DataBaseSource.getInstance(getApplicationContext());
        firebaseHandler = FirebaseHandler.getInstance();
        sharedPreferenceManager = SharedPreferenceManager.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Remove data older than a day..
        dataBaseSource.removeData(findTimeToKeepData());
        //Retrieving existing data
        populateDataNodeLists();
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
        startingTimeStamp = sharedPreferenceManager.retrieveLong("timestamp");
        firebaseHandler.getNode1Ref().orderByChild("timestamp").startAt(startingTimeStamp).addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
