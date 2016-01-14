package com.rubbersoft.android.valveleakage.utils;

import android.content.Context;

/**
 * Created by Muhammad Faizan Khan on 26/12/15.
 */
public class ConfigConstants {

    public static final String FIREBASE_URL = "https://incandescent-torch-9709.firebaseio.com/";
    // Table name for each node
    public static final String TABLE_NODE1 = "node1";
    public static final String TABLE_NODE2 = "node2";
    public static final String TABLE_NODE3 = "node3";
    public static final String TABLE_NODE4 = "node4";

    public static final String INTENT_EXTRA_NODE = "nodeName";

    public static final String RECEIVER_ACTION_NODE1 = "com.rubbersoft.android.valveleakage.ui.MainActivity.RECEIVER_ACTION.NODE1";
    public static final String RECEIVER_ACTION_NODE2 = "com.rubbersoft.android.valveleakage.ui.MainActivity.RECEIVER_ACTION.NODE2";
    public static final String RECEIVER_ACTION_NODE3 = "com.rubbersoft.android.valveleakage.ui.MainActivity.RECEIVER_ACTION.NODE3";
    public static final String RECEIVER_ACTION_NODE4 = "com.rubbersoft.android.valveleakage.ui.MainActivity.RECEIVER_ACTION.NODE4";

    public static final int NODE1_NOTIFICATION_ID = 1,
            NODE2_NOTIFICATION_ID = 2,
            NODE3_NOTIFICATION_ID = 3,
            NODE4_NOTIFICATION_ID = 4;

    public static interface ui {
        String MainActivity = "ui.MainActivity";
    }
}
