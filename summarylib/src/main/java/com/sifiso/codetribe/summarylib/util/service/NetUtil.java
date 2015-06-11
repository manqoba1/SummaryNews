package com.sifiso.codetribe.summarylib.util.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.sifiso.codetribe.summarylib.MainActivity;
import com.sifiso.codetribe.summarylib.MainPaneCategory;
import com.sifiso.codetribe.summarylib.R;

/**
 * Created by sifiso on 4/3/2015.
 */
public class NetUtil {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static void getConnectivityStatusString(Intent msgIntent,Context context) {
        int conn = NetUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetUtil.TYPE_WIFI) {
            sendNotification(msgIntent,context);
        } else if (conn == NetUtil.TYPE_MOBILE) {
            sendNotification(msgIntent,context);
        }

    }

   static NotificationManager mNotificationManager;
    public static final int NOTIFICATION_ID = 1;

    private static void sendNotification(Intent msgIntent, Context ctx) {

        mNotificationManager = (NotificationManager) ctx
                .getSystemService(ctx.NOTIFICATION_SERVICE);

        Intent resultIntent = new Intent(ctx, MainPaneCategory.class);
        resultIntent.putExtra("refresh", true);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        //stackBuilder.addParentStack(MainPagerActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        String title = ctx.getResources().getString(R.string.notification_title);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText("Update Summary News");

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }
}
