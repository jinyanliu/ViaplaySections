package se.sugarest.jane.viaplaysections.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import static se.sugarest.jane.viaplaysections.utilities.Constants.DATABASE_NAME;

/**
 * This abstract class is the database for the application including a table for {@link SectionEntry}
 * with the DAO {@link SectionDao}.
 * <p>
 * Created by jane on 17-11-29.
 */
@Database(entities = {SectionEntry.class}, version = 1)
public abstract class SectionDatabase extends RoomDatabase {

    private static final String LOG_TAG = SectionDatabase.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static SectionDatabase sInstance;

    public static SectionDatabase getInstance(Context context) {
        Log.d(LOG_TAG, "Getting the database");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        SectionDatabase.class, DATABASE_NAME).build();
                Log.d(LOG_TAG, "Made new database");
            }
        }
        return sInstance;
    }

    // The associated DAO for the database
    public abstract SectionDao sectionDao(); // Getters for Dao
}
