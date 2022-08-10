package com.app.coinstatstask

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CoinsRecyclerAdapter :RecyclerView.Adapter<CoinsRecyclerAdapter.CoinViewHolder>()  {

    var items:List<CoinAdapterModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        return CoinViewHolder(CoinsRecyclerItemView(parent.context))
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val itemView = (holder.itemView as CoinsRecyclerItemView)
        itemView.configureItem(items?.get(position))
    }

    override fun getItemCount(): Int {
        return items?.size?:0
    }

    class CoinViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
}