package com.ciplafoundation.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ciplafoundation.R;
import com.ciplafoundation.activities.FragmentBaseActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessageService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("Msg", "Message received [" + remoteMessage + "]");
        int when = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, FragmentBaseActivity.class);
        intent.putExtra("roleId",remoteMessage.getData().get("roleId"));
        intent.putExtra("notification-type",remoteMessage.getData().get("notification-type"));
        intent.putExtra("divisionId",remoteMessage.getData().get("divisionId"));
        intent.putExtra("userId",remoteMessage.getData().get("userId"));
        intent.putExtra("projectPanId",remoteMessage.getData().get("projectPanId"));
        intent.putExtra("Id",remoteMessage.getData().get("Id"));
        intent.putExtra("deviationId",remoteMessage.getData().get("deviationId"));
        intent.putExtra("PUSH","isPush");


     //   intent.putExtra("roleId","3");
        // USR_USER_ID=USR0000021&ID=PRS0000000298&DIVISION_ID=D0000001&ROLE_ID=3
      //  intent.putExtra("notification-type","proposal-view");
        //intent.putExtra("notification-type","project-view");
      //  intent.putExtra("divisionId","D0000001");
       // intent.putExtra("userId","USR0000021");
      // intent.putExtra("Id","PRS0000000298");
       // intent.putExtra("Id","PRJ0000039");


        // for project deviation
       /* intent.putExtra("PUSH","isPush");
        intent.putExtra("notification-type","project-deviation-view");
        intent.putExtra("userId","USR0000021");
        intent.putExtra("Id","PRJ0000289");
        intent.putExtra("divisionId","D0000007");
        intent.putExtra("roleId","3");
        intent.putExtra("PRD_ID","PRD0000000026");*/



        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, when, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setColor(getApplication().getResources().getColor(R.color.loginbtnclicked))
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Cipla")
                .setContentText(remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setSound(uri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(when, notificationBuilder.build());

    }
}


// help website -- "https://www.simplifiedcoding.net/firebase-cloud-messaging-android/"   /