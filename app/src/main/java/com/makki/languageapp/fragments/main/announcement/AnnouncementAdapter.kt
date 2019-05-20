package com.makki.languageapp.fragments.main.announcement

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.makki.basic.model.AnnouncementAsset
import com.makki.basic.model.NotificationAsset
import com.makki.languageapp.R
import com.makki.languageapp.lib.adapter.AbstractAdapter
import com.makki.languageapp.lib.utils.asResourceString

/**
 * @author Maksym.Popovych
 */
class AnnouncementAdapter : AbstractAdapter<AnnouncementAdapter.VH, AnnouncementAsset>(Factory()) {

    class VH(itemView: View) : AbstractAdapter.AbstractVH<AnnouncementAsset>(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.title)
        private val textView: TextView = itemView.findViewById(R.id.text)
        private val authorView: TextView = itemView.findViewById(R.id.author)
        private val timestampView: TextView = itemView.findViewById(R.id.time)

        override fun onBind(item: AnnouncementAsset) {
            titleView.text = item.title
            textView.text = item.message
            authorView.text = item.senderDisplayName

            val dateString = DateUtils.getRelativeTimeSpanString(item.timestamp, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS)

            timestampView.text = R.string.notification_timestamp_format.asResourceString(itemView.context, dateString)
        }
    }

    class Factory : AbstractAdapter.AbstractFactory<VH, AnnouncementAsset>() {
        override fun createViewHolder(parent: ViewGroup): VH {
            val layout = R.layout.vh_announcement
            val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            return VH(view)
        }
    }

}
