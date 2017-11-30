package se.sugarest.jane.viaplaysections.architecture_components.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

/**
 * Database for the application including a table for {@link SectionEntry} with the DAO {@link SectionDao}.
 * <p>
 * Created by jane on 17-11-29.
 */
@Database(entities = {SectionEntry.class}, version = 1)  // Entries listed here
public abstract class SectionDatabase extends RoomDatabase {

    private static final String LOG_TAG = SectionDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "section";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static SectionDatabase sInstance;

    public static SectionDatabase getsInstance(Context context) {
        Log.d(LOG_TAG, "Getting the database");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        SectionDatabase.class, SectionDatabase.DATABASE_NAME).build();
                Log.d(LOG_TAG, "Made new database");
            }
        }
        return sInstance;
    }

    // The associated DAOs for the database
    public abstract SectionDao sectionDao(); // Getters for Dao
}
