package com.rubbersoft;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.rubbersoft.model.SheetData;
import com.rubbersoft.model.SheetRow;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Faiz on 23/12/2015.
 */
public class FirebaseDataSender {

    private static final String FIREBASE_URL = "https://incandescent-torch-9709.firebaseio.com/";
    private boolean mWait;
    private static Firebase mRootRef;
    private static AuthData mUserAuth;
    private ArrayList<SheetData> sheetDataList;

    static {
        mRootRef = new Firebase(FIREBASE_URL);
    }

    public FirebaseDataSender(ArrayList<SheetData> sheetDataList) {
        this.sheetDataList = sheetDataList;
        this.mWait = true;
        run();
    }

    public void run() {
        checkForInterNetConnection();
        if (mUserAuth==null){
            loginUser("a@b.com", "123");
        }
        sendData();
    }

    /**
     * wait for internet connection until available
     */
    private void checkForInterNetConnection() {//check for internet connection(connection with google server :)
        while (!netIsAvailable()) {
            System.out.println("Internet Connection Not Available, Retrying In 5 sec...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Internet Available, proceeding...");
    }


    /**
     * create a user with the email and password provided for firebase authentication
     *
     * @param email email of the user
     * @param pass password of the user for firebase authentication
     */
    public void createUser(String email, String pass) {
        mWait = true;
        mRootRef.createUser(email, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                mWait = false;
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                processErrorCode(firebaseError);
                mWait = false;
            }
        });
        //wait for user creation
        waitToCompleteOperation();
    }

    /**
     * authenticate with firebase database
     *
     * @param email user email
     * @param pass user password
     * */
    public synchronized void loginUser(String email, String pass) {
        if(mUserAuth!=null) return;
        mWait = true;
        System.out.println("logging in user");
        mRootRef.authWithPassword(email, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                FirebaseDataSender.mUserAuth = authData;
                mWait = false;
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
                processErrorCode(firebaseError);
                mWait = false;
            }
        });
        waitToCompleteOperation();//this time wait for login
        System.out.println("user logged in");
    }

    /**
     * try to connect google
     *
     * @return true if connection is established else false
     */
    private static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            //this exception will occur if url is incorrect
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Send data to firebase passed to this thread.
     * Each data item will be sent to firebase separately
     * then waits for its completion.
     * After completion next item in the collection will be sent
     */
    private void sendData() {
        long t;
        t = System.currentTimeMillis();
        final int[] wait = {0};
        for (int i = 0; i < 4; i++) {
            ArrayList<SheetRow> list = sheetDataList.get(i).getAllRows();
            System.out.println("sending node " + (i + 1) + " data, size : " + list.size());
            for (SheetRow item : list) {
                wait[0]++;
                mRootRef.child("node" + (i + 1)).push().setValue(new SheetRow.FirebaseData(item), new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError == null) {
                            System.out.println("row sent");
                        } else {
                            processErrorCode(firebaseError);
                        }
                        mWait = --wait[0] > 0;
                    }
                });
            }
            System.out.println("All rows sent");
        }
        System.out.println("pushing data total time taken " + (System.currentTimeMillis() - t) + "ms, " + ((System.currentTimeMillis() - t) / (1000)) + "sec");
        mWait = wait[0] > 0;
        t = System.currentTimeMillis();
        waitToCompleteOperation();
        System.out.println("waiit time for sending data total time taken " + (System.currentTimeMillis() - t) + "ms, " + ((System.currentTimeMillis() - t) / (1000)) + "sec");
        System.out.println("All nodes data sent");
    }

    /**
     * Wait to complete operation from where it is called
     * when calling operation is completed
     * wait flag would be set to false
     */
    public void waitToCompleteOperation() {
        int i = 0;
        while (mWait) {
            try {
                System.out.println("waiting" + i++);
                Thread.sleep(100);
                if (!netIsAvailable()) {
                    System.out.println("Connection Lost");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("waiting end");
    }

    /**
     * process error code received from firebase
     */
    public void processErrorCode(FirebaseError firebaseError) {
        if (firebaseError == null) {
            return;
        }
        System.out.println("processing error code:" + firebaseError);
        switch (firebaseError.getCode()) {
            case FirebaseError.AUTHENTICATION_PROVIDER_DISABLED:
                System.out.println("AUTHENTICATION_PROVIDER_DISABLED");
                break;
            case FirebaseError.DISCONNECTED:
                System.out.println("DISCONNECTED");
                break;
            case FirebaseError.DENIED_BY_USER:
                System.out.println("DENIED_BY_USER");
                break;
            case FirebaseError.EMAIL_TAKEN:
                System.out.println("EMAIL_TAKEN");
                break;
            case FirebaseError.INVALID_AUTH_ARGUMENTS:
                System.out.println("INVALID_AUTH_ARGUMENTS");
                break;
            case FirebaseError.INVALID_EMAIL:
                System.out.println("INVALID_EMAIL");
                break;
            case FirebaseError.EXPIRED_TOKEN:
                System.out.println("EXPIRED_TOKEN");
                break;
            case FirebaseError.PERMISSION_DENIED:
                System.out.println("PERMISSION_DENIED");
                break;
            case FirebaseError.INVALID_PASSWORD:
                System.out.println("INVALID_PASSWORD");
                break;
            case FirebaseError.INVALID_CREDENTIALS:
                System.out.println("INVALID_CREDENTIALS");
                break;
            case FirebaseError.INVALID_CONFIGURATION:
                System.out.println("INVALID_CONFIGURATION");
                break;
            case FirebaseError.NETWORK_ERROR:
                System.out.println("NETWORK_ERROR");
                break;
        }

    }

}
