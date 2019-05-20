package com.makki.languageapp.lib.animators

import android.view.animation.AccelerateInterpolator

/**
 * @author Maksym.Popovych
 */
class FloatAnimator(from: Float = 0f, to: Float = 1f) : SmoothAnimator() {

    var value = from
        private set
    var startValue = from
    var endValue = to

    var lastFraction = 0f
        private set

    private var updateCallback: ((Float) -> Unit)? = null
    private var fractionCallback: ((Float) -> Unit)? = null

    init {
        setInterpolator(AccelerateInterpolator())
    }

    override fun onUpdate(fraction: Float) {
        value = startValue + (endValue - startValue) * fraction

        updateCallback?.invoke(value)
        fractionCallback?.invoke(fraction - lastFraction)
        lastFraction = fraction
    }

    fun setValues(from: Float, to: Float): FloatAnimator {
        value = from
        startValue = from
        endValue = to
        lastFraction = 0f
        return this
    }

    fun setUpdateListener(block: ((Float) -> Unit)?): FloatAnimator {
        updateCallback = block
        return this
    }

    fun setFractionListener(block: ((Float) -> Unit)?): FloatAnimator {
        fractionCallback = block
        return this
    }

    override fun start(): SmoothAnimator {
        lastFraction = 0f
        return super.start()
    }

    override fun cancel(): SmoothAnimator {
        lastFraction = 0f
        return super.cancel()
    }
}