package com.rubbersoft.android.valveleakage.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rubbersoft.android.valveleakage.ValveLeakageApplication;
import com.rubbersoft.android.valveleakage.model.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Muzammil on 26-Dec-15.
 */
public class DataBaseSource {

    public List<Data> dataNode1;
    public List<Data> dataNode2;
    public List<Data> dataNode3;
    public List<Data> dataNode4;

    public static DataBaseSource getInstance(){
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final DataBaseSource INSTANCE = new DataBaseSource ();
    }

    private DataBaseSource(){
        dataNode1 = new ArrayList<>();
        dataNode2 = new ArrayList<>();
        dataNode3 = new ArrayList<>();
        dataNode4 = new ArrayList<>();
    }

    public void insertData(Data data, String nodeName){
        switch (nodeName){
            case ConfigConstants.TABLE_NODE1:
                dataNode1.add(0,data);
                break;
            case ConfigConstants.TABLE_NODE2:
                dataNode2.add(0,data);
                break;
            case ConfigConstants.TABLE_NODE3:
                dataNode3.add(0,data);
                break;
            case ConfigConstants.TABLE_NODE4:
                dataNode4.add(0,data);
                break;
            default:
                AppLog.e("MuzammilQadri","insertData() called with wrong Node");
        }
    }

}
