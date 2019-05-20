package com.makki.languageapp.lib.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.makki.languageapp.lib.R
import com.makki.languageapp.lib.animators.FloatAnimator
import com.makki.languageapp.lib.utils.asColor
import com.makki.languageapp.lib.utils.safeSlice
import kotlin.math.*


/**
 * @author Maksym.Popovych
 */
class TimeGraphView : View, GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {
    companion object {
        private const val FLING_MIN_DISTANCE = 300
        private const val FLING_THRESHOLD = 200
    }

    private val animator = FloatAnimator().also {
        it.setDuration(350L)
        it.setUpdateListener { fraction ->
            maxValue += (maxTargetValue - maxValue) * fraction
            minValue += (minTargetValue - minValue) * fraction
            invalidate()
        }
    }

    private var flingVelocity = 0f
    private val flingAnimator = FloatAnimator().also {
        it.setDuration(800L)
        it.setInterpolator(DecelerateInterpolator())
        it.setFractionListener { fraction -> moveX(flingVelocity * fraction) }
    }

    private val gestureDetector = GestureDetector(context, this)
    private val scaleDetector = ScaleGestureDetector(context, this)

    var mainSource: List<Action> = emptyList()
        set(value) {
            field = value
            onContentChanged()
        }

    var secondarySource: List<Action> = emptyList()
        set(value) {
            field = value
            onContentChanged()
        }

    private var main = mainSource
    private var secondary = secondarySource

    private val textSize = 45f

    private val mainPaint = Paint().also {
        it.flags = Paint.ANTI_ALIAS_FLAG
        it.color = Color.GREEN
        it.alpha = 180
        it.style = Paint.Style.FILL
    }
    private val secondaryPaint = Paint().also {
        it.flags = Paint.ANTI_ALIAS_FLAG
        it.color = Color.RED
        it.alpha = 180
        it.style = Paint.Style.FILL
    }
    private val separatorPaint = Paint().also {
        it.flags = Paint.ANTI_ALIAS_FLAG
        it.color = R.color.colorDisabledFlat.asColor(context)
        it.strokeWidth = 2f
        it.textSize = textSize
    }
    private val selectionPaint = Paint().also {
        it.flags = Paint.ANTI_ALIAS_FLAG
        it.color = R.color.colorAccentMain.asColor(context)
        it.alpha = 70
        it.style = Paint.Style.FILL
    }

    private var tPath = Path()

    var minVisibleCount = 7
    private var visibleCountFloat = 0f
    private var visibleCount = 0
        set(value) {
            if (field != value) {
                field = value
                onCountChanged()
            }
        }
    private var offsetCount = 0
        set(value) {
            if (field != value) {
                field = value
                onCountChanged()
            }
        }

    private val maxScale = 183 / minVisibleCount.toFloat()
    private var maxOffset = 0f
    private var scale = 1f
        set(value) {
            field = min(max(0.95f, value), maxScale)
            separatorPaint.textSize = textSize / field
            maxOffset = width.toFloat() * mainSource.size - (scale * minVisibleCount)
        }

    private var offset = 0f
        set(value) {
            field = min(
                max(0f, value),
                width.toFloat() * (mainSource.size - (minVisibleCount.toFloat() * scale)) / (scale * minVisibleCount)
            )
        }

    private var lastVisibleOffset = 0f

    private var maxValue = 0f
    private var minValue = 0f

    private var maxTargetValue = 0f
    private var minTargetValue = 0f

    private val horizontalStep = 1f

    private var selectionCallback: ((String?) -> Unit)? = null
    var selectedId = ""
        set(value) {
            field = value
            invalidate()
        }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        visibleCountFloat = minVisibleCount * scale
        visibleCount = ceil(visibleCountFloat).roundToInt()
        val widthPerItem = width.toFloat() / visibleCountFloat

        val approxOffsetCount = offset / widthPerItem
        offsetCount = min(floor(approxOffsetCount).roundToInt(), mainSource.size - minVisibleCount)
        val xPosDelta = (approxOffsetCount - offsetCount) * widthPerItem
        lastVisibleOffset = xPosDelta / widthPerItem

        val graphHeight = (maxValue - minValue)
        val clearHeight = height - paddingBottom - paddingTop

        for (i in 0..main.size) {
            val xPos = width - i * widthPerItem + xPosDelta
            canvas.drawLine(xPos, paddingTop.toFloat(), xPos, height - paddingBottom.toFloat(), separatorPaint)
        }

        val bottomMark = ceil(minValue)
        var horizontalLine = bottomMark
        while (horizontalLine < maxValue) {
            val yPos = clearHeight - ((horizontalLine - minValue) / graphHeight) * clearHeight + paddingTop
            canvas.drawLine(0f, yPos, width.toFloat(), yPos, separatorPaint)
            horizontalLine += horizontalStep
        }

        val textYTopPos = height * 11f / 12f

