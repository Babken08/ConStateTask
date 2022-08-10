package com.app.coinstatstask.framWork

import android.util.Log
import com.app.beans.frameWork.RetrofitClient.RetrofitClientCreator
import com.app.coinstatstask.data.CoinDataSource
import com.app.coinstatstask.data.CoinsDatModel
import com.app.coinstatstask.data.CoinsFetchedModel
import com.app.coinstatstask.data.DataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CoinHttpDataSource :CoinDataSource {
    private val httpClient = RetrofitClientCreator.getRetrofitClient()
    override suspend fun readCoins(): DataResponse<CoinsDatModel>? {
        val result = httpClient.getCoins()
        return createDataResponse(result)
    }

    override suspend fun readFetchedCoins(): DataResponse<CoinsFetchedModel>? {
        val result = httpClient.getFetchedCoins("array")
//        httpClient.getFetchedCoins("array").enqueue(object :Callback<CoinsFetchedModel?>{
//            override fun onResponse(
//                call: Call<CoinsFetchedModel?>,
//                response: Response<CoinsFetchedModel?>
//            ) {
//                Log.i("ssss", "ssss")
//            }
//
//            override fun onFailure(call: Call<CoinsFetchedModel?>, t: Throwable) {
//                Log.i("sssssss", "sssssss")
//            }
//
//        })
        return createDataResponse(result)
    }

    private fun <T> createDataResponse(httpResponse:Response<T?>?) :DataResponse<T> {
        val dataResponse = DataResponse<T>()
        dataResponse.body = httpResponse?.body()
        dataResponse.code = httpResponse?.code()
        dataResponse.responseMessage = httpResponse?.message()
        dataResponse.status = if (httpResponse?.isSuccessful == true){DataResponse.DataResponseStatus.SUCCESS}else{DataResponse.DataResponseStatus.FAIL}
        return dataResponse
    }
}