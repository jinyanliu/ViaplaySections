package se.sugarest.jane.viaplaysections.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * This interface provides an api for all database operations
 * <p>
 * Created by jane on 17-11-29.
 */
@Dao // Required annotation for Dao to be recognized by Room
public interface SectionDao {

    /**
     * Inserts a list of {@link SectionEntry} into the sections table. If there is a conflicting
     * name, the section entry uses the {@link OnConflictStrategy} of replacing the section
     * information. The required uniqueness of these values is defined in the {@link SectionEntry}.
     *
     * @param sectionEntries A list of section information to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(List<SectionEntry> sectionEntries);

    /**
     * Updates one section identified by a specific name
     *
     * @param title       The title value to update
     * @param description The description value to update
     * @param name        The name of the matching section to update
     */
    @Query("UPDATE section SET title = :title, description = :description WHERE name = :name")
    void updateSectionByName(String title, String description, String name);

    /**
     * Gets all the sections
     *
     * @return a list of all the sections
     */
    @Query("SELECT * FROM section")
    LiveData<List<SectionEntry>> getSections();

    /**
     * Gets one section for a specific name
     *
     * @param name The name of the section to query
     * @return section for a specific name
     */
    @Query("SELECT * FROM section WHERE name = :name")
    LiveData<SectionEntry> getSectionByName(String name);
}

