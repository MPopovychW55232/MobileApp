package com.makki.languageapp.fragments.main.grades

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.makki.basic.model.GradePeriodAsset
import com.makki.languageapp.R
import com.makki.languageapp.lib.adapter.AbstractAdapter
import com.makki.languageapp.views.HorizontalSeparatorView
import com.makki.languageapp.views.SubjectHeaderView
import com.makki.languageapp.views.SubjectView
import org.joda.time.format.DateTimeFormat

/**
 * @author Maksym.Popovych
 */
class GradesAdapter : AbstractAdapter<GradesAdapter.VH, GradePeriodAsset>(Factory()) {

    class VH(itemView: View) : AbstractAdapter.AbstractVH<GradePeriodAsset>(itemView) {
        private val content: LinearLayout = itemView.findViewById(R.id.content)
        private val titleView: TextView = itemView.findViewById(R.id.title)
        private val timestampView: TextView = itemView.findViewById(R.id.time)

        @SuppressLint("SetTextI18n")
        override fun onBind(item: GradePeriodAsset) {
            val date = DateTimeFormat.forPattern("dd/MM/yyyy")

            titleView.text = item.periodName
            timestampView.text = "\u2022 ${date.print(item.start)} - ${date.print(item.end)}"

            content.removeAllViews()
            content.addView(SubjectHeaderView(content.context))
            item.grades.forEachIndexed { index, gradeAsset ->
                content.addView(HorizontalSeparatorView(content.context))
                content.addView(SubjectView(gradeAsset, content.context))
            }
        }
    }

    class Factory : AbstractAdapter.AbstractFactory<VH, GradePeriodAsset>() {
        override fun createViewHolder(parent: ViewGroup): VH {
            val layout = R.layout.vh_period
            val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            return VH(view)
        }
    }

}
