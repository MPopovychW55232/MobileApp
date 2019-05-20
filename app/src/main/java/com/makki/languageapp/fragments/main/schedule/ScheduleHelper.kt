package com.makki.languageapp.fragments.main.schedule

import android.graphics.Canvas
import android.graphics.Paint
import com.makki.basic.model.LessonAsset
import com.makki.languageapp.lib.views.TimeGraphView
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.concurrent.TimeUnit
import kotlin.math.min

/**
 * @author Maksym.Popovych
 */
object ScheduleHelper {

    val formatter = DateTimeFormat.forPattern("dd/MM")

    fun processToDayActions(all: List<LessonAsset>): List<TimeGraphView.Action> {
        val map = all.groupBy { item -> formatter.print(item.start) }

        var safeIndex = 0
        val result = ArrayList<TimeGraphView.Action>()
        var beginning = all.minBy { it.start }?.start ?: return emptyList()

        while (safeIndex < all.size) {
            val key = formatter.print(beginning)
            val list = map[key]
            if (list == null) {
                beginning += TimeUnit.DAYS.toMillis(1)
                result.add(TimeGraphView.Action.Null)
                continue
            } else {
                val start = list.minBy { it.start }?.start ?: break
                val end = list.maxBy { it.start + it.duration }?.let { it.start + it.duration } ?: break

                val enterDate = DateTime(start)
                val dayStart = enterDate.withTimeAtStartOfDay().millis
                val entered = (enterDate.millis - dayStart).toFloat() / TimeUnit.DAYS.toMillis(1) * 24f
                val left = (min(
                    end,
                    dayStart + TimeUnit.DAYS.toMillis(1) - 1
                ) - dayStart).toFloat() / TimeUnit.DAYS.toMillis(1) * 24f


                val subList = ArrayList<Lesson>()
                for (i in 0 until list.size) {
                    val record = list[i]
                    val next = list.getOrNull(i + 1)
                    val gap = !(next != null && (next.start - record.duration) == record.start)

                    subList += Lesson(
                        true,
                        record.duration.toFloat() / TimeUnit.DAYS.toMillis(1) * 24f
                    )

                    if (gap && next != null) {
                        subList += Lesson(
                            false,
                            (next.start - (record.start + record.duration)).toFloat() / TimeUnit.DAYS.toMillis(1) * 24f
                        )
                    }
                }
                val day = LessonDayAction(subList, dayStart.toString(), entered, left, "\u2022 $key")
                result.add(day)
            }

            beginning += TimeUnit.DAYS.toMillis(1)
            safeIndex += list.size
        }
        return result.asReversed()
    }

}

class LessonDayAction(private val lessons: List<Lesson>, id: String, entered: Float, exited: Float, tag: String) :
    TimeGraphView.Action(id, entered, exited, tag) {

    override fun draw(canvas: Canvas, temporary1: Paint) {
        if (right - left <= 30f || lessons.isEmpty()) {
            super.draw(canvas, temporary1)
        } else {
            val duration = exited - entered
            val heightInternal = bottom - top
            var newYBot = top
            for (i in 0 until lessons.size) {
                val lesson = lessons[i]
                val newTop = newYBot
                newYBot += (lesson.duration / duration) * heightInternal

                if (!lesson.valid) continue
                canvas.drawRect(left, newTop, right, newYBot, temporary1)
            }
        }
    }
}

class Lesson(val valid: Boolean, val duration: Float)