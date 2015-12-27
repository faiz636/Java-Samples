package com.rubbersoft.android.valveleakage;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.client.Firebase;
import com.rubbersoft.android.valveleakage.services.CoreLeakageService;
import com.rubbersoft.android.valveleakage.utils.AppLog;
import com.rubbersoft.android.valveleakage.utils.DataBaseSource;

/**
 * Created by Faiz on 25/12/2015.
 */
public class ValveLeakageApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("FBLOG", "in ValveLeakageApplication onCreate");

        Firebase.setAndroidContext(this);

        context = this;
        Intent intent = new Intent(getApplicationContext(), CoreLeakageService.class);
        startService(intent);
    }

    public static Context getContext() {
        return context;
    }
}
