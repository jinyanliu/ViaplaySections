package se.sugarest.jane.viaplaysections;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.FileObserver;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.sugarest.jane.viaplaysections.api.ViaplayClient;
import se.sugarest.jane.viaplaysections.data.type.JSONResponse;
import se.sugarest.jane.viaplaysections.data.type.ViaplaySection;

import static se.sugarest.jane.viaplaysections.util.Constants.VIAPLAY_BASE_URL;

/**
 * A ViewModel Class.
 * Created by jane on 17-11-25.
 */
public class ViaplaySectionNameViewModel extends ViewModel {

    private static final String LOG_TAG = ViaplaySectionNameViewModel.class.getSimpleName();

    private final SectionNamesLiveData sectionNamesLiveData = new SectionNamesLiveData();

    public LiveData<List<ViaplaySection>> getSectionNames() {
        return sectionNamesLiveData;
    }

    public class SectionNamesLiveData extends LiveData<List<ViaplaySection>> {

        private final FileObserver fileObserver;

        public SectionNamesLiveData() {
            String path = VIAPLAY_BASE_URL;
            fileObserver = new FileObserver(path) {
                @Override
                public void onEvent(int i, @Nullable String s) {
                    // The path, the basic url for Viaplay API has changed, so let's reload the data
                    loadData();
                }
            };
            loadData();
        }

        @Override
        protected void onActive() {
            fileObserver.startWatching();
        }

        @Override
        protected void onInactive() {
            fileObserver.stopWatching();
        }

        private void loadData() {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(VIAPLAY_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.client(httpClient.build()).build();
            ViaplayClient client = retrofit.create(ViaplayClient.class);
            Call<JSONResponse> call = client.getSections();

            call.enqueue(new Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                    List<ViaplaySection> viaplaySections = response.body().getLinks().getViaplaySections();

                    if (viaplaySections != null && !viaplaySections.isEmpty()) {

                        setValue(viaplaySections);

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
        }

    }
}
