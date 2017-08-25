package id.co.imastudio.popmov.fragment;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import id.co.imastudio.popmov.MovieAdapter;
import id.co.imastudio.popmov.MovieModel;
import id.co.imastudio.popmov.R;
import id.co.imastudio.popmov.data.MovieContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public FavoriteFragment() {
        // Required empty public constructor
    }

    private static final int ID_FILM_LOADER = 100;
    private static final String TAG = "MainActivity";
    RecyclerView recycler;
    ArrayList<MovieModel> listMovie;
    MovieAdapter adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(ID_FILM_LOADER, null, this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler = (RecyclerView) view.findViewById(R.id.recycler_view);
        //dataset
        //1 Buat Model datanya
        //2 ArrayList MovieModel
        listMovie = new ArrayList<>();
        //3 Set data ke MovieModel

        //layoutmanager
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        initAdapter(listMovie);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_popular, container, false);


        return fragmentView;
    }


    private void initAdapter(ArrayList<MovieModel> movies) {
        adapter = new MovieAdapter(getActivity(), movies);
        recycler.setAdapter(adapter);
    }

    //Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case ID_FILM_LOADER:
                Uri filmUri = MovieContract.MovieEntry.CONTENT_URI;
                Log.d(TAG, "onCreateLoader: "+ filmUri.toString());
                return new CursorLoader(getActivity(), filmUri, null, null, null, null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        adapter.swapCursor(data);
        if(data.getCount()>0) {
            initAdapter(getMoviesFromCursor(data));
        } else {
            Toast.makeText(getActivity(), "Tidak Ada Favorite", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<MovieModel> getMoviesFromCursor(Cursor cursor) {
        ArrayList<MovieModel> movies = new ArrayList<>();

        if (cursor != null) {
            /*Log.e("cursor length","->"+cursor.getCount());
            Log.e("column length","->"+cursor.getColumnCount());*/
            if (cursor.moveToFirst()){
                do{
                    MovieModel movie = new MovieModel();
                    movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID)));
                    movie.setJudul(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_JUDUL)));
                    movie.setPoster(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)));
                    movies.add(movie);
                }while(cursor.moveToNext());
            }
        }
        return movies;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }




}
