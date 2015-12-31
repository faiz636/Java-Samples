package com.rubbersoft;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.rubbersoft.model.SheetRow;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Created by Faiz on 27/12/2015.
 */
public class FirebaseDummyDataSender implements Runnable {

    private static final String FIREBASE_URL = "https://incandescent-torch-9709.firebaseio.com/";
    private static Firebase mRootRef;
    private static AuthData mUserAuth;
    private static int runCounter;
    static {
        mRootRef = new Firebase(FIREBASE_URL);
    }
    private boolean mWait;
    private Thread mThread;
    private String status, threadName;

    public FirebaseDummyDataSender() {
        this.mThread = new Thread(this, "FirebaseDummyDataSender");
        this.mWait = true;
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

    public Thread getThread() {
        return mThread;
    }

    @Override
    public void run() {
        threadName = "DummyThread-" + runCounter++;
        checkForInterNetConnection();
        if (mUserAuth == null) {
            loginUser("a@b.com", "123");
        }
        sendData();
    }

    /**
     * wait for internet connection until available
     */
    private void checkForInterNetConnection() {//check for internet connection(connection with google server :)
        while (!netIsAvailable()) {
            printMessage("Internet Connection Not Available, Retrying In 5 sec...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        printMessage("Internet Available, proceeding...");
    }

    /**
     * create a user with the email and password provided for firebase authentication
     *
     * @param email email of the user
     * @param pass  password of the user for firebase authentication
     */
    public void createUser(String email, String pass) {
        mWait = true;
        mRootRef.createUser(email, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                printMessage("Successfully created user account with uid: " + result.get("uid"));
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
        status = "waiting create user";
        waitToCompleteOperation();
    }

    /**
     * authenticate with firebase database
     *
     * @param email user email
     * @param pass  user password
     */
    public synchronized void loginUser(String email, String pass) {
        if (mUserAuth != null) return;
        mWait = true;
        printMessage("logging in user");
        mRootRef.authWithPassword(email, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                printMessage("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                FirebaseDummyDataSender.mUserAuth = authData;
                mWait = false;
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
                processErrorCode(firebaseError);
                mWait = false;
            }
        });
        status = "waiting for user to login";
        waitToCompleteOperation();//this time wait for login
        printMessage("user logged in");
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
            wait[0]++;
            SheetRow.FirebaseData dummyFirebaseData = new SheetRow.FirebaseData(t, (float) (150 * Math.random()), (float) (400 * Math.random()));
            mRootRef.child("node" + (i + 1)).push().setValue(dummyFirebaseData, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError == null) {
                        printMessage("row sent");
                    } else {
                        processErrorCode(firebaseError);
                    }
                    mWait = --wait[0] > 0;
                }
            });
            printMessage("All rows sent");
        }
        mWait = wait[0] > 0;
        status = "waiting for sending data";
        waitToCompleteOperation();
        printMessage("All nodes data sent");
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
                printMessage(status + "--" + i++);
                Thread.sleep(500);
                if (!netIsAvailable()) {
                    printMessage("Connection Lost");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        printMessage("waiting end");
    }

    private void printMessage(String message) {
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
