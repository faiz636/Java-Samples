package com.rubbersoft.android.valveleakage.utils;


import android.content.SharedPreferences;

import com.rubbersoft.android.valveleakage.ValveLeakageApplication;

/**
 * Created by Muhammad Faizan Khan on 26/12/15.
 */
public class SharedPreferenceManager {
    public SharedPreferences.Editor editorpreferences;
    public SharedPreferences preferences;
    private static SharedPreferenceManager manager;


    public static synchronized SharedPreferenceManager getInstance(){
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final SharedPreferenceManager INSTANCE = new SharedPreferenceManager ();
    }

    private SharedPreferenceManager(){
        preferences = ValveLeakageApplication.getContext().getSharedPreferences("Constants", 0);
        editorpreferences = preferences.edit();
        checkIfFirstTime();
    }

    private void checkIfFirstTime() {
        if(preferences.getBoolean("isFirstTime",true)){
            editorpreferences.putLong(ConfigConstants.TABLE_NODE1,0);
            editorpreferences.putLong(ConfigConstants.TABLE_NODE2,0);
            editorpreferences.putLong(ConfigConstants.TABLE_NODE3,0);
            editorpreferences.putLong(ConfigConstants.TABLE_NODE4,0);
            editorpreferences.putBoolean("isFirstTime",false);
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
