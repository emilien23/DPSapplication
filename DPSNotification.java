package com.testpush;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Эмиль on 26.03.2017.
 */

public class DPSNotification extends Activity{
    private static final int NOTIFY_ID = 101;
    long[] vibrate = new long[] { 1000, 1000};
    public static final int Radius = 6371;
    public static final int Distance = 300;

    private void Start(Context context) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_police_car)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_police_car))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("Предупреждение!")
                .setContentText("Вы приближаетесь к посту ДПС");
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_VIBRATE;
        notification.vibrate = vibrate;
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }
    public void SearchDpsNear (Context context, ArrayList<HashMap<String,String>> markers, double myLat, double myLng) {
        for (int i = 0; i < markers.size(); i++) {
            HashMap<String, String> map;
            map = markers.get(i);
            double markerLat = Double.parseDouble(map.get("lat"));
            double markerLng = Double.parseDouble(map.get("lng"));
            if (CheckDps(myLat,myLng,markerLat, markerLng)) {
                Start(context);
                break;
            }
        }
    }
    private boolean CheckDps(double myLat, double myLng, double markerLat, double markerLng) {
        final double dLng = Math.toRadians(myLng - markerLng);
        final double dLat = Math.toRadians(myLat - markerLat);
        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(markerLat))
                * Math.cos(Math.toRadians(myLat)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        final double c = Radius * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return c <= Distance ? true : false;
    }

}
