package com.hayashi.android_geogence;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;

// 参考
// https://developer.android.com/guide/topics/ui/notifiers/notifications?hl=ja

public class MyNotification {

    private Context context = null;
    private Class resultActivity = null;

    private int notificationId = 0;
    private int requestCode = 0;

    String channelId = "channeld";
    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    String title = "タイトル";
    String text = "テキスト";
    int icon = R.drawable.ic_launcher_foreground;


    public MyNotification(Context context) {
        this(context, context.getClass());
    }

    public MyNotification(Context context, Class resultActivity) {
        this.context = context;
        this.resultActivity = resultActivity;
    }

    public void run() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                        .setSound(defaultSoundUri)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSmallIcon(icon)
                        .setPriority(PRIORITY_MAX); // 通知の優先度を最高に設定

        // 明示的インテント
        Intent resultIntent = new Intent(context, resultActivity);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(context.getClass());
        // スタックの最初に実装するActivityを追加
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        requestCode,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // notificationIdを利用して通知をアップデート可能
        notificationManager.notify(notificationId, builder.build());
    }
}