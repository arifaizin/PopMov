package id.co.imastudio.popmov;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sackcentury.shinebuttonlib.ShineButton;

import id.co.imastudio.popmov.data.MovieContract;

public class DetailActivity extends AppCompatActivity {

    ImageView ivDetailPoster;
    ShineButton btnPaporit;
    SharedPreferences pref;
    String dataJudul, dataPoster;

    private static final String TAG = "DetailActivity";
    private int dataId;
    private NotificationManager mNotifyManager;
    private int NOTIFICATION_ID = 0;
    private static final String ACTION_UPDATE_NOTIFICATION =
            "id.co.imastudio.popmov.ACTION_UPDATE_NOTIFICATION";
    private NotificationReceiver mReceiver = new NotificationReceiver();
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                startActivity(new Intent(DetailActivity.this, Main2Activity.class));
            }
        });

        //terima data
        dataId = getIntent().getIntExtra("DATA_ID",0);
        dataJudul = getIntent().getStringExtra("DATA_JUDUL");
        dataPoster = getIntent().getStringExtra("DATA_POSTER");

        Log.d("DetailActivity",dataId+dataJudul+dataPoster);

        getSupportActionBar().setTitle(dataJudul);

        ivDetailPoster = (ImageView) findViewById(R.id.iv_detail_poster);
        Glide.with(DetailActivity.this).load("https://image.tmdb.org/t/p/w500"+dataPoster).into(ivDetailPoster);

        btnPaporit = (ShineButton) findViewById(R.id.btnFavorit);

        pref = getApplicationContext()
                .getSharedPreferences("SETTING", 0);
        Boolean favorit = pref.getBoolean("FAVORITE"+dataJudul, false);
        if (favorit){
            btnPaporit.setChecked(true);
        }

        btnPaporit.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                SharedPreferences.Editor editor = pref.edit();
                if (checked){
                    //simpan ke favorit
                    editor.putBoolean("FAVORITE"+dataJudul, true);
                    editor.commit();
                    tambahkedatabase();
                    tampilinNotification();
                    buatJadwalAlarm();
                } else {
                    //hapus dari favorit
                    editor.putBoolean("FAVORITE"+dataJudul, false);
                    editor.commit();
                    hapusdaridatabase();
                    updatenotification();
                }
            }


        });
        //di dalam onCreate
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        registerReceiver(mReceiver,new IntentFilter(ACTION_UPDATE_NOTIFICATION));

        //alarm
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    private void buatJadwalAlarm() {
        Intent notificationIntent = new Intent(this, MyAlarmReceiver.class);
        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//        long triggerTime = SystemClock.elapsedRealtime()
//                + AlarmManager.INTERVAL_FIFTEEN_MINUTES;
//        long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

        long triggerTime = SystemClock.elapsedRealtime()+30000;
        long repeatInterval = 30000;

//If the Toggle is turned on, set the repeating alarm with a 15 minute interval
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime, repeatInterval, notifyPendingIntent);

        boolean alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID, notificationIntent,
                PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmUp){
            Log.d(TAG, "setInexactRepeatingAlarm: Alarm tiap 30 detik nyala");
        }
    }

    private void updatenotification() {
        Intent notificationIntent = new Intent(this, TabActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap androidImage = BitmapFactory
                .decodeResource(getResources(),R.drawable.film);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Update Notification!")
                .setContentText("Favorite dihapus.")
                .setSmallIcon(R.drawable.ic_action_notif)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(androidImage)
                        .setBigContentTitle("Notification Updated!"))
                .setContentIntent(notificationPendingIntent);
        //harus v4
        Notification myNotification = notifyBuilder.build();
        mNotifyManager.notify(NOTIFICATION_ID, myNotification);
    }

    private void tampilinNotification() {

        //masuk aplikasi
        Intent notificationIntent = new Intent(this, TabActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //masuk web
        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW, Uri
                .parse("http://www.idn.id"));
        PendingIntent learnMorePendingIntent = PendingIntent.getActivity
                (this,NOTIFICATION_ID,learnMoreIntent,PendingIntent.FLAG_ONE_SHOT);

        //action
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("New Notification!")
                .setContentText("Favorite berhasil ditambah.")
                .setSmallIcon(R.drawable.ic_action_notif)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.ic_info_black_24dp,"Learn More", learnMorePendingIntent)
                .addAction(R.drawable.ic_sync_black_24dp, "Delete", updatePendingIntent)
                .setContentIntent(notificationPendingIntent);
        //harus v4
        Notification myNotification = notifyBuilder.build();
        mNotifyManager.notify(NOTIFICATION_ID, myNotification);


    }

    public class NotificationReceiver extends BroadcastReceiver {

        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_UPDATE_NOTIFICATION:
                    updatenotification();
                    hapusdaridatabase();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("FAVORITE"+dataJudul, false);
                    editor.commit();
                    btnPaporit.setChecked(false);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void hapusdaridatabase() {
        getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(dataId)).build(), null, null);

    }

    private void tambahkedatabase() {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_ID, dataId);
        cv.put(MovieContract.MovieEntry.COLUMN_JUDUL, dataJudul);
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER, dataPoster);
        Log.d(TAG, "onResponse: "+ cv.get(MovieContract.MovieEntry.COLUMN_POSTER));
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);
        Toast.makeText(DetailActivity.this, "Uri :" + uri, Toast.LENGTH_SHORT).show();

    }
}
