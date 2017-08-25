package id.co.imastudio.popmov.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by idn on 8/19/2017.
 */

public class MovieContract {
    public static final String AUTHORITY = "id.co.imastudio.popmov";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = "listfilm";

    public static final class MovieEntry implements BaseColumns {

        //Untuk Uri
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        //untuk sqlite
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_JUDUL = "title";
        public static final String COLUMN_POSTER = "poster_path";

    }
}
