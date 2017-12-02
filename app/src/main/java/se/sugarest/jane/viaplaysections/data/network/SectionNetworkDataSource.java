package se.sugarest.jane.viaplaysections.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.sugarest.jane.viaplaysections.utilities.AppExecutors;
import se.sugarest.jane.viaplaysections.api.ViaplayClient;
import se.sugarest.jane.viaplaysections.data.database.SectionEntry;
import se.sugarest.jane.viaplaysections.data.datatype.JSONResponse;
import se.sugarest.jane.viaplaysections.data.datatype.SingleJSONResponse;
import se.sugarest.jane.viaplaysections.data.datatype.ViaplaySection;

import static java.util.concurrent.TimeUnit.MINUTES;
import static se.sugarest.jane.viaplaysections.utilities.Constants.VIAPLAY_BASE_URL;

/**
 * This class provides an API for doing all operations with the server data
 * <p>
 * Created by jane on 17-11-30.
 */
public class SectionNetworkDataSource {

    private static final String LOG_TAG = SectionNetworkDataSource.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static SectionNetworkDataSource sInstance;
    private final Context mContext;

    // LiveData storing the latest downloaded section data
    private final MutableLiveData<SectionEntry> mDownloadedSectionInformation;
    private final MutableLiveData<List<SectionEntry>> mDownloadedSectionList;
    private final AppExecutors mExecutors;

    private SectionNetworkDataSource(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mDownloadedSectionInformation = new MutableLiveData<>();
        mDownloadedSectionList = new MutableLiveData<>();
    }

    /**
     * Get the singleton for this class
     */
    public static SectionNetworkDataSource getInstance(Context context, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new SectionNetworkDataSource(context.getApplicationContext(), executors);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    public LiveData<SectionEntry> getCurrentSectionInformation() {
        return mDownloadedSectionInformation;
    }

    public LiveData<List<SectionEntry>> getSectionList() {
        return mDownloadedSectionList;
    }

    public void fetchSectionInformation(String sectionName) {
        Log.d(LOG_TAG, "Fetch section information started.");

        mExecutors.networkIO().execute(() -> {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.connectTimeout(1, MINUTES)
                    .writeTimeout(1, MINUTES)
                    .readTimeout(1, MINUTES);

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(VIAPLAY_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.client(httpClient.build()).build();

            ViaplayClient client = retrofit.create(ViaplayClient.class);

            Call<SingleJSONResponse> call = client.getOneSectionByTitle(sectionName);

            call.enqueue(new Callback<SingleJSONResponse>() {
                @Override
                public void onResponse
                        (Call<SingleJSONResponse> call, Response<SingleJSONResponse> response) {

                    if (response.body() != null && response.body().getTitle() != null && response.body().getDescription() != null) {

                        SectionEntry responseSectionEntry
                                = new SectionEntry(sectionName, response.body().getTitle(), response.body().getDescription());

                        mDownloadedSectionInformation.postValue(responseSectionEntry);

                    } else {
                        Log.e(LOG_TAG, "There is no SingleJSONResponse comes back from internet with this url: "
                                + response.raw().request().url().toString());
                    }
                }

                @Override
                public void onFailure(Call<SingleJSONResponse> call, Throwable t) {
                    Log.e(LOG_TAG, "Failed to get SingleJSONResponse back.", t);
                }
            });
        });
    }

    public void fetchSectionList() {

        Log.d(LOG_TAG, "Fetch section list started.");

        mExecutors.networkIO().execute(() -> {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.connectTimeout(1, MINUTES)
                    .writeTimeout(1, MINUTES)
                    .readTimeout(1, MINUTES);

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(VIAPLAY_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.client(httpClient.build()).build();

            ViaplayClient client = retrofit.create(ViaplayClient.class);

            Call<JSONResponse> call = client.getSections();

            call.enqueue(new Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                    if (response.body() != null && response.body().getLinks() != null
                            && response.body().getLinks().getViaplaySections() != null
                            && !response.body().getLinks().getViaplaySections().isEmpty()) {

                        List<ViaplaySection> viaplaySectionList = response.body().getLinks().getViaplaySections();
                        List<SectionEntry> sectionEntryList = new ArrayList<>();

                        for (int i = 0; i < viaplaySectionList.size(); i++) {
                            String currentSectionName = viaplaySectionList.get(i).getTitle().toLowerCase();
                            // For the first time, the title and description are not retrieved yet,
                            // so default to empty
                            SectionEntry currentSectionEntry
                                    = new SectionEntry(currentSectionName, "", "");
                            sectionEntryList.add(currentSectionEntry);
                        }

                        mDownloadedSectionList.postValue(sectionEntryList);

                    } else {
                        Log.e(LOG_TAG, "There is no ViaplaySections comes back from internet with this url: "
                                + response.raw().request().url().toString());
                    }
                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Log.e(LOG_TAG, "Failed to get ViaplaySections back.", t);
                }
            });
        });
    }
}
