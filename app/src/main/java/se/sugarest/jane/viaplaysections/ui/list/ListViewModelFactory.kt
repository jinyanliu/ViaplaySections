package se.sugarest.jane.viaplaysections.ui.list

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import se.sugarest.jane.viaplaysections.data.repository.SectionRepository

/**
 * Factory class for [ListFragmentViewModel].
 *
 *
 * Created by jane on 17-11-30.
 */
class ListViewModelFactory(private val mRepository: SectionRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListFragmentViewModel(mRepository) as T
    }
}
