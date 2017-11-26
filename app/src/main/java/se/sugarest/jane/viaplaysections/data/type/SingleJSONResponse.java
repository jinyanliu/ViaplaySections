
package se.sugarest.jane.viaplaysections.data.type;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class SingleJSONResponse {

    @PrimaryKey
    @NonNull
    private String sectionName;

    private String title;
    private String description;

    @NonNull
    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(@NonNull String sectionName) {
        this.sectionName = sectionName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
