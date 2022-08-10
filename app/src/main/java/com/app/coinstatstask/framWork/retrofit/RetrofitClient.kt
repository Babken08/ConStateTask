package com.app.beans.frameWork.RetrofitClient

import com.app.coinstatstask.data.CoinsFetchedModel
import com.app.coinstatstask.data.CoinsDatModel
import retrofit2.Response
import retrofit2.http.*

interface RetrofitClient {
    @GET("coins")
    suspend fun getCoins(): Response<CoinsDatModel?>

    @GET("coins/")
    suspend fun getFetchedCoins(@Query("responseType") responseType:String):Response<CoinsFetchedModel?>
}