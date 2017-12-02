package se.sugarest.jane.viaplaysections.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import se.sugarest.jane.viaplaysections.data.repository.SectionRepository;
import se.sugarest.jane.viaplaysections.data.database.SectionEntry;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link SectionRepository} and an sectionName for the current {@link SectionEntry}
 * Created by jane on 17-11-30.
 */
public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final SectionRepository mRepository;
    private final String mSectionName;

    public DetailViewModelFactory(SectionRepository sectionRepository, String sectionName) {
        this.mRepository = sectionRepository;
        this.mSectionName = sectionName;
    }

    public SectionRepository getRepository() {
        return mRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        // noinspection unchecked
        return (T) new DetailFragmentViewModel(mRepository,mSectionName);
    }
}
