package com.makki.languageapp.lib.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.PublishSubject

/**
 * @author Maksym.Popovych
 */
abstract class AbstractAdapter<HOLDER : AbstractAdapter.AbstractVH<ITEM>, ITEM>(private val factory: AbstractFactory<HOLDER, ITEM>) :
	RecyclerView.Adapter<HOLDER>() {

	var list: ArrayList<ITEM> = ArrayList()
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	val onClickSubject = PublishSubject.create<ITEM>()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER {
		return factory.createViewHolder(parent)
	}

	override fun onBindViewHolder(viewHolder: HOLDER, position: Int) {
		val item = list[position]
		viewHolder.onBind(list[position])
		viewHolder.itemView.setOnClickListener { if (!viewHolder.onClick()) onClickSubject.onNext(item) }
	}

	override fun getItemCount(): Int = list.size

	abstract class AbstractVH<ITEM>(itemView: View) : RecyclerView.ViewHolder(itemView) {
		abstract fun onBind(item: ITEM)
		open fun onClick(): Boolean = false
	}

	abstract class AbstractFactory<HOLDER : AbstractAdapter.AbstractVH<ITEM>, ITEM> {
		abstract fun createViewHolder(parent: ViewGroup): HOLDER
	}

    fun setAll(items: Collection<ITEM>) {
        list.clear()
		notifyItemRangeRemoved(0, list.size)
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun addAll(items: Collection<ITEM>) {
        val oldSize = list.size - 1
        list.addAll(items)
        notifyItemRangeChanged(oldSize, oldSize + items.size)
    }
}
