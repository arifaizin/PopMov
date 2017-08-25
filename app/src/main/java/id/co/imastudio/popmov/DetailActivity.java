package id.co.imastudio.popmov;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
                } else {
                    //hapus dari favorit
                    editor.putBoolean("FAVORITE"+dataJudul, false);
                    editor.commit();
                    hapusdaridatabase();
                }
            }
        });

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
