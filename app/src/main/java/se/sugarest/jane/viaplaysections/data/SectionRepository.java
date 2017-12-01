package se.sugarest.jane.viaplaysections.data;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import se.sugarest.jane.viaplaysections.AppExecutors;
import se.sugarest.jane.viaplaysections.data.database.SectionDao;
import se.sugarest.jane.viaplaysections.data.database.SectionEntry;
import se.sugarest.jane.viaplaysections.data.network.SectionNetworkDataSource;

/**
 * Handles data operations in SectionApp. Acts as a mediator between
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
    private boolean mInitialized = false;

    private SectionRepository(SectionDao sectionDao,
                              SectionNetworkDataSource sectionNetworkDataSource,
                              AppExecutors executors) {
        mSectionDao = sectionDao;
        mSectionNetworkDataSource = sectionNetworkDataSource;
        mExecutors = executors;

        getAndSaveSectionEntryList();
    }

    public void getAndSaveSingleSectionEntryDetails() {
        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        LiveData<SectionEntry> networkDataSectionInforamtion = mSectionNetworkDataSource
                .getCurrentSectionInformation();
        networkDataSectionInforamtion.observeForever(newSectionInfoFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                if (newSectionInfoFromNetwork != null) {
                    // Insert our new weather data into Sunshine's database
                    mSectionDao.updateSection(newSectionInfoFromNetwork.getTitle(), newSectionInfoFromNetwork.getDescription()
                            , newSectionInfoFromNetwork.getName().toLowerCase());
                    Log.d(LOG_TAG, "New values updated with the section name: " + newSectionInfoFromNetwork.getName()
                            + newSectionInfoFromNetwork.getTitle());
                }
            });
        });
    }

    public void getAndSaveSectionEntryList() {
        LiveData<List<SectionEntry>> networkDataSectionList = mSectionNetworkDataSource.getSectionList();
        networkDataSectionList.observeForever(newSectionListFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                mSectionDao.bulkInsert(newSectionListFromNetwork);
                Log.d(LOG_TAG, "New values inserted");
            });
        });
    }

    public synchronized static SectionRepository getInstance(
            SectionDao weatherDao, SectionNetworkDataSource sectionNetworkDataSource,
            AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new SectionRepository(weatherDao, sectionNetworkDataSource,
                        executors);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    private synchronized void initializeData() {

//        // Only perform initialization once per app lifetime. If initialization has already been
//        // performed, we have nothing to do in this method.
//        if (mInitialized) return;
//        mInitialized = true;

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
        initializeData();
        return mSectionDao.getSections();
    }

    public LiveData<SectionEntry> getSectionByName(String sectionName) {
        initializeDataBySectionName(sectionName);
        return mSectionDao.getSectionByName(sectionName);
    }

    /**
     * Network related operation
     */
    private void startFetchSectionByName(String sectionName) {
        mSectionNetworkDataSource.fetchSectionInformation(sectionName);
    }

    private void startFetchSectionList() {
        mSectionNetworkDataSource.fetchSectionList();
    }

}
