package se.sugarest.jane.viaplaysections.data.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static se.sugarest.jane.viaplaysections.util.Constants.SECTIONS;
import static se.sugarest.jane.viaplaysections.util.Constants.SECTIONS_ID;

/**
 * The {@link ContentProvider} for Section data.
 * Created by jane on 17-11-15.
 */
public class SectionProvider extends ContentProvider {

    private static final String LOG_TAG = SectionProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(SectionContract.CONTENT_AUTHORITY, SectionContract.PATH_SECTION, SECTIONS);
        sUriMatcher.addURI(SectionContract.CONTENT_AUTHORITY, SectionContract.PATH_SECTION + "/#", SECTIONS_ID);
    }

    private SectionDbHelper mSectionDbHelper;

    @Override
    public boolean onCreate() {
        mSectionDbHelper = new SectionDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mSectionDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case SECTIONS:
                cursor = database.query(
                        SectionContract.SectionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case SECTIONS_ID:
                selection = SectionContract.SectionEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(SectionContract.SectionEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SECTIONS:
                return SectionContract.SectionEntry.CONTENT_LIST_TYPE;
            case SECTIONS_ID:
                return SectionContract.SectionEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database = mSectionDbHelper.getWritableDatabase();
        long id;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case SECTIONS:
                id = database.insert(SectionContract.SectionEntry.TABLE_NAME, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Cannot insert unknown URI " + uri);
        }
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted;
        SQLiteDatabase database = mSectionDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SECTIONS:
                rowsDeleted = database.delete(SectionContract.SectionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SECTIONS_ID:
                selection = SectionContract.SectionEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(SectionContract.SectionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues
            , @Nullable String selection, @Nullable String[] selectionArgs) {
        if (contentValues.size() == 0) {
            return 0;
        }
        int rowsUpdated;
        SQLiteDatabase database = mSectionDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SECTIONS:
                selection = selection + "=?";
                rowsUpdated = database.update(SectionContract.SectionEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase database = mSectionDbHelper.getWritableDatabase();
        int rowsInserted = 0;
        switch (sUriMatcher.match(uri)) {
            case SECTIONS:
                database.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long id = database.insert(SectionContract.SectionEntry.TABLE_NAME, null,
                                value);
                        if (id != -1) {
                            rowsInserted++;
                        }
                    }
                    database.setTransactionSuccessful();
                } finally {
                    database.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
