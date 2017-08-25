package id.co.imastudio.popmov.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import id.co.imastudio.popmov.MovieAdapter;
import id.co.imastudio.popmov.MovieModel;
import id.co.imastudio.popmov.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularFragment extends Fragment {


    public PopularFragment() {
        // Required empty public constructor
    }

    private static final int ID_FILM_LOADER = 100;
    private static final String TAG = "MainActivity";
    RecyclerView recycler;
    ArrayList<MovieModel> listMovie;
    private String linkurl;
    MovieAdapter adapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_popular, container, false);


        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //dataset
        //1 Buat Model datanya
        //2 ArrayList MovieModel
        listMovie = new ArrayList<>();

//        getActivity().getSupportLoaderManager().initLoader(ID_FILM_LOADER, null, this);


        //layoutmanager
        recycler = (RecyclerView) view.findViewById(R.id.recycler_view);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        //adapter
        adapter = new MovieAdapter(getActivity(), listMovie);
        recycler.setAdapter(adapter);

        //dataOnline
        linkurl = "https://api.themoviedb.org/3/movie/popular?api_key=b08e3495841838f530552c2b261e00b1&language=en-US&page=1";
        getDataOnline(linkurl);

    }

    private void getDataOnline(String url) {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading", "Mohon Bersabar");

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
                        movie1.setId(json.getInt("id"));
                        movie1.setJudul(json.getString("title"));
                        movie1.setPoster(json.getString("poster_path"));
                        listMovie.add(movie1);

                        //insert data to sqlite
//                        ContentValues cv = new ContentValues();
//                        cv.put(MovieContract.MovieEntry.COLUMN_JUDUL, json.getString("title"));
//                        cv.put(MovieContract.MovieEntry.COLUMN_POSTER, json.getString("poster_path"));
//                        Log.d(TAG, "onResponse: "+ cv.get(MovieContract.MovieEntry.COLUMN_POSTER));
//                        Uri uri = getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);
//
//                        Toast.makeText(getActivity(), "Uri :" + uri, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error : " + error, Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue antrian = Volley.newRequestQueue(getActivity());
        antrian.add(ambilData);


    }

//    //Loader
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        switch (id){
//            case ID_FILM_LOADER:
//                Uri filmUri = MovieContract.MovieEntry.CONTENT_URI;
//                Log.d(TAG, "onCreateLoader: "+ filmUri.toString());
//                return new CursorLoader(getActivity(), filmUri, null, null, null, null);
//            default:
//                throw new RuntimeException("Loader Not Implemented: " + id);
//        }
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
////        adapter.swapCursor(data);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//
//    }

}
