package com.makki.languageapp.lib.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.view.animation.CycleInterpolator
import androidx.core.graphics.toRectF
import com.makki.languageapp.lib.R
import com.makki.languageapp.lib.utils.asColor
import kotlin.math.*

/**
 * @author Maksym.Popovych
 */
class SpinnerDrawable(context: Context) : Drawable() {

    var count = 7
        set(value) {
            field = value
            angleDelta = Math.PI * 2 / count
        }

    private var radius = 0f
    private var dotSize = 0f
    private val duration = 2000
    private var tempRect = RectF()
    private var boundsRect = RectF()
    private val interpolator = CycleInterpolator(1f)
    private var angleDelta = Math.PI * 2 / count

    private val paint = Paint()

    init {
        paint.color = R.color.colorAccentMain.asColor(context)
        paint.flags = Paint.ANTI_ALIAS_FLAG
    }

    override fun draw(canvas: Canvas) {
        val rotateFraction = SystemClock.uptimeMillis() % duration / duration.toFloat()
        val zoomFraction = 1f - max(interpolator.getInterpolation(rotateFraction), 0f)

        val animatedDegree = Math.PI * 2 * rotateFraction

        val radiusT = ((radius - (dotSize / 2)) * zoomFraction)
        for (i in 0..count) {
            val angle = angleDelta * i + animatedDegree
            val x = (radius + radiusT * cos(angle) - (dotSize / 2)).roundToInt()
            val y = (radius + radiusT * sin(angle) - (dotSize / 2)).roundToInt()

            tempRect.set(x.toFloat(), y.toFloat(), x + dotSize, y + dotSize)
            canvas.drawArc(tempRect, 0f, 360f, true, paint)
        }

        try {
            invalidateSelf()
        } catch (ignore: Exception) {
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)

        val rect = bounds?.toRectF() ?: return
        boundsRect = rect

        radius = min(rect.width(), rect.height()) / 2
        dotSize = radius / 3
    }
}
