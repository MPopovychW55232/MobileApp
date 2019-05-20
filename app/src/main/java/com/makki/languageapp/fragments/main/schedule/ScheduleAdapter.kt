package com.makki.languageapp.fragments.main.schedule

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.makki.basic.model.LessonAsset
import com.makki.languageapp.R
import com.makki.languageapp.lib.adapter.AbstractAdapter
import org.joda.time.format.DateTimeFormat
import java.util.concurrent.TimeUnit

/**
 * @author Maksym.Popovych
 */
class ScheduleAdapter : AbstractAdapter<ScheduleAdapter.VH, LessonAsset>(Factory()) {

    companion object {
        val formatter = DateTimeFormat.forPattern("hh:mm")
    }

    class VH(itemView: View) : AbstractAdapter.AbstractVH<LessonAsset>(itemView) {
        private val timestampView: TextView = itemView.findViewById(R.id.time)
        private val titleView: TextView = itemView.findViewById(R.id.title)
        private val textView: TextView = itemView.findViewById(R.id.text)
        private val durationView: TextView = itemView.findViewById(R.id.duration)

        override fun onBind(item: LessonAsset) {
            titleView.text = item.subjectName
            textView.text = item.extra
            durationView.text = "(${TimeUnit.MILLISECONDS.toMinutes(item.duration)} min)"

            timestampView.text = formatter.print(item.start)
        }
    }

    class Factory : AbstractAdapter.AbstractFactory<VH, LessonAsset>() {
        override fun createViewHolder(parent: ViewGroup): VH {
            val layout = R.layout.vh_schedule
            val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            return VH(view)
        }
    }

}