package se.sugarest.jane.viaplaysections.data.network

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import se.sugarest.jane.viaplaysections.api.ViaplayClient
import se.sugarest.jane.viaplaysections.data.database.SectionEntry
import se.sugarest.jane.viaplaysections.data.datatype.JSONResponse
import se.sugarest.jane.viaplaysections.data.datatype.SingleJSONResponse
import se.sugarest.jane.viaplaysections.utilities.AppExecutors
import se.sugarest.jane.viaplaysections.utilities.Constants.VIAPLAY_BASE_URL
import java.util.*
import java.util.concurrent.TimeUnit.MINUTES

/**
 * This class provides an API for doing all operations with the server data
 *
 *
 * Created by jane on 17-11-30.
 */
class SectionNetworkDataSource private constructor(private val mExecutors: AppExecutors) {

    // LiveData storing the latest downloaded section data
    private val mDownloadedSectionInformation = MutableLiveData<SectionEntry>()
    private val mDownloadedSectionList = MutableLiveData<List<SectionEntry>>()

    val currentSectionInformation: LiveData<SectionEntry>
        get() = mDownloadedSectionInformation

    val sectionList: LiveData<List<SectionEntry>>
        get() = mDownloadedSectionList

    fun fetchSectionInformation(sectionName: String) {

        Log.d(LOG_TAG, "Fetch section information started for section=$sectionName")

        mExecutors.networkIO().execute {

            val httpClient = OkHttpClient.Builder()

            httpClient.connectTimeout(1, MINUTES)
                    .writeTimeout(1, MINUTES)
                    .readTimeout(1, MINUTES)

            val builder = Retrofit.Builder()
                    .baseUrl(VIAPLAY_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())

            val retrofit = builder.client(httpClient.build()).build()

            val client = retrofit.create(ViaplayClient::class.java)

            val call = client.getOneSectionByTitle(sectionName)

            call.enqueue(object : Callback<SingleJSONResponse> {
                override fun onResponse(call: Call<SingleJSONResponse>, response: Response<SingleJSONResponse>) {

                    if (response.body() != null && response.body()!!.title != null && response.body()!!.description != null) {

                        val responseSectionEntry = SectionEntry(sectionName, response.body()!!.title, response.body()!!.description)

                        mDownloadedSectionInformation.postValue(responseSectionEntry)

                    } else {
                        Log.e(LOG_TAG, "There is no SingleJSONResponse comes back from internet with this url=" + response.raw().request().url().toString())
                    }
                }

                override fun onFailure(call: Call<SingleJSONResponse>, t: Throwable) {
                    Log.e(LOG_TAG, "Failed to get SingleJSONResponse back for section=$sectionName", t)
                }
            })
        }
    }

    fun fetchSectionList() {

        Log.d(LOG_TAG, "Fetch section list started.")

        mExecutors.networkIO().execute {

            val httpClient = OkHttpClient.Builder()

            httpClient.connectTimeout(1, MINUTES)
                    .writeTimeout(1, MINUTES)
                    .readTimeout(1, MINUTES)

            val builder = Retrofit.Builder()
                    .baseUrl(VIAPLAY_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())

            val retrofit = builder.client(httpClient.build()).build()

            val client = retrofit.create(ViaplayClient::class.java)

            val call = client.sections

            call.enqueue(object : Callback<JSONResponse> {
                override fun onResponse(call: Call<JSONResponse>, response: Response<JSONResponse>) {
                    if (response.body() != null && response.body()!!.links != null
                            && response.body()!!.links!!.viaplaySections != null
                            && !response.body()!!.links!!.viaplaySections!!.isEmpty()) {

                        val viaplaySectionList = response.body()!!.links!!.viaplaySections

                        val sectionEntryList = ArrayList<SectionEntry>()

                        for (viaplaySection in viaplaySectionList!!) {
                            val currentSectionName = viaplaySection.title!!.toLowerCase()
                            // For the first time, the title and description are not retrieved yet,
                            // so default to empty
                            val currentSectionEntry = SectionEntry(currentSectionName, "", "")
                            sectionEntryList.add(currentSectionEntry)
                        }

                        mDownloadedSectionList.postValue(sectionEntryList)

                    } else {
                        Log.e(LOG_TAG, "There is no ViaplaySections comes back from internet with this url=" + response.raw().request().url().toString())
                    }
                }

                override fun onFailure(call: Call<JSONResponse>, t: Throwable) {
                    Log.e(LOG_TAG, "Failed to get ViaplaySections back.", t)
                }
            })
        }
    }

    companion object {

        private val LOG_TAG = SectionNetworkDataSource::class.java.simpleName

        private var sInstance: SectionNetworkDataSource? = null
        @Synchronized
        fun getInstance(executors: AppExecutors): SectionNetworkDataSource {
            if (sInstance == null) {
                sInstance = SectionNetworkDataSource(executors)
            }
            return sInstance as SectionNetworkDataSource
        }
    }
}
