package com.makki.languageapp.utils

import android.view.animation.AnimationUtils
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makki.languageapp.R

/**
 * @author Maksym.Popovych
 */

@BindingAdapter("app:setAdapter")
fun RecyclerView.setAdapter(adapter: RecyclerView.Adapter<*>) {
    this.setHasFixedSize(true)
    this.layoutManager = LinearLayoutManager(context)
    this.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down)
    this.adapter = adapter
}
