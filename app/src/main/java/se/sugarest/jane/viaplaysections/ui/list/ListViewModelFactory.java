package se.sugarest.jane.viaplaysections.ui.list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import se.sugarest.jane.viaplaysections.data.SectionRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link SectionRepository}
 * <p>
 * Created by jane on 17-11-30.
 */
public class ListViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final SectionRepository mRepository;

    public ListViewModelFactory(SectionRepository repository){
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ListFragmentViewModel(mRepository);
    }
}
