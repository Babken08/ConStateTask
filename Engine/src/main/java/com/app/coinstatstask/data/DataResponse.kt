package com.app.coinstatstask.data

class  DataResponse<T> {
    var body:T? = null
    var status: DataResponseStatus? = null
    var code:Int? = null
    var responseMessage:String? = null

    fun isSuccess() :Boolean {
        return status == DataResponseStatus.SUCCESS
    }

    enum class DataResponseStatus{
        SUCCESS, FAIL
    }
}