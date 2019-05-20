package com.makki.languageapp.fragments.main.movement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.makki.basic.model.MovementRecord
import com.makki.languageapp.R
import com.makki.languageapp.lib.adapter.AbstractAdapter
import org.joda.time.format.DateTimeFormat

/**
 * @author Maksym.Popovych
 */
class MovementAdapter : AbstractAdapter<MovementAdapter.VH, MovementRecord>(Factory()) {

    companion object {
        val dateFormat = DateTimeFormat.forPattern("HH:mm")
    }

    class VH(itemView: View) : AbstractAdapter.AbstractVH<MovementRecord>(itemView) {
        private val timeView: TextView = itemView.findViewById(R.id.time)
        private val gateView: TextView = itemView.findViewById(R.id.gate)
        private val direction: ImageView = itemView.findViewById(R.id.direction)

        override fun onBind(item: MovementRecord) {
            timeView.text = dateFormat.print(item.timestamp)
            gateView.text = item.gate

            val drawable = if (item.entered) {
                R.drawable.ic_movement_enter
            } else {
                R.drawable.ic_movement_leave
            }
            direction.setImageResource(drawable)
        }
    }

    class Factory : AbstractAdapter.AbstractFactory<VH, MovementRecord>() {
        override fun createViewHolder(parent: ViewGroup): VH {
            val layout = R.layout.vh_movement
            val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            return VH(view)
        }
    }

}
