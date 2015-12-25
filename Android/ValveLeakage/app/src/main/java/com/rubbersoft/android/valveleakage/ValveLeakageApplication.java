package com.rubbersoft.android.valveleakage;

import android.app.Application;
import android.content.Context;

import com.firebase.client.Firebase;
import com.rubbersoft.android.valveleakage.utils.AppLog;

/**
 * Created by Faiz on 25/12/2015.
 */
public class ValveLeakageApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
