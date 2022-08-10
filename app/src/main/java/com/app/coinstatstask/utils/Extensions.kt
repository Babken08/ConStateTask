package com.app.coinstatstask.utils

import android.content.res.Resources

private val one_dp:Int by lazy { Resources.getSystem().displayMetrics.density.toInt() }
var Int.dp: Int
    get() {
        return this*one_dp
    }
    set(value) {}