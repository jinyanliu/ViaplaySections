package se.sugarest.jane.viaplaysections;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

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

public class SectionProfileRepository {

    private static final String LOG_TAG = SectionProfileRepository.class.getSimpleName();

    private ViaplayClient client;

    public LiveData<SingleJSONResponse> getSingleJSONResponse(String sectionName) {

        final MutableLiveData<SingleJSONResponse> data = new MutableLiveData<>();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(VIAPLAY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        client = retrofit.create(ViaplayClient.class);
        Call<SingleJSONResponse> call = client.getOneSectionByTitle(sectionName);

        call.enqueue(new Callback<SingleJSONResponse>() {
            @Override
            public void onResponse(Call<SingleJSONResponse> call, Response<SingleJSONResponse> response) {

                String currentLongTitle = response.body().getTitle();
                String currentDescription = response.body().getDescription();

                if (null != currentLongTitle && !currentLongTitle.isEmpty() && null != currentDescription
                        && !currentDescription.isEmpty()) {

                    data.setValue(response.body());

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

        return data;
    }
}
