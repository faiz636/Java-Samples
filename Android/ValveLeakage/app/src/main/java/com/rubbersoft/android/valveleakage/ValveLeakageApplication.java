package com.rubbersoft.android.valveleakage;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.client.Firebase;
import com.rubbersoft.android.valveleakage.firebase.FirebaseHandler;
import com.rubbersoft.android.valveleakage.model.Data;
import com.rubbersoft.android.valveleakage.services.CoreLeakageService;
import com.rubbersoft.android.valveleakage.utils.AppLog;
import com.rubbersoft.android.valveleakage.utils.DataBaseSource;
import com.rubbersoft.android.valveleakage.utils.SharedPreferenceManager;

/**
 * Created by Faiz on 25/12/2015.
 */
public class ValveLeakageApplication extends Application {

    private static Context context;
    private static DataBaseSource dataBaseSource;
    private static FirebaseHandler firebaseHandler;
    private static SharedPreferenceManager sharedPreferenceManager;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("FBLOG", "in ValveLeakageApplication onCreate");
        context = this;

        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

        initializeSingletons();

        Intent intent = new Intent(getApplicationContext(), CoreLeakageService.class);
        startService(intent);
    }

    private void initializeSingletons() {
        dataBaseSource = DataBaseSource.getInstance();
        firebaseHandler = FirebaseHandler.getInstance();
//        sharedPreferenceManager = SharedPreferenceManager.getInstance();
    }

    public static Context getContext() {
        return context;
    }

    public static DataBaseSource getDataBaseSource() {
        return dataBaseSource;
    }

    public static FirebaseHandler getFirebaseHandler() {
        return firebaseHandler;
    }

    public static SharedPreferenceManager getSharedPreferenceManager() {
        return sharedPreferenceManager;
    }
}
