package com.rubbersoft.android.valveleakage;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;

import com.firebase.client.Firebase;
import com.rubbersoft.android.valveleakage.firebase.FirebaseHandler;
import com.rubbersoft.android.valveleakage.services.CoreLeakageService;
import com.rubbersoft.android.valveleakage.utils.AppLog;
import com.rubbersoft.android.valveleakage.utils.DataBaseSource;
import com.rubbersoft.android.valveleakage.utils.SharedPreferenceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Faiz on 25/12/2015.
 */
public class ValveLeakageApplication extends Application {

    private static final String TAG = "test-ValveLeakageApplication";

    private static Context context;
    private static DataBaseSource dataBaseSource;
    private static FirebaseHandler firebaseHandler;
    private static SharedPreferenceManager sharedPreferenceManager;
    private static Map<String, Boolean> states;

    static {
        states = new HashMap<>();
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

    @Override
    public void onCreate() {
        super.onCreate();
        AppLog.i(TAG, "in ValveLeakageApplication onCreate");
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
        sharedPreferenceManager = SharedPreferenceManager.getInstance();
    }

}