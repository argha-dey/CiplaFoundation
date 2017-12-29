package com.ciplafoundation.notification;

import android.content.SharedPreferences;
import android.util.Log;

import com.ciplafoundation.model.UserClass;
import com.ciplafoundation.utility.Util;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseInstanceIdService";

    private UserClass user = new UserClass();
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        System.out.println("Push Key: " + refreshedToken);

        Log.i("Push Key: ", refreshedToken);

         user.setTokenId(refreshedToken);
        Util.saveUserClass(MyFirebaseInstanceIDService.this,user);

       // sendRegistrationToServer(refreshedToken);
        storeRegIdInPref(refreshedToken);
    }





    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
       // prefs = new Prefs(MyFirebaseInstanceIDService.this);
       // prefs.setTokenId(token);
    }



    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("TokenId", token);
        editor.commit();
    }
}

