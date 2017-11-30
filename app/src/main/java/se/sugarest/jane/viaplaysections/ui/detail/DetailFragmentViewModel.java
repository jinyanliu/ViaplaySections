package se.sugarest.jane.viaplaysections.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import se.sugarest.jane.viaplaysections.data.SectionRepository;
import se.sugarest.jane.viaplaysections.data.database.SectionEntry;

/**
 * {@link ViewModel} for {@link DetailFragment}
 * <p>
 * Created by jane on 17-11-25.
 */
public class DetailFragmentViewModel extends ViewModel {

    // The Single Section the user is looking at
    private final LiveData<SectionEntry> mSection;

    // Section Name for the single section
    private final String mSectionName;
    private final SectionRepository mRepository;

    public DetailFragmentViewModel(SectionRepository sectionRepository, String sectionName) {
        mRepository = sectionRepository;
        mSectionName = sectionName;
        mSection = mRepository.getSectionByName(mSectionName);
    }

    public LiveData<SectionEntry> getSection() {
        return mSection;
    }
}
