package com.app.coinstatstask

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.coinstatstask.data.DataResponse

class CoinsActivity : AppCompatActivity() {
    private lateinit var activityView:CoinsActivityView
    private var viewModel:CoinsActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar_color)

        activityView = CoinsActivityView(this)
        setContentView(activityView)
        initRecycler()
        viewModel = ViewModelProvider(this, CoinViewModelFactory).get(CoinsActivityViewModel::class.java)
        initObservers()
        viewModel?.loadCoins(::callCoinsCallBack,::checkResponseErrors)
    }

    private fun initRecycler() {
        activityView.recyclerView.layoutManager = LinearLayoutManager(this)
        activityView.recyclerView.setHasFixedSize(true)
        activityView.recyclerView.adapter = CoinsRecyclerAdapter()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun callCoinsCallBack(items:List<CoinAdapterModel>?) {
        val adapter  = (activityView.recyclerView.adapter as CoinsRecyclerAdapter?)
        adapter?.items = items
        adapter?.notifyDataSetChanged()
        activityView.progress.visibility = View.GONE
        if (items != null && items.isNotEmpty()) {
            viewModel?.startTimer()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObservers() {
        viewModel?.updatesItemsPositions?.observe(this){
            if (it.size < 15) {
                for (i in it) {
                    activityView.recyclerView.adapter?.notifyItemChanged(i)
                }
            } else {
                activityView.recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun checkResponseErrors(dataResponse: DataResponse<*>?):Boolean {
        return if (dataResponse == null || !dataResponse.isSuccess()) {
            toastInfo(R.string.default_request_error)
            true
        } else {
            false
        }
    }

    private fun toastInfo(infoId:Int) {
        Toast.makeText(this, infoId, Toast.LENGTH_SHORT).show()
    }

    private fun toastInfo(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}