        fun drawActions(actions: List<Action>, paint: Paint) {
            tPath.reset()
            for (i in 0..actions.size) {
                val action = actions.getOrNull(i) ?: continue
                if (action == Action.Null) {
                    continue
                }

                val xPos = width - i * widthPerItem + xPosDelta
                val yBot = clearHeight - ((action.entered - minValue) / graphHeight) * clearHeight + paddingTop
                val yTop = clearHeight - ((action.exited - minValue) / graphHeight) * clearHeight + paddingTop

                if (action.id == selectedId) {
                    canvas.drawRect(
                        xPos - widthPerItem + 2,
                        paddingTop.toFloat(),
                        xPos - 2,
                        height - paddingBottom.toFloat(),
                        selectionPaint
                    )
                }

                action.onMeasure(xPos - widthPerItem + 2, yTop, xPos - 2, yBot)
                action.draw(canvas, paint)

                if (action.tag.isNotEmpty() && action != Action.Null && separatorPaint.textSize >= 9f) {
                    canvas.drawText(action.tag, xPos - widthPerItem + 2, textYTopPos + textSize, separatorPaint)
                }
            }
        }

        if (main.isEmpty()) return
        drawActions(secondary, secondaryPaint)
        drawActions(main, mainPaint)

        val sliderWidth = width * (visibleCountFloat / mainSource.size)
        val sliderOffset = width * offsetCount / mainSource.size
        canvas.drawRect(
            width - sliderWidth - sliderOffset,
            height - 30f,
            width.toFloat() - sliderOffset,
            height.toFloat() - 5f,
            separatorPaint
        )

        if (separatorPaint.textSize >= 9f) {
            --horizontalLine
            canvas.drawText(
                "${bottomMark.roundToInt()}:00",
                0f,
                clearHeight - ((bottomMark - minValue) / graphHeight) * clearHeight + paddingTop + separatorPaint.textSize,
                separatorPaint
            )
            canvas.drawText(
                "${horizontalLine.roundToInt()}:00",
                0f,
                clearHeight - ((horizontalLine - minValue) / graphHeight) * clearHeight + paddingTop + separatorPaint.textSize,
                separatorPaint
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        return true
    }

    override fun onShowPress(e: MotionEvent?) {}

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return findSelection(e)
    }

    override fun onDown(e: MotionEvent?): Boolean {
        flingAnimator.cancel()
        return false
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        if (e1.x - e2.x > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_THRESHOLD) {
            flingWithVelocity(velocityX)
            return true
        } else if (e2.x - e1.x > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_THRESHOLD) {
            flingWithVelocity(velocityX)
            return true
        }

        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        moveX(-distanceX)
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        offset = 0f
        invalidate()
    }

    override fun onScaleBegin(detector: ScaleGestureDetector) = true

    override fun onScaleEnd(detector: ScaleGestureDetector) {}

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        val factor = detector.scaleFactor
        if (scale <= 1f && factor > 1f) {
            return false
        }
        scale /= factor

        val delta = 1f - factor
        offset *= factor
        offset -= (width - detector.focusX) * delta

        invalidate()
        return true
    }

    private fun flingWithVelocity(velocity: Float) {
        flingAnimator.cancel()
        if (velocity == 0f) return

        flingVelocity = velocity / 2

        flingAnimator.start()
    }

    private fun moveX(delta: Float) {
        offset += delta
        invalidate()
    }

    private fun findSelection(event: MotionEvent): Boolean {
        val xPercent = event.x / width
        val index =
            floor(visibleCountFloat + lastVisibleOffset - (minVisibleCount.toFloat() * scale) * xPercent).roundToInt()

        val actionId = main.getOrNull(index)?.id ?: return false

        selectionCallback?.invoke(actionId)

        return true
    }

    private fun onContentChanged() {
        selectedId = mainSource.find { it != Action.Null }?.id ?: ""
        selectionCallback?.invoke(selectedId)

        onCountChanged()
    }

    private fun onCountChanged() {
        animator.cancel()

        val padding = 0.5f

        main = mainSource.safeSlice(offsetCount..visibleCount + offsetCount)
        var minNewValue = main.filter { it != Action.Null }.minBy { it.entered }?.entered
            ?: 0f
        var maxNewValue = main.filter { it != Action.Null }.maxBy { it.exited }?.exited
            ?: 0f

        if (secondarySource.isEmpty()) {
            minTargetValue = minNewValue - padding
            maxTargetValue = maxNewValue + padding

            animator.start()
            return
        }
        secondary = secondarySource.safeSlice(offsetCount..visibleCount + offsetCount)
        minTargetValue = min(
            secondary.filter { it != Action.Null }.minBy { it.entered }?.entered
                ?: 0f,
            minNewValue
        ) - padding
        maxTargetValue = max(
            secondary.filter { it != Action.Null }.maxBy { it.exited }?.exited ?: 0f,
            maxNewValue
        ) + padding

        animator.start()
    }

    fun setSelectedListener(block: (String?) -> Unit) {
        selectionCallback = block
    }

    open class Action(val id: String, val entered: Float, val exited: Float, val tag: String) {

        var left: Float = 0f
        var top: Float = 0f
        var right: Float = 0f
        var bottom: Float = 0f

        open fun onMeasure(left: Float, top: Float, right: Float, bottom: Float) {
            this.left = left
            this.top = top
            this.right = right
            this.bottom = bottom
        }

        open fun draw(canvas: Canvas, temporary1: Paint) {
            canvas.drawRect(left, top, right, bottom, temporary1)
        }

        companion object {
            val Null = Action("", 0f, 0f, "")
        }
    }

}


