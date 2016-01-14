package com.rubbersoft.android.valveleakage.utils;

import android.app.Application;
import android.content.Intent;

import com.rubbersoft.android.valveleakage.ValveLeakageApplication;
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

    public Callback dataChangeCallback1;
    public Callback dataChangeCallback2;
    public Callback dataChangeCallback3;
    public Callback dataChangeCallback4;

    public static DataBaseSource getInstance(){
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final DataBaseSource INSTANCE = new DataBaseSource ();
    }

    private DataBaseSource() {
        dataNode1 = new ArrayList<>();
        dataNode2 = new ArrayList<>();
        dataNode3 = new ArrayList<>();
        dataNode4 = new ArrayList<>();
        initBroadcastIntents();
    }

    public static DataBaseSource getInstance() {
        return Holder.INSTANCE;
    }

    private void initBroadcastIntents(){
        node1receiverIntent = new Intent(ConfigConstants.RECEIVER_ACTION_NODE1);
        node2receiverIntent = new Intent(ConfigConstants.RECEIVER_ACTION_NODE2);
        node3receiverIntent = new Intent(ConfigConstants.RECEIVER_ACTION_NODE3);
        node4receiverIntent = new Intent(ConfigConstants.RECEIVER_ACTION_NODE4);
    }

    public void insertData(Data data, String nodeName) {
        AppLog.i(TAG + "-insertData", nodeName + " --- " + data);
        switch (nodeName) {
            case ConfigConstants.TABLE_NODE1:
                dataNode1.add(0, data);
                ValveLeakageApplication.getContext().sendBroadcast(node1receiverIntent);
                break;
            case ConfigConstants.TABLE_NODE2:
                dataNode2.add(0, data);
                ValveLeakageApplication.getContext().sendBroadcast(node2receiverIntent);
                break;
            case ConfigConstants.TABLE_NODE3:
                dataNode3.add(0, data);
                ValveLeakageApplication.getContext().sendBroadcast(node3receiverIntent);
                break;
            case ConfigConstants.TABLE_NODE4:
                dataNode4.add(0, data);
                ValveLeakageApplication.getContext().sendBroadcast(node4receiverIntent);
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
                dataNode1.add(0,data);
                dataChangeCallback1.callback();
                break;
            case ConfigConstants.TABLE_NODE2:
                dataNode2.add(0,data);
                dataChangeCallback2.callback();
                break;
            case ConfigConstants.TABLE_NODE3:
                dataNode3.add(0,data);
                dataChangeCallback3.callback();
                break;
            case ConfigConstants.TABLE_NODE4:
                dataNode4.add(0,data);
                dataChangeCallback4.callback();
                break;
            default:
                list = null;
                AppLog.e(TAG + "-insertData", "insertData() called with wrong Node");
        }
        if (list == null) {
            return;
        }
        Iterator<Data> iterator = list.iterator();
        while (iterator.hasNext()){//it is tempted but don't change it to for each :D
            Data d = iterator.next();
            if (key.compareTo(d.getKey()) == 0) {
                list.remove(d);
                break;
            }
        }
        switch (nodeName) {
            case ConfigConstants.TABLE_NODE1:
                ValveLeakageApplication.getContext().sendBroadcast(node1receiverIntent);
                break;
            case ConfigConstants.TABLE_NODE2:
                ValveLeakageApplication.getContext().sendBroadcast(node2receiverIntent);
                break;
            case ConfigConstants.TABLE_NODE3:
                ValveLeakageApplication.getContext().sendBroadcast(node3receiverIntent);
                break;
            case ConfigConstants.TABLE_NODE4:
                ValveLeakageApplication.getContext().sendBroadcast(node4receiverIntent);
                break;
        }

    }

    private static class Holder {
        static final DataBaseSource INSTANCE = new DataBaseSource();
    }

}
