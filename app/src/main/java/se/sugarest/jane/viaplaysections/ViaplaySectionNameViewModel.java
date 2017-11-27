package se.sugarest.jane.viaplaysections;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import se.sugarest.jane.viaplaysections.data.type.ViaplaySection;

/**
 * A ViewModel Class.
 * Created by jane on 17-11-25.
 */
public class ViaplaySectionNameViewModel extends ViewModel {

    private LiveData<List<ViaplaySection>> sectionNames;
    private SectionNameRepository sectionNameRepository = new SectionNameRepository();

    public void init() {
        if (this.sectionNames != null) {
            return;
        }
        sectionNames = sectionNameRepository.getSectionNames();
    }

    public LiveData<List<ViaplaySection>> getSectionNames() {
        return this.sectionNames;
    }
}
