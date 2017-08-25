package id.co.imastudio.popmov;

/**
 * Created by idn on 8/6/2017.
 */

public class MovieModel {



    private int id;
    private String judul;
    private String poster;

    //generate setter and getter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
