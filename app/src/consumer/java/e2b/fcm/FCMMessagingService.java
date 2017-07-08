package e2b.fcm;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.e2b.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

import e2b.activity.OrderDetailActivity;
import e2b.activity.OrdersActivity;

public class FCMMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMMessagingService";
    private PendingIntent pendingIntent;
    private NotificationCompat.Builder notificationBuilder;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification());
        sendNotification(remoteMessage);
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(RemoteMessage messageBody) {

        Map<String, String> map = messageBody.getData();
        if (map != null) {
            String msg = map.get("msg");

            String payload = map.get("payload");
            if (payload != null && !"".equals(payload)) {
                try {
                    JSONObject object = new JSONObject(payload);
                    String id = object.getString("id");
                    String s = object.getString("s");
                    Log.i(TAG, "Noti : " + msg + " , " + "status : " + " , " + s + " , "+ " orderid : "+id);
                    sendNotification(msg, s, id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void sendNotification(String msg, String status, String id) {
        // Creates an Intent for the Activity
        long ID = new Date().getTime();
        Intent notifyIntent = setNavigationIntent(status, id);
                /*new Intent(this, HomeActivity.class);*/
        // Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // Creates the PendingIntent
        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        // Instantiate a Builder object.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(msg)
                .setAutoCancel(true)
                .setContentIntent(notifyPendingIntent)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setAutoCancel(true);

        // Puts the PendingIntent into the notification builder
        builder.setContentIntent(notifyPendingIntent);
        // Notifications are issued by sending them to the
        // NotificationManager system service.
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds an anonymous Notification object from the builder, and
        // passes it to the NotificationManager
        mNotificationManager.notify((int) ID, builder.build());
    }

    private Intent setNavigationIntent(String status, String id) {
        Intent intent = null;
        Bundle bundle = new Bundle();
                intent = new Intent(this, OrderDetailActivity.class);
                bundle.putString("orderId", id);
        if (intent != null)
            intent.putExtras(bundle);
        return intent;
    }

//    private void sendNotification(String msg, String status, String id) {
//
//        notificationBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle(getString(R.string.app_name))
//                        .setContentText(msg)
//                        .setAutoCancel(true)
//                        .setContentIntent(pendingIntent);
//        notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
//        notificationBuilder.setAutoCancel(true);
//
//        NotificationManagerCompat notificationManager =
//                NotificationManagerCompat.from(this);
//        notificationManager.notify(0, notificationBuilder.build());
//    }
}