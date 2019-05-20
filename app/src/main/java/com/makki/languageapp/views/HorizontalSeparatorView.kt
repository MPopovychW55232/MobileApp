package com.makki.languageapp.views

import android.content.Context
import android.widget.FrameLayout
import com.makki.languageapp.R

/**
 * @author Maksym.Popovych
 */
class HorizontalSeparatorView(context: Context) : FrameLayout(context) {

    init {
        inflate(context, R.layout.v_separtor, this)
    }
}