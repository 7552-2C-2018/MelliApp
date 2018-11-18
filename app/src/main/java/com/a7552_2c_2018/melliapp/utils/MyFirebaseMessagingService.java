package com.a7552_2c_2018.melliapp.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.ChatActivity;
import com.a7552_2c_2018.melliapp.activity.HomeActivity;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String chatId = "";
    private String title = "";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            try {
                title = remoteMessage.getData().get("title");
                checkMessage(remoteMessage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void checkMessage(RemoteMessage remoteMessage) throws JSONException {
        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);
        chatId = object.getString("id");
        getChats();
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatId", chatId);
        intent.putExtra("title", title);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                        .setContentTitle("Comprame")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }

        Objects.requireNonNull(notificationManager).notify(1 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
    }

    private void getChats() {
        String REQUEST_TAG = "getChats";
        String url = getString(R.string.remote_chats) +
                "userChats/" + SingletonUser.getInstance().getUser().getFacebookID() + ".json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                getChatsResponse(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, volleyError.toString());
            }
        });

        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(request,REQUEST_TAG);
    }

    private void getChatsResponse(String response) {
        Log.d(TAG, response);
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray chats = obj.getJSONArray("chats");
            for (int i=0; i<chats.length(); i++){
                if (chatId == chats.getString(i)){
                    sendNotification(title);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}