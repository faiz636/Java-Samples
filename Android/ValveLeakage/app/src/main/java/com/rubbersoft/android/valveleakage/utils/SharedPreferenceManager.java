package com.rubbersoft.android.valveleakage.utils;


import android.content.SharedPreferences;

import com.rubbersoft.android.valveleakage.ValveLeakageApplication;

/**
 * Created by Muhammad Faizan Khan on 26/12/15.
 */
public class SharedPreferenceManager {
    static SharedPreferences.Editor preferences = ValveLeakageApplication.getContext().getSharedPreferences("Constants", 0).edit();
    static SharedPreferenceManager manager;

    public static SharedPreferenceManager getInstance() {
        if (manager == null)
            manager = new SharedPreferenceManager();
        return manager;
    }

/*
    public void save(String mUserId) {
        userId = mUserId;
        preferences.putString("USER", userId);
        preferences.commit();
    }
*/

    public void clear() {
        preferences.clear();
        preferences.commit();
    }

}
