package com.makki.languageapp.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputLayout
import com.makki.languageapp.R
import com.makki.languageapp.lib.animators.appearFor
import com.makki.languageapp.lib.views.TimeGraphView

/**
 * @author Maksym.Popovych
 */

@BindingAdapter("app:visibleOrGone")
fun View.visibleOrGone(value: Boolean) {
    visibility = if (value) View.VISIBLE else View.GONE
}

@BindingAdapter("app:visibleOrNot")
fun View.visibleOrNot(value: Boolean) {
    visibility = if (value) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("app:onFocusChanged")
fun View.onFocusChanged(listener: FocusChangeListener) {
    this.setOnFocusChangeListener(listener::onChanged)
}

interface FocusChangeListener {
    fun onChanged(view: View, focused: Boolean)
}

@BindingAdapter("app:errorHint")
fun TextInputLayout.setErrorHint(text: String) {
    this.error = text
}

@BindingAdapter("app:showNotice")
fun View.showNotice(show: Boolean) {
    if (!show) return
    this.appearFor(4000)
}

@BindingAdapter("app:activatedOrNot")
fun View.activatedOrNot(active: Boolean) {
    isActivated = active
}

@BindingAdapter("app:loadImage")
fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .error(R.drawable.background_avatar)
        .placeholder(R.drawable.background_avatar)
        .centerCrop()
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}

@BindingAdapter("app:mainGraphData")
fun TimeGraphView.loadData(list: List<TimeGraphView.Action>) {
    this.mainSource = list
}

@BindingAdapter("app:secondaryGraphData")
fun TimeGraphView.loadSecondData(list: List<TimeGraphView.Action>) {
    this.secondarySource = list
}

@BindingAdapter("app:onDateSelection")
fun TimeGraphView.onDateSelection(callback: OnDateSelection) {
    this.setSelectedListener {
        callback.onSelected(it)
    }
}

@BindingAdapter("app:setSelection")
fun TimeGraphView.setSelection(id: String) {
    this.selectedId = id
}

interface OnDateSelection {
    fun onSelected(id: String?)
}

@BindingAdapter("app:minCount")
fun TimeGraphView.setMinimalCount(count: Int) {
    this.minVisibleCount = count
}