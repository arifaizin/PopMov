package id.co.imastudio.popmov;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by idn on 8/6/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<MovieModel> listMovie;

    //generate constructor


    public MovieAdapter(Context context, ArrayList<MovieModel> listMovie) {
        this.context = context;
        this.listMovie = listMovie;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //menghubungkan dengan movie_item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //ngapain
        //set data
        holder.tvjudulMovie.setText(listMovie.get(position).getJudul());
//        holder.ivposterMovie.set
        //Glide untuk load gambar dari Internet
        Glide.with(context).load(listMovie.get(position).getPoster()).into(holder.ivposterMovie);
        //setOnClick
        holder.ivposterMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Kliked posisi "+ position, Toast.LENGTH_SHORT).show();
                // Intent ke DetailActivity
                Intent pindah = new Intent(context, DetailActivity.class);
                //kirim data
                pindah.putExtra("DATA_JUDUL", listMovie.get(position).getJudul());
                pindah.putExtra("DATA_POSTER", listMovie.get(position).getPoster());
                context.startActivity(pindah);
            }
        });

    }

    @Override
    public int getItemCount() {
        //jumlah list
        return listMovie.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //menyambungkan dengan komponen yg di dalam item
        ImageView ivposterMovie;
        TextView tvjudulMovie;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivposterMovie = (ImageView) itemView.findViewById(R.id.iv_item_poster);
            tvjudulMovie = (TextView) itemView.findViewById(R.id.tv_item_judul);

        }
    }
}
