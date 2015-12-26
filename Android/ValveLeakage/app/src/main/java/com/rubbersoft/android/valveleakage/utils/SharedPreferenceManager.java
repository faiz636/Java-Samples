package com.rubbersoft.android.valveleakage.utils;


import android.content.SharedPreferences;

import com.rubbersoft.android.valveleakage.ValveLeakageApplication;

/**
 * Created by Muhammad Faizan Khan on 26/12/15.
 */
public class SharedPreferenceManager {
    SharedPreferences.Editor editorpreferences;
    SharedPreferences preferences;
    static SharedPreferenceManager manager;

    public static SharedPreferenceManager getInstance() {
        if (manager == null)
            manager = new SharedPreferenceManager();
        return manager;
    }

    private SharedPreferenceManager(){
        preferences = ValveLeakageApplication.getContext().getSharedPreferences("Constants", 0);
        editorpreferences = preferences.edit();
        checkIfFirstTime();
    }

    private void checkIfFirstTime() {
        if(preferences.getString("isFirstTime",null) == null){
            editorpreferences.putString("isFirstTime","false");
            editorpreferences.putLong("timestamp",0);
        }
    }

    public void store (String key, String data) {
        editorpreferences.putString(key, data);
        editorpreferences.apply();
    }

    public void store (String key, long data) {
        editorpreferences.putLong(key, data);
        editorpreferences.apply();
    }

    public String retrieveString (String key){
        return preferences.getString(key,null);
    }

    public long retrieveLong(String key){
        return preferences.getLong(key, 0);
    }

/*
    public void clear() {
        editorpreferences.clear();
        editorpreferences.commit();
    }
*/



}
