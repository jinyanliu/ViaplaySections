package se.sugarest.jane.viaplaysections.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import se.sugarest.jane.viaplaysections.data.repository.SectionRepository;

/**
 * Factory class for {@link DetailFragmentViewModel}.
 * <p>
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
        return (T) new DetailFragmentViewModel(mRepository, mSectionName);
    }
}
