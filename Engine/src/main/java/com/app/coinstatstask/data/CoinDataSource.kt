package com.app.coinstatstask.data

interface CoinDataSource {
    suspend  fun readCoins():DataResponse<CoinsDatModel>?
    suspend fun readFetchedCoins():DataResponse<CoinsFetchedModel>?
}