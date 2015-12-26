package com.rubbersoft.android.valveleakage.firebase;

import com.firebase.client.Firebase;
import com.rubbersoft.android.valveleakage.utils.ConfigConstants;

/**
 * Created by Faiz on 26/12/2015.
 */
public class FirebaseHandler {

    private static FirebaseHandler OUR_INSTANCE;
    private Firebase firebaseRef;

    private Firebase node1,node2,node3,node4;

    private FirebaseHandler() {
        firebaseRef = new Firebase(ConfigConstants.FIREBASE_URL);
        initChildRefs();
    }

    public static FirebaseHandler getInstance() {
        return OUR_INSTANCE == null ? (OUR_INSTANCE = new FirebaseHandler()) : OUR_INSTANCE;
    }

    private void initChildRefs(){
        node1 = firebaseRef.child("node1");
        node2 = firebaseRef.child("node2");
        node3 = firebaseRef.child("node3");
        node4 = firebaseRef.child("node4");
    }

    public Firebase getRootRef() {
        return firebaseRef;
    }

    public Firebase getNode1Ref() {
        return node1;
    }

    public Firebase getNode2Ref() {
        return node2;
    }

    public Firebase getNode3Ref() {
        return node3;
    }

    public Firebase getNode4Ref() {
        return node4;
    }
}
