package se.sugarest.jane.viaplaysections;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import se.sugarest.jane.viaplaysections.data.type.SingleJSONResponse;

/**
 * Created by jane on 17-11-25.
 */
public class ViaplaySectionInformationViewModel extends ViewModel {

    private LiveData<SingleJSONResponse> singleJSONResponseLiveData;
    private SectionProfileRepository sectionProfileRepository = new SectionProfileRepository();

    public void init(String sectionName) {
        if (this.singleJSONResponseLiveData != null) {
            return;
        }
        singleJSONResponseLiveData = sectionProfileRepository.getSingleJSONResponse(sectionName);
    }

    public LiveData<SingleJSONResponse> getSingleJSONResponseLiveData() {
        return this.singleJSONResponseLiveData;
    }
}
