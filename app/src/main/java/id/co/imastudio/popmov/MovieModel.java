package id.co.imastudio.popmov;

/**
 * Created by idn on 8/6/2017.
 */

public class MovieModel {

    private String judul;
    private String poster;

    //generate setter and getter

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPoster() {
        return "https://image.tmdb.org/t/p/w500"+poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
