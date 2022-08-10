package com.app.beans.frameWork.RetrofitClient

import com.app.coinstatstask.framWork.UrlWrapper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientCreator {
    fun getRetrofitClient():RetrofitClient{
        return getRetrofit().create(RetrofitClient::class.java)
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UrlWrapper.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}