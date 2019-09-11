package se.sugarest.jane.viaplaysections.data.repository

import android.arch.lifecycle.LiveData
import android.util.Log
import se.sugarest.jane.viaplaysections.data.database.SectionDao
import se.sugarest.jane.viaplaysections.data.database.SectionEntry
import se.sugarest.jane.viaplaysections.data.network.SectionNetworkDataSource
import se.sugarest.jane.viaplaysections.utilities.AppExecutors

/**
 * This class handles data operations in SectionApp. Acts as a mediator between [SectionNetworkDataSource]
 * and [SectionDao]
 *
 *
 * Created by jane on 17-11-30.
 */
class SectionRepository private constructor(private val mSectionDao: SectionDao,
                                            private val mSectionNetworkDataSource: SectionNetworkDataSource,
                                            private val mExecutors: AppExecutors) {

    /**
     * Database related operations
     */
    val sectionsList: LiveData<List<SectionEntry>>
        get() {
            initializeSectionListData()
            return mSectionDao.sections
        }

    init {

        getAndSaveSectionEntryList()

        getAndUpdateSingleSectionEntryDetails()
    }

    private fun getAndSaveSectionEntryList() {
        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        val networkDataSectionList = mSectionNetworkDataSource.sectionList
        networkDataSectionList.observeForever { newSectionListFromNetwork ->
            mExecutors.diskIO().execute {
                // BulkInsert new section list data into SectionDatabase
                mSectionDao.bulkInsert(newSectionListFromNetwork)
                Log.d(LOG_TAG, "New values inserted")
            }
        }
    }

    private fun getAndUpdateSingleSectionEntryDetails() {
        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        val networkDataSectionInformation = mSectionNetworkDataSource
                .currentSectionInformation
        networkDataSectionInformation.observeForever { newSectionInfoFromNetwork ->
            mExecutors.diskIO().execute {
                if (newSectionInfoFromNetwork != null) {
                    // Update new single section detail data into SectionDatabase
                    mSectionDao.updateSectionByName(newSectionInfoFromNetwork.title, newSectionInfoFromNetwork.description, newSectionInfoFromNetwork.name.toLowerCase())
                    Log.d(LOG_TAG, "New values updated with the section name="
                            + newSectionInfoFromNetwork.name + " and the section title="
                            + newSectionInfoFromNetwork.title)
                }
            }
        }
    }

    @Synchronized
    private fun initializeSectionListData() {
        mExecutors.diskIO().execute { startFetchSectionList() }
    }

    @Synchronized
    private fun initializeDataBySectionName(sectionName: String) {
        mExecutors.diskIO().execute { startFetchSectionByName(sectionName) }
    }

    fun getSectionByName(sectionName: String): LiveData<SectionEntry> {
        initializeDataBySectionName(sectionName)
        return mSectionDao.getSectionByName(sectionName)
    }

    /**
     * Network related operation
     */
    private fun startFetchSectionList() {
        mSectionNetworkDataSource.fetchSectionList()
    }

    fun startFetchSectionByName(sectionName: String) {
        mSectionNetworkDataSource.fetchSectionInformation(sectionName)
    }

    companion object {

        private val LOG_TAG = SectionRepository::class.java.simpleName

        // For Singleton instantiation
        private val LOCK = Any()
        private var sInstance: SectionRepository? = null

        @Synchronized
        fun getInstance(
                sectionDao: SectionDao, sectionNetworkDataSource: SectionNetworkDataSource,
                executors: AppExecutors): SectionRepository {
            Log.d(LOG_TAG, "Getting the repository")
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = SectionRepository(sectionDao, sectionNetworkDataSource,
                            executors)
                    Log.d(LOG_TAG, "Made new repository")
                }
            }
            return sInstance as SectionRepository
        }
    }
}
