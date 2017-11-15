package se.sugarest.jane.viaplaysections;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jane on 17-11-15.
 */

public class SectionContract {

    public static final String CONTENT_AUTHORITY = "se.sugarest.jane.viaplaysections";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SECTION = "section";

    public static final class SectionEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SECTION)
                .build();

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SECTION;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SECTION;

        public static final String TABLE_NAME = "section";

        public static final String COLUMN_SECTION_TITLE = "section_title";

        public static final String COLUMN_HREF_URL = "href_url";

        public static final String COLUMN_SECTION_LONG_TITLE = "section_long_title";

        public static final String COLUMN_SECTION_DESCRIPTION = "section_description";
    }
}
