package code.common;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zozima.android.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import code.notification.NotificationActivity;
import code.product.ProductDetailsActivity;
import code.utils.AppUtils;

import static android.util.Log.e;

/**
 * Created by Mohammad Faiz on 3/29/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    String CHANNEL_ID = "com.mrnmrsekart";
    private NotificationUtils notificationUtils;

    private NotificationManager mManager;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "From: " + remoteMessage.getFrom());


        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData());

                //JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(jsonObject);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            try {

                /*Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notify=new Notification.Builder
                    (getApplicationContext()).setContentTitle("Newnuk").setContentIntent(pendingIntent).setContentText(message).setSmallIcon(R.mipmap.logo).build();

            notify.flags |= Notification.FLAG_SHOW_LIGHTS;
            notif.notify(0, notify);*/

            } catch (Exception e) {
                e.printStackTrace();
            }


        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {

            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            long time= AppUtils.currentTimestamp();

            String message = json.getString("message");
            String imageUrl = json.getString("largeIcon");
            String title = json.getString("title");
            String id = json.getString("id");
            String add_date = json.getString("add_date");
            String datetime = String.valueOf(time);
            String type = json.getString("type");

            e(TAG, "message: " + message);
            e(TAG, "largeIcon: " + imageUrl);
            e(TAG, "title: " + title);
            e(TAG, "id: " + id);
            e(TAG, "add_date: " + add_date);
            e(TAG, "datetime: " + datetime);
            e(TAG, "type: " + type);

           /* ContentValues mContentValues = new ContentValues();

            mContentValues.put(TablePush.pushColumn.message.toString(), json.get("message").toString());
            mContentValues.put(TablePush.pushColumn.largeIcon.toString(), json.get("largeIcon").toString());
            mContentValues.put(TablePush.pushColumn.title.toString(), json.get("title").toString());
            mContentValues.put(TablePush.pushColumn.id.toString(), json.get("id").toString());
            mContentValues.put(TablePush.pushColumn.add_date.toString(),  json.get("add_date").toString());
            mContentValues.put(TablePush.pushColumn.datetime.toString(),  datetime);
            mContentValues.put(TablePush.pushColumn.type.toString(),  json.get("type").toString());
            DatabaseController.insertUpdateData(mContentValues, TablePush.push, TablePush.pushColumn.id.toString(),json.get("id").toString());

*/

            if (TextUtils.isEmpty(imageUrl)) {
                //showNotificationMessage(getApplicationContext(), title, message,datetime,resultIntent,type);
                createNotification(message,title,type,id);
            } else {
                // image is present, show notification with image
                //showNotificationMessageWithBigImage(getApplicationContext(), title, message,datetime,resultIntent, imageUrl,type);

                Bitmap bitmap = getBitmapFromURL(imageUrl);

                if (bitmap != null) {
                    createNotificationBig(message,title,type,id,bitmap);
                } else {
                    createNotification(message,title,type,id);
                }
            }

        } catch (JSONException e) {
            e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent,String type) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent,type);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl,String type) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl,type);
    }



    public void createNotification(String message, String title,String type,String id) {

        Log.d("Step2", "Step2");


        Intent resultIntent = null;

        if(type.equals("2"))
        {
            // app is in background, show the notification in notification tray
            resultIntent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
            resultIntent.putExtra("message", message);
            resultIntent.putExtra("type", type);
            resultIntent.putExtra("id", id);
        }
        else
        {
            resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
            resultIntent.putExtra("message", message);
            resultIntent.putExtra("type", type);
            resultIntent.putExtra("id", id);
        }


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel androidChannel = new NotificationChannel(CHANNEL_ID,
                    title, NotificationManager.IMPORTANCE_HIGH);
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            androidChannel.setLightColor(Color.GREEN);
            //androidChannel.setSound(null, null);

            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(androidChannel);

            NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon( R.mipmap.logo)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setTicker(title)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setSound(defaultUri)
                    .setContentIntent(contentIntent);

            getManager().notify(Integer.parseInt(id), notification.build());

        } else {
            try {

                playNotificationSound();

                @SuppressLint({"NewApi", "LocalSuppress"}) androidx.core.app.NotificationCompat.Builder notificationBuilder = new androidx.core.app.NotificationCompat.Builder(this).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle(title)
                        .setTicker(title)
                        .setAutoCancel(true)
                        .setContentText(message)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setContentIntent(contentIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setLights(0xFF760193, 300, 1000)
                        .setAutoCancel(true).setVibrate(new long[]{200, 400});

                long timestamp = AppUtils.currentTimestamp();

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify((int) timestamp/* ID of notification */, notificationBuilder.build());

            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }
    }

    public void createNotificationBig(String message, String title,String type,String id,Bitmap bitmap) {

        Log.d("Step2", "Step2");

        Intent resultIntent = null;

        if(type.equals("2"))
        {
            // app is in background, show the notification in notification tray
            resultIntent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
            resultIntent.putExtra("message", message);
            resultIntent.putExtra("type", type);
            resultIntent.putExtra("id", id);
        }
        else
        {
            resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
            resultIntent.putExtra("message", message);
            resultIntent.putExtra("type", type);
            resultIntent.putExtra("id", id);
        }


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel androidChannel = new NotificationChannel(CHANNEL_ID,
                    title, NotificationManager.IMPORTANCE_HIGH);
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            androidChannel.setLightColor(Color.GREEN);
            //androidChannel.setSound(null, null);

            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(androidChannel);

            NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setTicker(title)
                    .setAutoCancel(true)
                    .setStyle(bigPictureStyle)
                    .setSound(defaultUri)
                    .setContentIntent(contentIntent);

            getManager().notify(Integer.parseInt(id), notification.build());

        } else {
            try {

                playNotificationSound();

                @SuppressLint({"NewApi", "LocalSuppress"}) androidx.core.app.NotificationCompat.Builder notificationBuilder = new androidx.core.app.NotificationCompat.Builder(this).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle(title)
                        .setTicker(title)
                        .setAutoCancel(true)
                        .setContentText(message)
                        .setStyle(bigPictureStyle)
                        .setContentIntent(contentIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setLights(0xFF760193, 300, 1000)
                        .setAutoCancel(true).setVibrate(new long[]{200, 400});

                long timestamp = AppUtils.currentTimestamp();

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify((int) timestamp/* ID of notification */, notificationBuilder.build());

            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }
    }


    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(this, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}