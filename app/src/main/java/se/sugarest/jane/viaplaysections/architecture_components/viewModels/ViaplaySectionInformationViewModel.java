package se.sugarest.jane.viaplaysections.architecture_components.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import se.sugarest.jane.viaplaysections.architecture_components.repositories.SectionProfileRepository;
import se.sugarest.jane.viaplaysections.data.datatype.SingleJSONResponse;

/**
 * A SectionInformation ViewModel Class.
 * <p>
 * Created by jane on 17-11-25.
 */
public class ViaplaySectionInformationViewModel extends ViewModel {

    private LiveData<SingleJSONResponse> singleJSONResponseLiveData;
    private SectionProfileRepository sectionProfileRepository = new SectionProfileRepository();

    public void init(String sectionName) {
        singleJSONResponseLiveData = sectionProfileRepository.getSingleJSONResponse(sectionName);
    }

    public LiveData<SingleJSONResponse> getSingleJSONResponseLiveData() {
        return this.singleJSONResponseLiveData;
    }
}
