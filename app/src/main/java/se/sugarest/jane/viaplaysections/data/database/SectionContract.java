package se.sugarest.jane.viaplaysections.data.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This class defines tables and column names for the section database.
 * It contains SectionEntry.
 * <p>
 * Created by jane on 17-11-15.
 */
public class SectionContract {

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * Play Store.
     */
    public static final String CONTENT_AUTHORITY = "se.sugarest.jane.viaplaysections";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for Viaplaysections.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SECTION = "section";

    /**
     * Inner class that defines the table contents of the section table.
     */
    public static final class SectionEntry implements BaseColumns {

        /**
         * The base CONTENT_URI used to query the Section table from the content provider.
         */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SECTION)
                .build();

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of sections.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SECTION;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single section.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SECTION;

        /**
         * Used internally as the name of the section table.
         */
        public static final String TABLE_NAME = "section";

        public static final String COLUMN_SECTION_TITLE = "section_title";
        public static final String COLUMN_SECTION_LONG_TITLE = "section_long_title";
        public static final String COLUMN_SECTION_DESCRIPTION = "section_description";
    }
}
