package se.sugarest.jane.viaplaysections.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import se.sugarest.jane.viaplaysections.data.type.JSONResponse;
import se.sugarest.jane.viaplaysections.data.type.SingleJSONResponse;

import static se.sugarest.jane.viaplaysections.util.Constants.VIAPLAY_API_END_POINT;
import static se.sugarest.jane.viaplaysections.util.Constants.VIAPLAY_API_SLASH;

/**
 * Created by jane on 17-11-15.
 */

public interface ViaplayClient {
    @GET(VIAPLAY_API_END_POINT)
    Call<JSONResponse> getSections();

    @GET(VIAPLAY_API_END_POINT + VIAPLAY_API_SLASH + "{title}")
    Call<SingleJSONResponse> getOneSectionByTitle(@Path("title") String title);
}
