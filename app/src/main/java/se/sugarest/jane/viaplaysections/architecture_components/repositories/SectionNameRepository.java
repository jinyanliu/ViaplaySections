package se.sugarest.jane.viaplaysections.architecture_components.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.sugarest.jane.viaplaysections.api.ViaplayClient;
import se.sugarest.jane.viaplaysections.data.datatype.JSONResponse;
import se.sugarest.jane.viaplaysections.data.datatype.ViaplaySection;

import static se.sugarest.jane.viaplaysections.util.Constants.VIAPLAY_BASE_URL;

/**
 * Created by jane on 17-11-27.
 */
@Singleton // Informs Dagger that this class should be constructed once
public class SectionNameRepository {

    private static final String LOG_TAG = SectionNameRepository.class.getSimpleName();

    private ViaplayClient client;

    public LiveData<List<ViaplaySection>> getSectionNames() {

        final MutableLiveData<List<ViaplaySection>> data = new MutableLiveData<>();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(VIAPLAY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();
        client = retrofit.create(ViaplayClient.class);
        Call<JSONResponse> call = client.getSections();

        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                if (response.body() != null && response.body().getLinks() != null
                        && response.body().getLinks().getViaplaySections() != null
                        && !response.body().getLinks().getViaplaySections().isEmpty()) {

                    data.setValue(response.body().getLinks().getViaplaySections());

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
        return data;
    }
}


