package com.makki.languageapp.tests

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import com.makki.basic.model.MovementAsset
import com.makki.languageapp.lib.base.BaseActivity
import com.makki.languageapp.lib.views.TimeGraphView
import com.makki.services.api.managers.MovementManager
import io.reactivex.android.schedulers.AndroidSchedulers
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit
import kotlin.math.min

/**
 * @author Maksym.Popovych
 */
class TestActivity : BaseActivity() {

   lateinit var view: TimeGraphView
    private val manager: MovementManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        view = TimeGraphView(this)
        view.setSelectedListener { id ->
            Log.e("TEST", "Selected id: $id")
        }
        view.minVisibleCount = 3

        manager.getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .toList()
            .subscribe { list ->
                view.mainSource = list.map { it.toAction() }
            }

        setContentView(view)
    }

    fun MovementAsset.toAction(): TimeGraphView.Action {
        if (this == MovementAsset.Null) return TimeGraphView.Action.Null

        val enterDate = DateTime(entered)
        val dayStart = enterDate.withTimeAtStartOfDay().millis
        val entered = (enterDate.millis - dayStart).toFloat() / TimeUnit.DAYS.toMillis(1) * 24f
        val left = (min(left, dayStart + TimeUnit.DAYS.toMillis(1) - 1) - dayStart).toFloat() / TimeUnit.DAYS.toMillis(1) * 24f

        return ScheduleAction(id, entered, left, DateTimeFormat.forPattern("\u2022 dd/MM").print(enterDate))
    }

}

class ScheduleAction(id: String, entered: Float, exited: Float, tag: String) : TimeGraphView.Action(id, entered, exited, tag) {

    private var lessons = emptyList<Lesson>()

    init {
        val list = ArrayList<Lesson>()
        var currentTime = entered
        while (currentTime <= exited) {
            list.add(Lesson(true, 45f / 60f))
            list.add(Lesson(false, 15f / 60f))
            currentTime += 1f
        }
        lessons = list
    }

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

    private class Lesson(val valid: Boolean, val duration: Float)

}