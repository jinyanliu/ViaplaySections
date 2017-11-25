package se.sugarest.jane.viaplaysections.data.type;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.sugarest.jane.viaplaysections.api.ViaplayClient;

import static se.sugarest.jane.viaplaysections.util.Constants.VIAPLAY_BASE_URL;

/**
 * A ViewModel Class.
 * Created by jane on 17-11-25.
 */
public class ViaplaySectionNameViewModel extends ViewModel {

    private static final String LOG_TAG = ViaplaySectionNameViewModel.class.getSimpleName();

    private MutableLiveData<List<String>> sectionNames;

    public LiveData<List<String>> getSectionNames() {
        if (sectionNames == null) {
            sectionNames = new MutableLiveData<>();
            loadData();
        }
        return sectionNames;
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

                    List<String> sectionNamesStringList = new ArrayList<>();
                    for (int i = 0; i < viaplaySections.size(); i++) {
                        String currentTitle = viaplaySections.get(i).getTitle();
                        if (!sectionNamesStringList.contains(currentTitle)) {
                            sectionNamesStringList.add(currentTitle);
                        }
                    }

                    sectionNames.setValue(sectionNamesStringList);

                } else {
                    Log.e(LOG_TAG, "There is no sectionNames comes back from internet with this url: "
                            + response.raw().request().url().toString());
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.e(LOG_TAG, "Failed to get sectionNames list back.", t);
            }
        });
    }
}
