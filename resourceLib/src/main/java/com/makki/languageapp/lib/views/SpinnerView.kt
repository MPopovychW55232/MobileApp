package com.makki.languageapp.lib.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * @author Maksym.Popovych
 */
class SpinnerView : AppCompatImageView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val drawable: SpinnerDrawable

    init {
        setWillNotDraw(false)
        drawable = SpinnerDrawable(context)
        setImageDrawable(drawable)
    }
}