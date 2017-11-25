package se.sugarest.jane.viaplaysections;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.FileObserver;
import android.support.annotation.Nullable;
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
 * Created by jane on 17-11-25.
 */

public class ViaplaySectionInformationViewModel extends ViewModel {

    private static final String LOG_TAG = ViaplaySectionInformationViewModel.class.getSimpleName();

    private String sectionName;

    public ViaplaySectionInformationViewModel(String sectionName) {
        this.sectionName = sectionName;
    }

    private final SectionInformationLiveData singleJSONLiveData
            = new SectionInformationLiveData();

    public LiveData<SingleJSONResponse> getSingleJSONResponse(String sectionName) {
        this.sectionName = sectionName;
        return singleJSONLiveData;
    }

    public class SectionInformationLiveData extends LiveData<SingleJSONResponse> {

        private final FileObserver fileObserver;

        public SectionInformationLiveData() {
            String path = VIAPLAY_BASE_URL;
            fileObserver = new FileObserver(path) {

                @Override
                public void onEvent(int i, @Nullable String s) {
                    // The path, the basic url for Viaplay API has changed, so let's reload the data
                    loadData(sectionName);
                }
            };
            loadData(sectionName);
        }

        @Override
        protected void onActive() {
            fileObserver.startWatching();
        }

        @Override
        protected void onInactive() {
            fileObserver.stopWatching();
        }

        private void loadData(String sectionName) {

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(VIAPLAY_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();
            ViaplayClient client = retrofit.create(ViaplayClient.class);
            Call<SingleJSONResponse> call = client.getOneSectionByTitle(sectionName);

            call.enqueue(new Callback<SingleJSONResponse>() {
                @Override
                public void onResponse(Call<SingleJSONResponse> call, Response<SingleJSONResponse> response) {

                    String currentLongTitle = response.body().getTitle();
                    String currentDescription = response.body().getDescription();

                    if (null != currentLongTitle && !currentLongTitle.isEmpty() && null != currentDescription
                            && !currentDescription.isEmpty()) {

                        setValue(response.body());

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
        }
    }
}
