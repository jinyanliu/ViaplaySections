package se.sugarest.jane.viaplaysections;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import se.sugarest.jane.viaplaysections.data.type.SingleJSONResponse;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by jane on 17-11-26.
 */
@Dao
public interface SectionProfileDao {

    @Insert(onConflict = REPLACE)
    void save(SingleJSONResponse singleJSONResponse);

    @Query("SELECT * FROM singlejsonresponse WHERE sectionName = :sectionName")
    LiveData<SingleJSONResponse> load(String sectionName);

}
