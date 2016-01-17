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

    //address of firebase databse
    private static final String FIREBASE_URL = "https://incandescent-torch-9709.firebaseio.com/";
    private boolean mWait;//flag for wait operation
    private static Firebase mRootRef;//usable root reference for firebase database
    private static AuthData mUserAuth;//authentication data from firebase
    private ArrayList<SheetData> sheetDataList;//list of sheet data to be send
    private String threadName;//name of running instance
    private static int runCounter;//to check which run it is

    static {
        mRootRef = new Firebase(FIREBASE_URL);//create firebase root reference
    }

    public FirebaseDataSender(ArrayList<SheetData> sheetDataList) {
        this.sheetDataList = sheetDataList;//save data base object
        this.mWait = true;
        run();//start necessary operation
    }

    public void run() {
        threadName = "FirebaseDataSender-"+runCounter++;//set name of running instance and counter
        checkForInterNetConnection();//check for internet connection
        if (mUserAuth==null){//login if not authenticated
            loginUser("a@b.com", "123");
        }
        sendData();//send data to firebase
    }

    /**
     * wait for internet connection until available
     */
    private void checkForInterNetConnection() {//check for internet connection(connection with google server :)
        while (!netIsAvailable()) {
            printMessage("Internet Connection Not Available, Retrying In 5 sec...");
            try {
                Thread.sleep(1000);//wait for second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        printMessage("Internet Available, proceeding...");
    }

    /**
     * authenticate with firebase database
     *
     * @param email user email
     * @param pass user password
     * */
    public synchronized void loginUser(String email, String pass) {
        if(mUserAuth!=null) return;//if already login
        mWait = true;
        printMessage("logging in user");
        //authenticate with email and password
        mRootRef.authWithPassword(email, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {//firebase response if authentication is success
                printMessage("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                FirebaseDataSender.mUserAuth = authData;
                mWait = false;
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {//firebase response if not unsuccessful
                // there was an error
                processErrorCode(firebaseError);
                mWait = false;
            }
        });
        waitToCompleteOperation("waiting for user to login");//wait for login complete
        printMessage("user logged in");
    }

    /**
     * try to connect google
     *
     * @return true if connection is established else false
     */
    private static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");//make a url
            final URLConnection conn = url.openConnection();//obtain a connection with url
            conn.connect();//connect with setver
            return true;//will reach here only if connection is establish
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
        final int[] wait = {0};//waiting counter in array
        for (int i = 0; i < 4; i++) {//loop for sheet
            int j=0;
            ArrayList<SheetRow> list = sheetDataList.get(i).getAllRows();
            printMessage("sending node " + (i + 1) + " data, size : " + list.size());
            for (SheetRow item : list) {//loop for rows
                wait[0]++;//increase wile sending data
                System.out.println(" ------------> sending : "+ j++);
                //get firebase data
                SheetRow.FirebaseData data = new SheetRow.FirebaseData(item);
                //send data to firebase
                mRootRef.child("node" + (i + 1)).push().setValue(data, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {//response for data sent
                        if (firebaseError == null) {
                            printMessage("row sent");
                        } else {
                            processErrorCode(firebaseError);
                        }
                        mWait = --wait[0] > 0;//decrement wait counter and update wait flag
                    }
                });
            }
            printMessage("All rows sent");
        }
        mWait = wait[0] > 0;
        waitToCompleteOperation("data sent waiting for acknowledgement");//wait for data to be sent
        printMessage("All nodes data sent");
    }

    /**
     * Wait to complete operation from where it is called
     * when calling operation is completed
     * wait flag would be set to false
     */
    public void waitToCompleteOperation(String s) {
        int i = 0;
        while (mWait) {//if wait is enable
            try {
                printMessage(s + "--" + i++);
                Thread.sleep(500);//wait for 500 ms
                if (!netIsAvailable()) {
                    printMessage("Connection Lost");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        printMessage("waiting end");
    }

    //print formatted data
    private void printMessage(String message){
        System.out.println(threadName + "--" + message);
    }

    /**
     * process error code received from firebase
     */
    public void processErrorCode(FirebaseError firebaseError) {
        if (firebaseError == null) {
            return;
        }
        printMessage("processing error code:" + firebaseError);
        switch (firebaseError.getCode()) {
            case FirebaseError.AUTHENTICATION_PROVIDER_DISABLED:
                printMessage("AUTHENTICATION_PROVIDER_DISABLED");
                break;
            case FirebaseError.DISCONNECTED:
                printMessage("DISCONNECTED");
                break;
            case FirebaseError.DENIED_BY_USER:
                printMessage("DENIED_BY_USER");
                break;
            case FirebaseError.EMAIL_TAKEN:
                printMessage("EMAIL_TAKEN");
                break;
            case FirebaseError.INVALID_AUTH_ARGUMENTS:
                printMessage("INVALID_AUTH_ARGUMENTS");
                break;
            case FirebaseError.INVALID_EMAIL:
                printMessage("INVALID_EMAIL");
                break;
            case FirebaseError.EXPIRED_TOKEN:
                printMessage("EXPIRED_TOKEN");
                break;
            case FirebaseError.PERMISSION_DENIED:
                printMessage("PERMISSION_DENIED");
                break;
            case FirebaseError.INVALID_PASSWORD:
                printMessage("INVALID_PASSWORD");
                break;
            case FirebaseError.INVALID_CREDENTIALS:
                printMessage("INVALID_CREDENTIALS");
                break;
            case FirebaseError.INVALID_CONFIGURATION:
                printMessage("INVALID_CONFIGURATION");
                break;
            case FirebaseError.NETWORK_ERROR:
                printMessage("NETWORK_ERROR");
                break;
        }

    }

}
