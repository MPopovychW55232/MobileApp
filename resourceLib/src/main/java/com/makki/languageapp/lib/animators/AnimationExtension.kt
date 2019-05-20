package com.makki.languageapp.lib.animators

import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.postDelayed

/**
 * @author Maksym.Popovych
 */

fun View.appearFor(timeMs: Long) {
  visibility = View.VISIBLE

  startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))

  postDelayed(timeMs) {
    startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_out))
    postDelayed(400) {
      visibility = View.INVISIBLE
    }

  }
}