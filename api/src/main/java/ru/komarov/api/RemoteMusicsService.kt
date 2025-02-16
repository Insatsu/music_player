package ru.komarov.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteMusicsService {

    @GET("chart")
    suspend fun getChart(): RemoteListMusic

    @GET("track/{id}")
    suspend fun getMusicById(@Path("id") id: Int): RemoteMusic

    @GET("search")
    suspend fun getMusicBySearch(@Query("q") query: String): RemoteTracks

}

fun RemoteMusicsService(): RemoteMusicsService{
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val okhttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(okhttpClient).addConverterFactory(GsonConverterFactory.create()).build()

    return retrofit.create(RemoteMusicsService::class.java)
}

const val BASE_URL = "https://api.deezer.com/"