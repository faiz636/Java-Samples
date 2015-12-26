package com.rubbersoft.android.valveleakage.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rubbersoft.android.valveleakage.model.Data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Muhammad Muzammil on 26-Dec-15.
 */
public class DataBaseSource {

    private static DataBaseSource dataBaseSource;
    SQLiteHandler sqLiteHandler;
    public List<Data> dataNode1;
    public List<Data> dataNode2;
    public List<Data> dataNode3;
    public List<Data> dataNode4;

    public static DataBaseSource getInstance(Context context){
        return dataBaseSource != null ? dataBaseSource : new DataBaseSource(context);
    }

    private DataBaseSource(Context context){
        sqLiteHandler = new SQLiteHandler(context);
        dataNode1 = new ArrayList<>();
        dataNode2 = new ArrayList<>();
        dataNode3 = new ArrayList<>();
        dataNode4 = new ArrayList<>();
    }

    public void insertData(Data data, int tableNumber){
        SQLiteDatabase sqLiteDatabase = sqLiteHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Date date = new Date(data.getTimestamp());

//        For conversion of timestamp to date and time
//        AppLog.d("MuzammilQadri","Current Date: " + new SimpleDateFormat("yyyy.MM.dd").format(date));
//        AppLog.d("MuzammilQadri", "Current Time: " + new SimpleDateFormat("hh:mm:ss a").format(date));

        contentValues.put("timestamp", data.getTimestamp());
        contentValues.put("temperature", data.getTemperature());
        contentValues.put("lpgconcentration", data.getLPGConcentration());

        switch (tableNumber){
            case 1:
                sqLiteDatabase.insert(SQLiteHandler.getTableNode1(), null, contentValues);
                break;
            case 2:
                sqLiteDatabase.insert(SQLiteHandler.getTableNode2(), null, contentValues);
                break;
            case 3:
                sqLiteDatabase.insert(SQLiteHandler.getTableNode3(), null, contentValues);
                break;
            case 4:
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
        data.setTimestamp(cursor.getLong(0));
        data.setTemperature(cursor.getFloat(1));
        data.setLPGConcentration(cursor.getFloat(2));
        return data;
    }

}
