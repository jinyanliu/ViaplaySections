package se.sugarest.jane.viaplaysections.api;

import retrofit2.Call;
import retrofit2.http.GET;
import se.sugarest.jane.viaplaysections.data.type.JSONResponse;

import static se.sugarest.jane.viaplaysections.util.Constants.VIAPLAY_API_END_POINT;

/**
 * Created by jane on 17-11-15.
 */

public interface ViaplayClient {
    @GET(VIAPLAY_API_END_POINT)
    Call<JSONResponse> getSections();
}
