package com.app.coinstatstask

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.coinstatstask.utils.UiUtils.getToolbarHeight
import com.app.coinstatstask.utils.dp

class CoinsActivityView(context: Context) :ViewGroup(context) {
    private var toolBarHeight:Int = getToolbarHeight(context)
    private var margins = 16.dp
    private var pageTitle:TextView? = null
    private var tvUsedTo:TextView? = null

    private var tvCoin:TextView? = null
    private var tvHour:TextView? = null
    private var tvPrice:TextView? = null

    lateinit var recyclerView: RecyclerView
    lateinit var progress:ProgressBar

    init {
        createTopLayout()
        createDescriptionLayout()
        createRecyclerView()
        createProgress()
    }

    private fun createTopLayout() {
        pageTitle = TextView(context)
        pageTitle?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
        pageTitle?.setTextColor(ContextCompat.getColor(context, R.color.color_black_text_color))
        pageTitle?.typeface = Typeface.DEFAULT_BOLD
        pageTitle?.setText(R.string.coin_list)
        this.addView(pageTitle)

        tvUsedTo = TextView(context)
        tvUsedTo?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
        tvUsedTo?.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray))
        tvUsedTo?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_search_24, 0)
        tvUsedTo?.compoundDrawablePadding = margins
        tvUsedTo?.setText(R.string.used_to)
        tvUsedTo?.setOnClickListener { Toast.makeText(context, "this is a click", Toast.LENGTH_SHORT).show() }
        this.addView(tvUsedTo)
    }

    @SuppressLint("SetTextI18n")
    private fun createDescriptionLayout() {
        tvCoin = TextView(context)
        tvCoin?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        tvCoin?.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray_2))
        tvCoin?.setText(R.string.coin)
        this.addView(tvCoin)

        tvHour = TextView(context)
        tvHour?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        tvHour?.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray_2))
        tvHour?.text = "24${context.getString(R.string.hour)}"
        this.addView(tvHour)

        tvPrice = TextView(context)
        tvPrice?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        tvPrice?.setTextColor(ContextCompat.getColor(context, R.color.text_color_gray_3))
        tvPrice?.setText(R.string.price)
        tvPrice?.typeface = Typeface.DEFAULT_BOLD
        this.addView(tvPrice)
    }

    private fun createRecyclerView() {
        recyclerView = RecyclerView(context)
        this.addView(recyclerView)
    }

    private fun createProgress() {
        progress = ProgressBar(context)
        this.addView(progress)
    }

    private fun calculateRecyclerViewHeight(screenH:Int) :Int {
        return screenH - toolBarHeight - 3*margins - (tvPrice?.measuredHeight?:0)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val maxW = (w / 3) - 2*margins
        pageTitle?.measure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(h, MeasureSpec.AT_MOST))
        tvUsedTo?.measure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(h, MeasureSpec.AT_MOST))
        tvCoin?.measure(MeasureSpec.makeMeasureSpec(maxW, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(h, MeasureSpec.AT_MOST))
        tvHour?.measure(MeasureSpec.makeMeasureSpec(maxW, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(h, MeasureSpec.AT_MOST))
        tvPrice?.measure(MeasureSpec.makeMeasureSpec(maxW, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(h, MeasureSpec.AT_MOST))
        recyclerView.measure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(calculateRecyclerViewHeight(h), MeasureSpec.AT_MOST))
        if (progress.visibility == View.VISIBLE) {
            progress.measure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(h, MeasureSpec.AT_MOST))
        }
        setMeasuredDimension(w, h)
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        val titleTop = (toolBarHeight - (pageTitle?.measuredHeight?:0)) /2
        val titleLeft  = (width- (pageTitle?.measuredWidth?:0))/2
        pageTitle?.layout(titleLeft, titleTop, titleLeft + (pageTitle?.measuredWidth?:0), titleTop + (pageTitle?.measuredHeight?:0))
        val usdToLeft = width - margins - (tvUsedTo?.measuredWidth?:0)
        tvUsedTo?.layout(usdToLeft, titleTop, usdToLeft + (tvUsedTo?.measuredWidth?:0), titleTop + (tvUsedTo?.measuredHeight?:0))

        val maxW = width/3 - 2*margins
        val tvCoinTop = toolBarHeight + margins
        tvCoin?.layout(margins, tvCoinTop, margins + maxW, tvCoinTop + (tvCoin?.measuredHeight?:0))
        val hourLeft  = width/2 - (tvHour?.measuredWidth?:0)/2
        tvHour?.layout(hourLeft, tvCoinTop, hourLeft+(tvHour?.measuredWidth?:0), tvCoinTop + (tvHour?.measuredHeight?:0))
        tvPrice?.layout(width - margins - (tvPrice?.measuredWidth?:0), tvCoinTop, width - margins, tvCoinTop + (tvPrice?.measuredHeight?:0))

        val recyclerTop = tvCoinTop + (tvPrice?.measuredHeight?:0) + margins
        recyclerView.layout(margins, recyclerTop, width - margins, recyclerTop + (recyclerView.measuredHeight))

        if (progress.visibility == View.VISIBLE) {
            val progressTop = (height - progress.measuredHeight) / 2
            val progressLeft = (width - progress.measuredWidth) / 2
            progress.layout(progressLeft, progressTop, progressLeft + progress.measuredWidth, progressTop + progress.measuredHeight)
        }
    }
}