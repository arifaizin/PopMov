package id.co.imastudio.popmov;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    ImageView ivDetailPoster;
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //terima data
        String dataJudul = getIntent().getStringExtra("DATA_JUDUL");
        String dataPoster = getIntent().getStringExtra("DATA_POSTER");

        Log.d("DetailActivity",dataJudul+dataPoster);

        getSupportActionBar().setTitle(dataJudul);

        ivDetailPoster = (ImageView) findViewById(R.id.iv_detail_poster);
        Glide.with(DetailActivity.this).load(dataPoster).into(ivDetailPoster);

    }
}
