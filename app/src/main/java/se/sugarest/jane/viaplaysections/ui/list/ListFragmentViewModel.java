package se.sugarest.jane.viaplaysections.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import se.sugarest.jane.viaplaysections.data.SectionRepository;
import se.sugarest.jane.viaplaysections.data.database.SectionEntry;

/**
 * {@link ViewModel} for {@link ListFragment}
 * <p>
 * Created by jane on 17-11-30.
 */
public class ListFragmentViewModel extends ViewModel {

    private final SectionRepository mRepository;
    private final LiveData<List<SectionEntry>> mSections;

    public ListFragmentViewModel(SectionRepository repository) {
        mRepository = repository;
        mSections = mRepository.getSectionsList();
    }

    public LiveData<List<SectionEntry>> getSections() {
        return mSections;
    }
}
