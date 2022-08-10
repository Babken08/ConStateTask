package com.app.coinstatstask

import androidx.lifecycle.MutableLiveData
import com.app.coinstatstask.data.CoinRepository
import com.app.coinstatstask.data.CoinsDatModel
import com.app.coinstatstask.data.CoinsFetchedModel
import com.app.coinstatstask.data.DataResponse
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class CoinsActivityViewModel(repository: CoinRepository):CoinViewModel(repository) {
    private var coroutineScope = CoroutineScope(Dispatchers.IO)
    private var itemsMap:MutableMap<String?, ModelWithPosition> = LinkedHashMap()
    var updatesItemsPositions:MutableLiveData<List<Int>> = MutableLiveData()
    private var timer:Timer? = null
    private var timerTask:TimerTask? = null

    fun loadCoins(callBack:(items:List<CoinAdapterModel>?) -> Unit, handleErrorsIfExist:(DataResponse<*>?) -> Boolean) {
        if (itemsMap.isNotEmpty()) {
            callBack(getListFromMap(itemsMap))
            return
        }
        coroutineScope.launch {
            val responseData = repository.getCoins()
            withContext(Dispatchers.Main) {
                if (!handleErrorsIfExist(responseData)) {
                    withContext(Dispatchers.IO) {
                        val items = createAndGetItems(responseData?.body)
                        withContext(Dispatchers.Main) {
                            callBack(items)
                        }
                    }
                }
            }
        }
    }

    private fun getListFromMap(map:Map<String?, ModelWithPosition>):List<CoinAdapterModel> {
        val list = ArrayList<CoinAdapterModel>()
        for (item in map){
            list.add(item.value.model)
        }
        return list
    }

    fun startTimer() {
        if (timerTask == null) {
            createTimerTask()
        } else {
            timerTask?.cancel()
            createTimerTask()
        }
        if (timer != null) {
            timer?.cancel()
            timer = null
        }
        timer = Timer()
        timer!!.schedule(timerTask, 0, 5000)
    }

    private var fetchedInProgress = false
    private fun createTimerTask() {
        timerTask = object :TimerTask(){
            override fun run() {
                if (!fetchedInProgress){
                    fetchedInProgress = true
                    coroutineScope.launch {
                        handleFetchedResponse(repository.getFetchedCoins())
                    }
                }
            }
        }
    }

    private suspend fun handleFetchedResponse(response: DataResponse<CoinsFetchedModel>?) {
        if (response?.isSuccess() == true && !response.body?.coins.isNullOrEmpty()) {
            val list = ArrayList<Int>(response.body!!.coins!!.size)
            for (fetchedItem in response.body!!.coins!!) {
                if (!fetchedItem.isNullOrEmpty()) {
                    val identifier = fetchedItem[0]
                    val existItem = itemsMap[identifier]
                    fetchItem(existItem?.model, fetchedItem)
                    existItem?.position?.let {
                        list.add(it)
                    }
                }
            }
            withContext(Dispatchers.Main) {
                updatesItemsPositions.value = list
                fetchedInProgress = false
            }
        } else {
            withContext(Dispatchers.Main) {
                fetchedInProgress = false
            }
        }
    }

    private fun fetchItem(item:CoinAdapterModel?, list:List<Any?>) {
        try {
            item?.rank = list[1] as Int?
            item?.price = list[2] as Double?
            item?.percent = list[7] as Double?
        } catch (e:IndexOutOfBoundsException){
        } catch (e:ClassCastException){}
    }

    private fun createAndGetItems(dataModel:CoinsDatModel?):List<CoinAdapterModel>? {
        val list  = dataModel?.coins
        val items :ArrayList<CoinAdapterModel>?

        if (list == null) {
            items = null
        } else {
            items = ArrayList()
            for (i in list.indices) {
                val item  = list[i]
                val model = CoinAdapterModel()
                model.configure(item)
                items.add(model)
                itemsMap[model.identifier] = ModelWithPosition(model, i)
            }
        }
        return items
    }

    override fun onCleared() {
        super.onCleared()
        try {
            timerTask?.cancel()
            coroutineScope.cancel()
        }catch (e:Exception) {
            e.printStackTrace()
        }
    }

    private data class ModelWithPosition(val model: CoinAdapterModel, val position:Int)
}