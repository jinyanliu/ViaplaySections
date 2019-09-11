package se.sugarest.jane.viaplaysections.ui.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import se.sugarest.jane.viaplaysections.data.database.SectionEntry
import se.sugarest.jane.viaplaysections.data.repository.SectionRepository

/**
 * A [ViewModel] class for [ListFragment]
 *
 *
 * Created by jane on 17-11-30.
 */
class ListFragmentViewModel(private val mRepository: SectionRepository) : ViewModel() {
    val sections: LiveData<List<SectionEntry>>

    init {
        sections = mRepository.sectionsList
    }
}
