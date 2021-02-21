package cu.wilb3r.news.data.api

import cu.wilb3r.news.data.model.NewsResponse
import cu.wilb3r.news.data.network.ApiResponse
import cu.wilb3r.news.data.network.FlowCallAdapterFactory
import cu.wilb3r.news.others.Constants
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("everything")
    fun getEverything(
        @QueryMap filter: Map<String, String>
    ): Flow<ApiResponse<NewsResponse>>

    companion object {
        operator fun invoke(
                httpLoggingInterceptor: HttpLoggingInterceptor
        ): ApiService {
            val okHttpclient = OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(40, TimeUnit.SECONDS)
                    .build()
            return Retrofit.Builder()
                    .client(okHttpclient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(FlowCallAdapterFactory())
                    .baseUrl(Constants.API_URL)
                    .build()
                    .create(ApiService::class.java)
        }
    }
}