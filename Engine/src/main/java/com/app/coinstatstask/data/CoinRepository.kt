package com.app.coinstatstask.data


class CoinRepository(private val coinDataSource: CoinDataSource) {
    private var coinsDataResponse:DataResponse<CoinsDatModel>? = null
    suspend fun getCoins() :DataResponse<CoinsDatModel>? {
        if (coinsDataResponse != null && coinsDataResponse!!.isSuccess()) {
            return coinsDataResponse
        }
        this.coinsDataResponse =  coinDataSource.readCoins()
        return coinsDataResponse
    }

    suspend fun getFetchedCoins() = coinDataSource.readFetchedCoins()
}