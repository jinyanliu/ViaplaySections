package se.sugarest.jane.viaplaysections.utilities

import android.content.Context
import se.sugarest.jane.viaplaysections.data.database.SectionDatabase
import se.sugarest.jane.viaplaysections.data.network.SectionNetworkDataSource
import se.sugarest.jane.viaplaysections.data.repository.SectionRepository
import se.sugarest.jane.viaplaysections.ui.detail.DetailViewModelFactory
import se.sugarest.jane.viaplaysections.ui.list.ListViewModelFactory

/**
 * This class provides static methods to inject the various classes needed for the app
 *
 *
 * Created by jane on 17-11-30.
 */
object InjectorUtils {

    fun provideRepository(context: Context): SectionRepository {
        val database = SectionDatabase.getInstance(context.applicationContext)
        val executors = AppExecutors.getInstance()
        val networkDataSource = SectionNetworkDataSource.getInstance(executors)
        return SectionRepository.getInstance(database.sectionDao(), networkDataSource, executors)
    }

    fun provideDetailFragmentModelFactory(context: Context, sectionName: String): DetailViewModelFactory {
        val repository = provideRepository(context.applicationContext)
        return DetailViewModelFactory(repository, sectionName)
    }

    fun provideListFragmentViewModelFactory(context: Context): ListViewModelFactory {
        val repository = provideRepository(context.applicationContext)
        return ListViewModelFactory(repository)
    }
}
