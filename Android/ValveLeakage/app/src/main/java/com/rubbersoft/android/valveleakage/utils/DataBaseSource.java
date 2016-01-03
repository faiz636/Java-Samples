package com.rubbersoft.android.valveleakage.utils;

import com.rubbersoft.android.valveleakage.model.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Muhammad Muzammil on 26-Dec-15.
 */
public class DataBaseSource {

    private static final String TAG = "DataBaseSource";

    public List<Data> dataNode1;
    public List<Data> dataNode2;
    public List<Data> dataNode3;
    public List<Data> dataNode4;

    private DataBaseSource() {
        dataNode1 = new ArrayList<>();
        dataNode2 = new ArrayList<>();
        dataNode3 = new ArrayList<>();
        dataNode4 = new ArrayList<>();
    }

    public static DataBaseSource getInstance() {
        return Holder.INSTANCE;
    }

    public void insertData(Data data, String nodeName) {
        AppLog.i(TAG + "-insertData", nodeName + " --- " + data);
        switch (nodeName) {
            case ConfigConstants.TABLE_NODE1:
                dataNode1.add(0, data);
                break;
            case ConfigConstants.TABLE_NODE2:
                dataNode2.add(0, data);
                break;
            case ConfigConstants.TABLE_NODE3:
                dataNode3.add(0, data);
                break;
            case ConfigConstants.TABLE_NODE4:
                dataNode4.add(0, data);
                break;
            default:
                AppLog.e(TAG + "-insertData", "insertData() called with wrong Node");
        }
    }

    public void removeData(String key, String nodeName) {
        if (nodeName == null) {
            AppLog.e(TAG + "-insertData", "insertData() called with null node name");
            return;
        }
        List<Data> list;
        switch (nodeName) {
            case ConfigConstants.TABLE_NODE1:
                list = dataNode1;
                break;
            case ConfigConstants.TABLE_NODE2:
                list = dataNode2;
                break;
            case ConfigConstants.TABLE_NODE3:
                list = dataNode3;
                break;
            case ConfigConstants.TABLE_NODE4:
                list = dataNode4;
                break;
            default:
                list = null;
                AppLog.e(TAG + "-insertData", "insertData() called with wrong Node");
        }
        if (list == null) {
            return;
        }
        Iterator<Data> iterator = list.iterator();
        while (iterator.hasNext()){
            Data d = iterator.next();
            if (key.compareTo(d.getKey()) == 0) {
                list.remove(d);
                break;
            }
        }

//        // TODO: 03/01/2016 send broadcase for dataset change
    }


    private static class Holder {
        static final DataBaseSource INSTANCE = new DataBaseSource();
    }

}
