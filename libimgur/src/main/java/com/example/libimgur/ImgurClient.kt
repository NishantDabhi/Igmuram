package com.example.libimgur

import com.example.libimgur.apis.ImgurAPIv3
import com.example.libimgur.converters.EnumConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ImgurClient {
    //benefit of doing {lazy} is whenever someone actually try to access api than only it will create httpclient and retroft
    // once it is created than next time it will use the already created one, not create again
    // it is threadsafe and syncronised by default

    const val API_KEY = "bae0d1e8ec348d7"

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor {
                val request = it.request().newBuilder()
                    .addHeader("Authorization" , "Client-ID $API_KEY")
                    .build()
                it.proceed(request)
            }
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .addConverterFactory(EnumConverterFactory()) // enumConverterFactory to convert enum to string
            .baseUrl("https://api.imgur.com/3/")
            .build()
    }
    val api: ImgurAPIv3 by lazy {
        retrofit.create(ImgurAPIv3::class.java)
    }
}