package com.app.coinstatstask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.coinstatstask.data.CoinRepository

object CoinViewModelFactory : ViewModelProvider.Factory {
    private var repository:CoinRepository? = null
    fun inject(repository:CoinRepository) {
        this.repository = repository
    }
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (CoinViewModel::class.java.isAssignableFrom(modelClass)) {
            return modelClass.getConstructor(CoinRepository::class.java).newInstance(repository)
        } else {
            throw IllegalStateException("ViewModel must extend CoinViewModel")
        }
    }
}