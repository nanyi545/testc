package com.example.testc2.encoder1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaCodec;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.testc2.R;

public class MyService extends Service {
    public MyService() {
    }

    private String TAG = "MyService";

    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification =
                null;



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String channelID = "channel_id";

            NotificationChannel chan = new NotificationChannel(channelID,
                    "channel_name", NotificationManager.IMPORTANCE_NONE);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(chan);

            notification = new Notification.Builder(this, channelID)
                    .setContentTitle("11")
                    .setContentText("222")
                    .build();
        }

// Notification ID cannot be 0.
        startForeground(11, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"---onBind---");
        return null;
    }
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private MediaCodec mediaCodec;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service onStartCommand() is called");
//        startForeground();

        this.mediaProjectionManager = (MediaProjectionManager)getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent captureIntent = mediaProjectionManager.createScreenCaptureIntent();



        return Service.START_STICKY;
    }

}
