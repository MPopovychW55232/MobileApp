package com.makki.languageapp.lib.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.view.animation.BounceInterpolator
import androidx.core.graphics.toRectF
import com.makki.languageapp.lib.R
import com.makki.languageapp.lib.utils.asColor
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * @author Maksym.Popovych
 */
class RollinDrawable(context: Context) : Drawable() {

    var count = 7
        set(value) {
            field = value
            angleDelta = Math.PI * 2 / count
        }

    private var radius = 0f
    private var dotSize = 0f
    private val duration = 1500
    private var rollDistance = 0f
    private var distancePerRotation = 0f
    private var tempRect = RectF()
    private var boundsRect = RectF()
    private var angleDelta = Math.PI * 2 / count

    private val bounceInterpolator = BounceInterpolator()

    private val paint = Paint()

    init {
        paint.color = R.color.colorAccentMain.asColor(context)
        paint.flags = Paint.ANTI_ALIAS_FLAG
    }

    override fun draw(canvas: Canvas) {
        val fraction = SystemClock.uptimeMillis() % duration / duration.toFloat()
        val positionX = rollDistance * fraction - radius * 2

        val positionY = (boundsRect.height() - radius * 2) * bounceInterpolator.getInterpolation(fraction)

        val rotateFraction = positionX % distancePerRotation / distancePerRotation

        val radiusT = (radius - (dotSize / 2))
        for (i in 0..count) {
            val angle = angleDelta * i + rotateFraction
            val x = (radius + radiusT * cos(angle) - (dotSize / 2)).roundToInt()
            val y = (radius + radiusT * sin(angle) - (dotSize / 2)).roundToInt()

            tempRect.set(
                positionX + x.toFloat(),
                positionY + y.toFloat(),
                positionX + x + dotSize,
                positionY + y + dotSize
            )
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

        radius = min(rect.width(), rect.height()) / 4
        dotSize = radius / 2

        rollDistance = rect.width() + radius * 2
        distancePerRotation = radius * Math.PI.toFloat()
    }
}
