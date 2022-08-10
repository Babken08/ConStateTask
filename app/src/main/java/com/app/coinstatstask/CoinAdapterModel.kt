package com.app.coinstatstask

import com.app.coinstatstask.domain.Coin

class CoinAdapterModel {
    var identifier:String? = null
    var imageUrl:String? = null
    var rank:Int? = null
    var price:Double? = null
    var name:String? = null
    var symbol:String? = null
    var percent:Double? = null

    fun configure(item:Coin) {
        identifier = item.i
        imageUrl = item.ic
        rank = item.r
        price = item.pu
        name = item.n
        symbol = item.s
        percent = item.p24
    }
}
