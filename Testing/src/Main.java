import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseApp;
import com.firebase.client.FirebaseError;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        FirebaseThread ft = new FirebaseThread();
        try {
            ft.getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ending main thread");
//        System.exit(1);
//        Runtime.getRuntime().
    }
}

class FirebaseThread implements Runnable {

    private static final String FIREBASE_URL = "https://incandescent-torch-9709.firebaseio.com/";
    public boolean mWait;
    Thread mThread;
    private Firebase mRootRef;
    private AuthData mUserAuth;

    FirebaseThread() {
        this.mThread = new Thread(this, "FirebaseThread");
        mRootRef = new Firebase(FIREBASE_URL);
        mThread.start();
        this.mWait = true;
    }

    private static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void run() {
        checkForInterNetConnection();
//        createUser("a@b.com", "123");
        loginUser("a@b.com", "123");
        sendData();
//        waitToCompleteOperation();//not needed here as of now waiting for each individual operation where needed
//        Thread.currentThread().stop();
//        mRootRef.unauth();
    }

    public void sendData() {
        mWait = true;
        mRootRef.child("test").setValue("Building jar file2...", new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError == null) {
                    System.out.println("data sent");
                } else {
                    processErrorCode(firebaseError);
                }
//                System.out.println(firebaseError);
//                System.out.println(firebase);
                mWait = false;
            }
        });
        waitToCompleteOperation();
        System.out.println("data sent");
    }

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
        waitToCompleteOperation();//wait for creating user
    }

    public void loginUser(String email, String pass) {
        mWait = true;
        System.out.println("logging in user");
        mRootRef.authWithPassword(email, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                FirebaseThread.this.mUserAuth = authData;
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

    public void checkForInterNetConnection() {//check for internet connection(connection with google server :)
        while (!netIsAvailable()) {
            System.out.println("Internet Connection Not Available, Retrying In 5 sec...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Internet Available, proceeding...");
    }

    public void waitToCompleteOperation() {
        System.out.println("waiting start");
        while (mWait) {
            try {
                System.out.println("waiting");
                Thread.sleep(2000);
                if (!netIsAvailable()) {
                    System.out.println("Connection Lost");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("waiting end");
    }

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

    public Thread getThread() {
        return mThread;
    }


}
