package com.app.coinstatstask

import androidx.lifecycle.ViewModel
import com.app.coinstatstask.data.CoinRepository

open class CoinViewModel(protected val repository: CoinRepository):ViewModel()