package ru.komarov.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface MusicsService {

}

fun MusicsService(): MusicsService{
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val okhttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val retrofit = Retrofit.Builder().baseUrl("").client(okhttpClient).addConverterFactory(GsonConverterFactory.create()).build()

    return retrofit.create(MusicsService::class.java)
}