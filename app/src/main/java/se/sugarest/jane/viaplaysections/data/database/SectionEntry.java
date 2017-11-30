package se.sugarest.jane.viaplaysections.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Room;

/**
 * Defines the schema of a table in {@link Room} for a single section information.
 * The data is used as an {@link Index} so that its uniqueness can be ensured.
 * Indexes also allow for fast lookup for the column.
 * <p>
 * Created by jane on 17-11-29.
 */
@Entity(tableName = "sections", indices = {@Index(value = {"name"}, unique = true)})
public class SectionEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String title;
    private String description;

    // Constructor used by converting json
    @Ignore
    public SectionEntry(String name, String title, String description) {
        this.name = name;
        this.title = title;
        this.description = description;
    }

    // Constructor used by Room to create SectionEntries
    public SectionEntry(int id, String name, String title, String description) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
