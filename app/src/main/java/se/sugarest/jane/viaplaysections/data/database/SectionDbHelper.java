package se.sugarest.jane.viaplaysections.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages a local database for section data
 * <p>
 * Created by jane on 17-11-15.
 */
public class SectionDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "section.db";

    private static final int DATABASE_VERSION = 1;

    public SectionDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_SECTION_TABLE =
                "CREATE TABLE " + SectionContract.SectionEntry.TABLE_NAME + " (" +
                        SectionContract.SectionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SectionContract.SectionEntry.COLUMN_SECTION_TITLE + " TEXT NOT NULL, " +
                        SectionContract.SectionEntry.COLUMN_SECTION_LONG_TITLE + " TEXT NOT NULL, " +
                        SectionContract.SectionEntry.COLUMN_SECTION_DESCRIPTION + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_SECTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
