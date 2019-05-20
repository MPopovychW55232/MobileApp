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
import com.makki.languageapp.lib.R
import com.makki.languageapp.lib.animators.FloatAnimator
import com.makki.languageapp.lib.utils.asColor
import com.makki.languageapp.lib.utils.safeSlice
import kotlin.math.*
import kotlin.random.Random

/**
 * @author Maksym.Popovych
 */
class ScheduleView : View, GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {
    private val animator = FloatAnimator().also {
        it.setDuration(350L)
        it.setUpdateListener { fraction ->
            maxValue += (maxTargetValue - maxValue) * fraction
            minValue += (minTargetValue - minValue) * fraction
            invalidate()
        }
    }

    private val gestureDetector = GestureDetector(context, this)
    private val scaleDetector = ScaleGestureDetector(context, this)

    /**MOCK DATA**/
    private val random = Random(System.currentTimeMillis())
    private val mainSource: List<Day> = ArrayList<Day>().also {
        for (i in 0..400) {
            if (i % 7 == 0 || i % 7 == 1) {
                it.add(Day.Null)
                continue
            }
            val msg = if (i % 7 == 6) {
                " • Day: ${400 - i}"
            } else {
                ""
            }

            //108 x 5mins till 9am

            it.add(Day(random.nextInt(108, 140) * 0.05f, random.nextInt(180, 200) * 0.05f, msg))
        }
    }

    private var main = mainSource

    private val textSize = 65f

    private val mainPaint = Paint().also {
        it.flags = Paint.ANTI_ALIAS_FLAG
        it.color = Color.GREEN
        it.alpha = 160
        it.style = Paint.Style.FILL
    }
    private val separatorPaint = Paint().also {
        it.flags = Paint.ANTI_ALIAS_FLAG
        it.color = R.color.colorDisabledFlat.asColor(context)
        it.strokeWidth = 2f
        it.textSize = textSize
    }

    private var tPath = Path()

    private val minVisibleCount = 7
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
            field = min(max(0f, value), width.toFloat() * (mainSource.size - (minVisibleCount.toFloat() * scale)) / (scale * minVisibleCount))
        }

    var maxValue = 0f
    var minValue = 0f

    var maxTargetValue = 0f
    var minTargetValue = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val visibleCountFloat = minVisibleCount * scale
        visibleCount = ceil(visibleCountFloat).roundToInt()
        val widthPerItem = width.toFloat() / visibleCountFloat

        val approxOffsetCount = offset / widthPerItem
        offsetCount = min(floor(approxOffsetCount).roundToInt(), mainSource.size - minVisibleCount)
        val xPosDelta = (approxOffsetCount - offsetCount) * widthPerItem

        val graphHeight = (maxValue - minValue)

        for (i in 0..main.size) {
            val xPos = width - i * widthPerItem + xPosDelta
            canvas.drawLine(xPos, height * 1f / 12f, xPos, height * 11f / 12f, separatorPaint)
        }

        fun drawActions(days: List<Day>, paint: Paint) {
            tPath.reset()
            for (i in 0..days.size) {
                val action = days.getOrNull(i) ?: continue
                if (action == Day.Null) {
                    continue
                }

                val xPos: Float
                val yBot: Float
                val yTop: Float
                if (widthPerItem <= 40f) {
                    xPos = width - i * widthPerItem + xPosDelta
                    yBot = height - ((action.entered - minValue) / graphHeight) * height
                    yTop = height - ((action.getEnd() - minValue) / graphHeight) * height

                    canvas.drawRect(xPos - widthPerItem + 2, yTop, xPos - 2, yBot, paint)
                } else {
                    xPos = width - i * widthPerItem + xPosDelta
                    yBot = height - ((action.entered - minValue) / graphHeight) * height

                    var yHeight = yBot
                    for (lesson in action.subLessons) {
                        var lessonHeight = lesson.duration / graphHeight * height
                        yHeight -= lessonHeight
                        if (lesson == Lesson.Null) {
                            continue
                        }

                        canvas.drawRect(xPos - widthPerItem + 2, yHeight, xPos - 2, yHeight + lessonHeight, paint)
                        if (lesson.tag.isNotEmpty() && separatorPaint.textSize >= 35f) {
                            canvas.drawText(lesson.tag, xPos - widthPerItem + 2, yHeight + separatorPaint.textSize, separatorPaint)
                        }
                    }
                }

                if (action.tag.isNotEmpty() && separatorPaint.textSize >= 6f) {
                    canvas.drawText(action.tag, xPos - widthPerItem + 2, height * 11f / 12f + textSize, separatorPaint)
                }
            }
        }

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
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        return true
    }

    private fun onCountChanged() {
        animator.cancel()
        val padding = 1.5f

        main = mainSource.safeSlice(offsetCount..visibleCount + offsetCount)
        minTargetValue = main.filter { it != Day.Null }.minBy { it.entered }?.entered
                ?: throw Exception()
        maxTargetValue = main.filter { it != Day.Null }.maxBy { it.getEnd() }?.getEnd()
                ?: throw Exception()

        minTargetValue -= padding
        maxTargetValue += padding
        animator.start()
    }

    private fun moveX(delta: Float) {
        offset += delta
        invalidate()
    }

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
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

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
    }

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

    class Day(val entered: Float, private val left: Float, val tag: String) {
        companion object {
            val Null = Day(0f, 0f, "")
        }

        val subLessons: List<Lesson>

        fun getEnd() : Float {
            return entered + subLessons.map { it.duration }.sum()
        }

        init {
            val list = ArrayList<Lesson>()
            var start = entered
            var lessonN = 1
            var i = 0
            while (start < left) {
                if (i % 2 == 0) {
                    val duration = 0.8f
                    list.add(Lesson(duration, lessonN))
                    start += 0.8f
                    lessonN++
                } else {
                    list.add(Lesson.Null)
                    start += 0.2f
                }
                i++
            }
            subLessons = list
        }
    }

    class Lesson(val duration: Float, index: Int) {
        val tag = "L$index"

        companion object {
            val Null = Lesson(0.2f, 0)
        }
    }

}


