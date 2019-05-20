package com.makki.languageapp.utils

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.PublishSubject

/**
 * @author Maksym.Popovych
 */
abstract class BindAdapter<MODEL: ViewModel, HOLDER : BindAdapter.AbstractVH<MODEL, ITEM>, ITEM>(
	private val viewModel: MODEL,
	private val factory: AbstractFactory<HOLDER>,
	@LayoutRes val layoutId: Int
) :
	RecyclerView.Adapter<HOLDER>() {

	var list: List<ITEM> = emptyList()
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	val onClickSubject = PublishSubject.create<ITEM>()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER {
		return factory.createViewHolder(parent, viewType)
	}

	override fun getItemViewType(position: Int): Int {
		return layoutId
	}

	override fun onBindViewHolder(viewHolder: HOLDER, position: Int) {
		val item = list[position]
		viewHolder.onBind(viewModel, item, position)
		viewHolder.itemView.setOnClickListener { if (!viewHolder.onClick()) onClickSubject.onNext(item) }
	}

	override fun getItemCount(): Int = list.size

	abstract class AbstractVH<MODEL: ViewModel, ITEM>(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
		abstract fun onBind(model: MODEL, item: ITEM, position: Int)
		open fun onClick(): Boolean = false
	}

	abstract class AbstractFactory<HOLDER : AbstractVH<*, *>> {
		abstract fun createViewHolder(parent: ViewGroup, viewType: Int): HOLDER
	}

}
