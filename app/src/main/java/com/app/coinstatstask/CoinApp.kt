package com.app.coinstatstask

import android.app.Application
import com.app.coinstatstask.data.CoinRepository
import com.app.coinstatstask.framWork.CoinHttpDataSource

class CoinApp:Application() {

    override fun onCreate() {
        super.onCreate()
        injectData()
    }

    private fun injectData() {
        val coinDataSource = CoinHttpDataSource()
        val coinRepository = CoinRepository(coinDataSource)
        CoinViewModelFactory.inject(coinRepository)
    }
}