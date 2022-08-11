package com.app.coinstatstask

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import com.app.coinstatstask.utils.dp
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception
import kotlin.math.abs

class CoinsRecyclerItemView(context: Context):View(context) {
    private var image:Bitmap? = null
            set(value) {
                field = value
                invalidate()
            }
    private var imageSize = 24.dp
    private var margins = 16.dp

    private lateinit var namePaint:TextPaint
    private lateinit var symbolPaint:TextPaint
    private lateinit var rankPaint:TextPaint
    private lateinit var percentPaint:TextPaint
    private lateinit var pricePaint:TextPaint
    private var percentBgGreen:Drawable? = null
    private var percentBgRed:Drawable? = null
    private var rankBg:Drawable? = null

    private var nameRect = Rect()
    private var symbolRect = Rect()
    private var rankRect = Rect()
    private var percentChangeRect = Rect()
    private var priceRect = Rect()
    private var item:CoinAdapterModel? = null
    private var upDrawable:Drawable? = null
    private var downDrawable:Drawable? = null
    private var starDrawable:Drawable? = null
    private lateinit var bitMapPaint:Paint
    private var defaultImage:Drawable? = null
    private lateinit var target:Target


    init {
        createPaints()
        initDrawables()
        createTarget()
    }

    private fun createPaints() {
        namePaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        namePaint.textSize = 14.dp.toFloat()
        namePaint.color = ContextCompat.getColor(context, R.color.text_color_gray_3)

        symbolPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        symbolPaint.textSize = 12.dp.toFloat()
        symbolPaint.color = ContextCompat.getColor(context, R.color.text_color_gray_2)

        percentPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        percentPaint.textSize = 12.dp.toFloat()
        percentPaint.color = ContextCompat.getColor(context, R.color.green_text_color)

        pricePaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        pricePaint.textSize = 14.dp.toFloat()
        pricePaint.color = ContextCompat.getColor(context, R.color.color_black_text_color)
        pricePaint.typeface = Typeface.DEFAULT_BOLD

        rankPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        rankPaint.textSize = 12.dp.toFloat()
        rankPaint.color = ContextCompat.getColor(context, R.color.text_color_gray_4)
        bitMapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    private fun initDrawables() {
        percentBgGreen = ContextCompat.getDrawable(context, R.drawable.ic_statistic_green)
        percentBgRed = ContextCompat.getDrawable(context, R.drawable.ic_statistic_red)
        defaultImage =  ContextCompat.getDrawable(context, R.drawable.ic_baseline_currency_bitcoin_24)
        upDrawable = ContextCompat.getDrawable(context, R.drawable.ic_up)
        downDrawable = ContextCompat.getDrawable(context, R.drawable.ic_down_)
        rankBg = ContextCompat.getDrawable(context, R.drawable.bg_drawable)
        starDrawable = ContextCompat.getDrawable(context, R.drawable.ic_star)
    }

    private fun createTarget() {
        target = object :Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                image = bitmap
            }
            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

        }
    }

    private fun initRects(){
        initSingleRect(item?.name, namePaint, nameRect)
        initSingleRect(item?.symbol, symbolPaint, symbolRect)
        if (item?.percent != null) {
            val percentText = abs(item!!.percent!!).toString()
            initSingleRect("${percentText}%", percentPaint, percentChangeRect)
        }
        initSingleRect("$${item?.price?.toString()}", pricePaint, priceRect)
        initSingleRect(item?.rank?.toInt().toString(), rankPaint, rankRect)
    }

    private fun initSingleRect(str:String?, paint:TextPaint?, rect:Rect) {
        str?.let {
            paint?.getTextBounds(it, 0, it.length, rect)
        }
    }

    private fun calculateItemHeight() :Int{
        val h = nameRect.height()  + (rankBg?.intrinsicHeight?:0) + margins
        return Math.max(h, imageSize + margins)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(w, calculateItemHeight())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?:return
        val bitMapTop = (height - imageSize) / 2f

        if (image == null) {
            defaultImage?.setBounds(0, bitMapTop.toInt(), imageSize, imageSize)
            defaultImage?.draw(canvas)
        } else {
            canvas.drawBitmap(image!!, 0f, bitMapTop, bitMapPaint)
        }


        drawNameAndStar(canvas)
        drawRankAndSymbolSell(canvas)
        drawPercentSell(canvas)
        item?.price?.let {
            val x = (width - priceRect.width()) - 2.dp
            val y  = (height + priceRect.height()) /2f
            canvas.drawText("$${it}", x.toFloat(), y, pricePaint)
        }
    }

    private fun drawNameAndStar(canvas: Canvas) {
        val nameTop = height/2f - margins/4
        item?.name?.let {
            val textMaxWith = width /3f - imageSize - margins/2
            val txt = TextUtils.ellipsize(it, namePaint, textMaxWith, TextUtils.TruncateAt.END).toString()
            canvas.drawText(txt, imageSize + margins/2f, nameTop, namePaint)

            if (starDrawable != null) {
                val starTop = (height - starDrawable!!.intrinsicHeight) / 2
                val starLeft = imageSize + margins/2 + textMaxWith + margins/2
                starDrawable!!.setBounds(starLeft.toInt(), starTop, starLeft.toInt() + starDrawable!!.intrinsicWidth, starTop + starDrawable!!.intrinsicHeight)
                starDrawable!!.draw(canvas)
            }
        }
    }

    private fun drawRankAndSymbolSell(canvas: Canvas) {
        val bgDrawableWith = Math.max(18.dp, rankRect.width() + margins/2)
        val bgDrawableHeight = rankRect.height() + margins/2
        val left = imageSize + margins/2
        val right = left + bgDrawableWith
        val top = height/2 + margins/4
        val bottom = top + bgDrawableHeight
        val textMaxWith = width /3f - imageSize - margins/2 - bgDrawableWith - margins/2
        val textTop = top + margins/4f + rankRect.height()
        rankBg?.let {
            it.setBounds(left, top, right, bottom)
            it.draw(canvas)
        }
        item?.rank?.let {
            val l = left + (bgDrawableWith/2 - rankRect.width()/2)
            canvas.drawText(it.toInt().toString(), l.toFloat(),textTop, rankPaint)
        }
        item?.symbol?.let {
            val txt = TextUtils.ellipsize(it, symbolPaint, textMaxWith, TextUtils.TruncateAt.END).toString()
            canvas.drawText(txt, right + margins/2f, textTop, symbolPaint)
        }
    }

    private fun drawPercentSell(canvas: Canvas) {
        if (item?.percent == null) return
        val bgDrawableWith = percentChangeRect.width() + (upDrawable?.intrinsicWidth?:0) + margins
        val bgDrawableHeight = percentChangeRect.height() + margins
        val left = (width - bgDrawableWith)/2 + margins
//        val left = 2 * (width/3) - bgDrawableWith - margins/2
        val right = left + bgDrawableWith
        val percentText = abs(item!!.percent!!)


        if (item!!.percent!! >= 0) {
            percentPaint.color = ContextCompat.getColor(context, R.color.green_text_color)
            percentBgGreen?.let {
                val top = (height - bgDrawableHeight) /2
                val bottom = top + bgDrawableHeight
                it.setBounds(left, top, right, bottom)
                it.draw(canvas)
            }
            upDrawable?.let {
                // the logic was not written in the dock. That's why I always draw the up icon
                val x = right - percentChangeRect.width() - 2*margins/3 - it.intrinsicWidth
                val top = height/2 - it.intrinsicHeight/2
                val bottom = top +  it.intrinsicHeight
                it.setBounds(x, top, x+it.intrinsicWidth, bottom)
                it.draw(canvas)
            }
        } else {
            percentPaint.color = ContextCompat.getColor(context, R.color.text_red)
            percentBgRed?.let {
                val top = (height - bgDrawableHeight) /2
                val bottom = top + bgDrawableHeight
                it.setBounds(left, top, right, bottom)
                it.draw(canvas)
            }
            downDrawable?.let {
                // the logic was not written in the dock. That's why I always draw the up icon
                val x = right - percentChangeRect.width() - 2*margins/3 - it.intrinsicWidth
                val top = height/2 - it.intrinsicHeight/2
                val bottom = top +  it.intrinsicHeight
                it.setBounds(x, top, x+it.intrinsicWidth, bottom)
                it.draw(canvas)
            }
        }

        val x = right - percentChangeRect.width() - margins/3f
        val y  = (height + percentChangeRect.height()) /2f
        canvas.drawText("${percentText}%", x, y, percentPaint)
    }

    fun configureItem(item:CoinAdapterModel?) {
        item?:return
        if (this.item == item) {
            if (image != null) {
                image = image
            } else {
                Picasso.get().load(item.imageUrl).resize(imageSize, imageSize).into(target)
            }
        } else {
            image = null
            this.item = item
            Picasso.get().load(item.imageUrl).resize(imageSize, imageSize).into(target)
        }
        initRects()
    }
}