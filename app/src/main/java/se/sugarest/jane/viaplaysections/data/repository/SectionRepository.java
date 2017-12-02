package se.sugarest.jane.viaplaysections.data.repository;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import se.sugarest.jane.viaplaysections.utilities.AppExecutors;
import se.sugarest.jane.viaplaysections.data.database.SectionDao;
import se.sugarest.jane.viaplaysections.data.database.SectionEntry;
import se.sugarest.jane.viaplaysections.data.network.SectionNetworkDataSource;

/**
 * This class handles data operations in SectionApp. Acts as a mediator between {@link SectionNetworkDataSource}
 * and {@link SectionDao}
 * <p>
 * Created by jane on 17-11-30.
 */
public class SectionRepository {

    private static final String LOG_TAG = SectionRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static SectionRepository sInstance;
    private final SectionDao mSectionDao;
    private final SectionNetworkDataSource mSectionNetworkDataSource;
    private final AppExecutors mExecutors;

    private SectionRepository(SectionDao sectionDao,
                              SectionNetworkDataSource sectionNetworkDataSource,
                              AppExecutors executors) {
        mSectionDao = sectionDao;
        mSectionNetworkDataSource = sectionNetworkDataSource;
        mExecutors = executors;

        getAndSaveSectionEntryList();

        getAndUpdateSingleSectionEntryDetails();
    }

    private void getAndSaveSectionEntryList() {
        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        LiveData<List<SectionEntry>> networkDataSectionList = mSectionNetworkDataSource.getSectionList();
        networkDataSectionList.observeForever(newSectionListFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                // BulkInsert new section list data into SectionDatabase
                mSectionDao.bulkInsert(newSectionListFromNetwork);
                Log.d(LOG_TAG, "New values inserted");
            });
        });
    }

    private void getAndUpdateSingleSectionEntryDetails() {
        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        LiveData<SectionEntry> networkDataSectionInforamtion = mSectionNetworkDataSource
                .getCurrentSectionInformation();
        networkDataSectionInforamtion.observeForever(newSectionInfoFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                if (newSectionInfoFromNetwork != null) {
                    // Update new single section detail data into SectionDatabase
                    mSectionDao.updateSection(newSectionInfoFromNetwork.getTitle(), newSectionInfoFromNetwork.getDescription()
                            , newSectionInfoFromNetwork.getName().toLowerCase());
                    Log.d(LOG_TAG, "New values updated with the section name: "
                            + newSectionInfoFromNetwork.getName() + " and the section title: "
                            + newSectionInfoFromNetwork.getTitle());
                }
            });
        });
    }

    public synchronized static SectionRepository getInstance(
            SectionDao sectionDao, SectionNetworkDataSource sectionNetworkDataSource,
            AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new SectionRepository(sectionDao, sectionNetworkDataSource,
                        executors);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    private synchronized void initializeSectionListData() {
        mExecutors.diskIO().execute(() -> {
            startFetchSectionList();
        });
    }

    private synchronized void initializeDataBySectionName(String sectionName) {
        mExecutors.diskIO().execute(() -> {
            startFetchSectionByName(sectionName);
        });
    }

    /**
     * Database related operations
     **/
    public LiveData<List<SectionEntry>> getSectionsList() {
        initializeSectionListData();
        return mSectionDao.getSections();
    }

    public LiveData<SectionEntry> getSectionByName(String sectionName) {
        initializeDataBySectionName(sectionName);
        return mSectionDao.getSectionByName(sectionName);
    }

    /**
     * Network related operation
     */
    private void startFetchSectionList() {
        mSectionNetworkDataSource.fetchSectionList();
    }

    public void startFetchSectionByName(String sectionName) {
        mSectionNetworkDataSource.fetchSectionInformation(sectionName);
    }
}
