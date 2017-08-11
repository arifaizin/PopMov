package id.co.imastudio.popmov;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler;
    ArrayList<MovieModel> listMovie;
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
        for (int i=0; i< 20; i++){
            MovieModel movie1 = new MovieModel();
            movie1.setJudul("Secario");
            movie1.setPoster("http://4.bp.blogspot.com/-C2mOS0RSo4A/VpMM15jHevI/AAAAAAAAJ1I/w-fsNlOZvYc/s1600/poster%2Bfilm%2Bterbaik%2Bsicario%2B-%2Bnamafilm.jpg");
            listMovie.add(movie1);

            MovieModel movie2 = new MovieModel();
            movie2.setJudul("Aisyah");
            movie2.setPoster("http://cdn0-a.production.liputan6.static6.com/medias/1237323/big-portrait/079302900_1463568425-aisyah_4.jpg");
            listMovie.add(movie2);
        }

        //adapter
        MovieAdapter adapter = new MovieAdapter(MainActivity.this, listMovie);
        recycler.setAdapter(adapter);

        //layoutmanager
        recycler.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

    }
}
