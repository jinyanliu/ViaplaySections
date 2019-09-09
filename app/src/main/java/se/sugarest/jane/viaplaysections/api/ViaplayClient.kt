package se.sugarest.jane.viaplaysections.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import se.sugarest.jane.viaplaysections.data.datatype.JSONResponse
import se.sugarest.jane.viaplaysections.data.datatype.SingleJSONResponse

import se.sugarest.jane.viaplaysections.utilities.Constants.VIAPLAY_API_END_POINT
import se.sugarest.jane.viaplaysections.utilities.Constants.VIAPLAY_API_SLASH

/**
 * This interface will be used with external lib Retrofit.
 * Reference: https://github.com/square/retrofit
 *
 *
 * Created by jane on 17-11-15.
 */
interface ViaplayClient {

    // Get all the sections
    @get:GET(VIAPLAY_API_END_POINT)
    val sections: Call<JSONResponse>

    // Get detail information for one section
    @GET("$VIAPLAY_API_END_POINT$VIAPLAY_API_SLASH{title}")
    fun getOneSectionByTitle(@Path("title") title: String): Call<SingleJSONResponse>
}
