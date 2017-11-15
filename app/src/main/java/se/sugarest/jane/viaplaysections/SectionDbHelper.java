package se.sugarest.jane.viaplaysections;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import se.sugarest.jane.viaplaysections.SectionContract.SectionEntry;

/**
 * Created by jane on 17-11-15.
 */

public class SectionDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "section.db";

    private static final int DATABASE_VERSION = 1;

    public SectionDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_SECTION_TABLE =
                "CREATE TABLE " + SectionEntry.TABLE_NAME + " (" +
                        SectionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SectionEntry.COLUMN_SECTION_TITLE + " TEXT NOT NULL, " +
                        SectionEntry.COLUMN_HREF_URL + " TEXT NOT NULL, " +
                        SectionEntry.COLUMN_SECTION_LONG_TITLE + " TEXT NOT NULL, " +
                        SectionEntry.COLUMN_SECTION_DESCRIPTION + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_SECTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
