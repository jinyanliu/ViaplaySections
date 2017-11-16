package se.sugarest.jane.viaplaysections.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import se.sugarest.jane.viaplaysections.data.type.JSONResponse;
import se.sugarest.jane.viaplaysections.data.type.SingleJSONResponse;

import static se.sugarest.jane.viaplaysections.util.Constants.VIAPLAY_API_END_POINT;
import static se.sugarest.jane.viaplaysections.util.Constants.VIAPLAY_API_SLASH;

/**
 * This interface will be used with external lib Retrofit.
 * Reference: https://github.com/square/retrofit
 * <p>
 * Created by jane on 17-11-15.
 */
public interface ViaplayClient {

    // Get all the sections
    @GET(VIAPLAY_API_END_POINT)
    Call<JSONResponse> getSections();

    // Get detail information for one section
    @GET(VIAPLAY_API_END_POINT + VIAPLAY_API_SLASH + "{title}")
    Call<SingleJSONResponse> getOneSectionByTitle(@Path("title") String title);
}
