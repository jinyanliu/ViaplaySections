package se.sugarest.jane.viaplaysections;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import se.sugarest.jane.viaplaysections.data.type.SingleJSONResponse;

/**
 * Created by jane on 17-11-26.
 */
@Database(entities = {SingleJSONResponse.class}, version = 1)
public abstract class SectionProfileDatabase extends RoomDatabase {
    public abstract SectionProfileDao sectionProfileDao();
}
