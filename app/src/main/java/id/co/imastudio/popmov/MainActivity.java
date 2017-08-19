package id.co.imastudio.popmov;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.imastudio.popmov.data.MovieContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_FILM_LOADER = 100;
    private static final String TAG = "MainActivity";
    RecyclerView recycler;
    ArrayList<MovieModel> listMovie;
    String pilihanFilm = "popular";
    private String linkurl;
    MovieAdapter adapter;

//    MovieDbHelper dbHelper = new MovieDbHelper(this);
//    SQLiteDatabase mDb = dbHelper.getWritableDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = (RecyclerView) findViewById(R.id.recycler_view);
        //dataset
        //1 Buat Model datanya
        //2 ArrayList MovieModel
        listMovie = new ArrayList<>();
        //3 Set data ke MovieModel

        //datadummy
//        for (int i=0; i< 20; i++){
//            MovieModel movie1 = new MovieModel();
//            movie1.setJudul("Secario");
//            movie1.setPoster("http://4.bp.blogspot.com/-C2mOS0RSo4A/VpMM15jHevI/AAAAAAAAJ1I/w-fsNlOZvYc/s1600/poster%2Bfilm%2Bterbaik%2Bsicario%2B-%2Bnamafilm.jpg");
//            listMovie.add(movie1);
//
//            MovieModel movie2 = new MovieModel();
//            movie2.setJudul("Aisyah");
//            movie2.setPoster("http://cdn0-a.production.liputan6.static6.com/medias/1237323/big-portrait/079302900_1463568425-aisyah_4.jpg");
//            listMovie.add(movie2);
//        }

//        Boolean favorit = pref.getBoolean("FAVORITE"+dataJudul, false);
//        if (favorit){
//            btnPaporit.setChecked(true);
//        }
        //dataOnline
        linkurl = "https://api.themoviedb.org/3/movie/popular?api_key=b08e3495841838f530552c2b261e00b1&language=en-US&page=1";

//        if (isNetworkConnected()) {
            getDataOnline(linkurl);
            getSupportLoaderManager().initLoader(ID_FILM_LOADER, null, MainActivity.this);

//        } else {
//            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
//            getSupportLoaderManager().initLoader(ID_FILM_LOADER, null, MainActivity.this);
//        }



//        public static boolean CheckIsDataAlreadyInDBorNot(String TableName,
//                String dbfield, String fieldValue) {
//            SQLiteDatabase sqldb = EGLifeStyleApplication.sqLiteDatabase;
//            String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;
//            Cursor cursor = sqldb.rawQuery(Query, null);
//            if(cursor.getCount() <= 0){
//                cursor.close();
//                return false;
//            }
//            cursor.close();
//            return true;
//        }

        //adapter
        adapter = new MovieAdapter(MainActivity.this, listMovie);
        recycler.setAdapter(adapter);

        //layoutmanager
        recycler.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));



    }

    private void getDataOnline(String url) {
        final ProgressDialog loading = ProgressDialog.show(MainActivity.this, "Loading", "Mohon Bersabar");
//        String url = "https://api.themoviedb.org/3/movie/" + pilihanFilm + "?api_key=b08e3495841838f530552c2b261e00b1&language=en-US&page=1";

        JsonObjectRequest ambilData = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.hide();
                try {
                    JSONArray arrayresults = response.getJSONArray("results");
                    for (int i = 0; i < arrayresults.length(); i++) {
                        JSONObject json = arrayresults.getJSONObject(i);
                        Log.d("MainActivity", "Hasil json : " + json);

                        MovieModel movie1 = new MovieModel();
                        movie1.setJudul(json.getString("title"));
                        movie1.setPoster(json.getString("poster_path"));
                        listMovie.add(movie1);

                        //insert data to sqlite
                        ContentValues cv = new ContentValues();
                        cv.put(MovieContract.MovieEntry.COLUMN_JUDUL, json.getString("title"));
                        cv.put(MovieContract.MovieEntry.COLUMN_POSTER, json.getString("poster_path"));
                        Log.d(TAG, "onResponse: "+ cv.get(MovieContract.MovieEntry.COLUMN_POSTER));
                        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);

                        Toast.makeText(MainActivity.this, "Uri :" + uri, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error : " + error, Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue antrian = Volley.newRequestQueue(this);
        antrian.add(ambilData);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MainActivity", "Masuk onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity", "Masuk onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MainActivity", "Masuk onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MainActivity", "Masuk onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity", "Masuk onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case ID_FILM_LOADER:
                Uri filmUri = MovieContract.MovieEntry.CONTENT_URI;
                Log.d(TAG, "onCreateLoader: "+ filmUri.toString());
                return new CursorLoader(this, filmUri, null, null, null, null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

//
private boolean isNetworkConnected() {
    ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    return networkInfo != null && networkInfo.isConnected();
}
}
