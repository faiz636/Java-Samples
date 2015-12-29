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

    SQLiteHandler sqLiteHandler;
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
        sqLiteHandler = new SQLiteHandler(ValveLeakageApplication.getContext());
        dataNode1 = new ArrayList<>();
        dataNode2 = new ArrayList<>();
        dataNode3 = new ArrayList<>();
        dataNode4 = new ArrayList<>();
    }

    public void insertData(Data data, String tableName){

        //If timestamp already exists dont insert..
        if(checkIsDataAlreadyInDBorNot(tableName, "timestamp", String.valueOf(data.getTimestamp()))) {
            Log.d("FBLOG", "in insertData, Data Exists: " + String.valueOf(data.getTimestamp()));
            return;
        }

        SQLiteDatabase sqLiteDatabase = sqLiteHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("timestamp", String.valueOf(data.getTimestamp()));
        contentValues.put("temperature", String.valueOf(data.getTemperature()));
        contentValues.put("lpgconcentration", String.valueOf(data.getLPGConcentration()));


        switch (tableName){
            case "node1":
                sqLiteDatabase.insert(SQLiteHandler.getTableNode1(), null, contentValues);
                break;
            case "node2":
                sqLiteDatabase.insert(SQLiteHandler.getTableNode2(), null, contentValues);
                break;
            case "node3":
                sqLiteDatabase.insert(SQLiteHandler.getTableNode3(), null, contentValues);
                break;
            case "node4":
                sqLiteDatabase.insert(SQLiteHandler.getTableNode4(), null, contentValues);
                break;
            default:
                AppLog.e("MuzammilQadri","insertData() called with wrong Table/Node");
        }
        sqLiteDatabase.close();

    }

    public void removeData(long milliSeconds){
        SQLiteDatabase sqLiteDatabase = sqLiteHandler.getWritableDatabase();

        //Deleting from all tables one by one..
        String query = "delete from "+ SQLiteHandler.getTableNode1() +" where timestamp < " + milliSeconds;
        sqLiteDatabase.execSQL(query);
        query = "delete from "+ SQLiteHandler.getTableNode2() +" where timestamp < " + milliSeconds;
        sqLiteDatabase.execSQL(query);
        query = "delete from "+ SQLiteHandler.getTableNode3() +" where timestamp < " + milliSeconds;
        sqLiteDatabase.execSQL(query);
        query = "delete from "+ SQLiteHandler.getTableNode4() +" where timestamp < " + milliSeconds;
        sqLiteDatabase.execSQL(query);

        sqLiteDatabase.close();
    }


    public ArrayList<Data> getData(String tableName){
        ArrayList<Data> dataList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = sqLiteHandler.getReadableDatabase();
        String query = "SELECT * FROM "+ tableName;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Data data = cursorToData(cursor);
            dataList.add(data);
            cursor.moveToNext();
        }
        sqLiteDatabase.close();
        return dataList;
    }

    private Data cursorToData(Cursor cursor) {
        Data data =  new Data();
        data.setTimestamp(Long.valueOf(cursor.getString(1)));
        data.setTemperature(Float.valueOf(cursor.getString(2)));
        data.setLPGConcentration(Float.valueOf(cursor.getString(3)));

        Log.d("Muzammil","Timestamp: "+  data.getTimestamp());
        Log.d("Muzammil","Temperature: "+  data.getTemperature());
        Log.d("Muzammil","LPG: "+  data.getLPGConcentration());

        return data;
    }

    public void populateDataNodeLists() {
        dataNode1 = getData(SQLiteHandler.getTableNode1());
        dataNode2 = getData(SQLiteHandler.getTableNode2());
        dataNode3 = getData(SQLiteHandler.getTableNode3());
        dataNode4 = getData(SQLiteHandler.getTableNode4());
    }


    public boolean checkIsDataAlreadyInDBorNot(String TableName, String dbfield, String fieldValue) {
        SQLiteDatabase sqLiteDatabase = sqLiteHandler.getReadableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
