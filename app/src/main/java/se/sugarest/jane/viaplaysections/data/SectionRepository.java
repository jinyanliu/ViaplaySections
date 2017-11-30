package se.sugarest.jane.viaplaysections.data;

import android.arch.lifecycle.LiveData;
import android.util.Log;

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

        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        LiveData<SectionEntry> networkData = mSectionNetworkDataSource.getCurrentSectionInformation();
        networkData.observeForever(newForecastsFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                // Insert our new weather data into Sunshine's database
                mSectionDao.insertSection(newForecastsFromNetwork);
                Log.d(LOG_TAG, "New values inserted");
            });
        });
    }

    public synchronized static SectionRepository getInstance(
            SectionDao weatherDao, SectionNetworkDataSource weatherNetworkDataSource,
            AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new SectionRepository(weatherDao, weatherNetworkDataSource,
                        executors);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    private synchronized void initializeData(String sectionName) {

        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if (mInitialized) return;
        mInitialized = true;

        mExecutors.diskIO().execute(() -> {
            startFetchSection(sectionName);
        });
    }

//    /**
//     * Database related operations
//     **/
//
//    public LiveData<List<ListWeatherEntry>> getCurrentWeatherForecasts() {
//        initializeData();
//        Date today = SunshineDateUtils.getNormalizedUtcDateForToday();
//        return mWeatherDao.getCurrentWeatherForecasts(today);
//    }

    public LiveData<SectionEntry> getSectionByName(String sectionName) {
        initializeData(sectionName);
        return mSectionDao.getSectionByName(sectionName);
    }

//    /**
//     * Deletes old weather data because we don't need to keep multiple days' data
//     */
//    private void deleteOldData() {
//        Date today = SunshineDateUtils.getNormalizedUtcDateForToday();
//        mWeatherDao.deleteOldWeather(today);
//    }

    /**
     * Network related operation
     */
    private void startFetchSection(String sectionName) {
        mSectionNetworkDataSource.fetchSectionInformation(sectionName);
    }

}
