package id.co.imastudio.popmov;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by idn on 8/26/2017.
 */

public class MyAlarmReceiver extends BroadcastReceiver{
    private int NOTIFICATION_ID = 1;
    private static final String TAG = "MyAlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //masuk aplikasi
        Intent notificationIntent = new Intent(context, TabActivity.class);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context)
                .setContentTitle("Sudah 15 menit!")
                .setContentText("Cek aplikasinya.")
                .setSmallIcon(R.drawable.ic_action_notif)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(notificationPendingIntent);
        //harus v4
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

        Log.d(TAG, "onReceive() called:");

    }
       
}
