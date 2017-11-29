package se.sugarest.jane.viaplaysections.architecture_components.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import se.sugarest.jane.viaplaysections.architecture_components.repositories.SectionNameRepository;
import se.sugarest.jane.viaplaysections.data.datatype.ViaplaySection;

/**
 * A SectionNames ViewModel Class.
 * <p>
 * Created by jane on 17-11-25.
 */
public class SectionNameViewModel extends ViewModel {

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
