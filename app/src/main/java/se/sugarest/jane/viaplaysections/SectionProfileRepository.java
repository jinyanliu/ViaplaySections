package se.sugarest.jane.viaplaysections;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.sugarest.jane.viaplaysections.api.ViaplayClient;
import se.sugarest.jane.viaplaysections.data.type.SingleJSONResponse;

import static se.sugarest.jane.viaplaysections.util.Constants.VIAPLAY_BASE_URL;

/**
 * Created by jane on 17-11-26.
 */

@Singleton // Informs Dagger that this class should be constructed once
public class SectionProfileRepository {

    private static final String LOG_TAG = SectionProfileRepository.class.getSimpleName();

    private ViaplayClient client;
    private SectionProfileDao sectionProfileDao;
    private Executor executor;

    @Inject
    public SectionProfileRepository(ViaplayClient client, SectionProfileDao sectionProfileDao, Executor executor) {
        this.client = client;
        this.sectionProfileDao = sectionProfileDao;
        this.executor = executor;
    }


    public LiveData<SingleJSONResponse> getSingleJSONResponse(String sectionName) {

        refreshSectionProfile(sectionName);
        // return a LiveData directly from the database.
        return sectionProfileDao.load(sectionName);
    }

    private void refreshSectionProfile(String sectionName) {
        executor.execute(() -> {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(VIAPLAY_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();
            client = retrofit.create(ViaplayClient.class);
            Call<SingleJSONResponse> call = client.getOneSectionByTitle(sectionName);

            call.enqueue(new Callback<SingleJSONResponse>()

            {
                @Override
                public void onResponse
                        (Call<SingleJSONResponse> call, Response<SingleJSONResponse> response) {

                    if (null != response) {

                        sectionProfileDao.save(response.body());
                        Log.i(LOG_TAG, "sectionProfileDao is saving " + sectionName + "\'s information.");

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
}
