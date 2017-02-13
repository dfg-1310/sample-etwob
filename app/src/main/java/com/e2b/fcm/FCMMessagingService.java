package com.e2b.fcm;


import android.app.PendingIntent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.e2b.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

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
            String notiType = map.get("type");
            String newsFeed = map.get("newsFeed");
            if (newsFeed != null && !"".equals(newsFeed)) {
                try {
                    JSONObject object = new JSONObject(newsFeed);
                    String _id = object.getString("_id");
                    String type = object.getString("type");
                    Log.i(TAG, "Noti : " + msg + " , " + notiType + " , " + _id + " , " + type);
                    sendNotification(msg, Integer.parseInt(notiType), _id, Integer.parseInt(type));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private void sendNotification(String msg, int notificationType, String id, int type) {

        notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
        notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        notificationBuilder.setAutoCancel(true);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.notify(0, notificationBuilder.build());
    }
}