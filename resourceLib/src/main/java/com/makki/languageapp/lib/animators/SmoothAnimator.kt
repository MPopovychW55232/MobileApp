package com.makki.languageapp.lib.animators

import android.view.Choreographer
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator

/**
 * @author Maksym.Popovych
 */
abstract class SmoothAnimator : Choreographer.FrameCallback {

  var isRunning: Boolean = false
    private set

  var duration: Long = 500L
    private set

  private var time: Long = 0L

  private var endCallback: ((SmoothAnimator) -> Unit)? = null
  private var startCallback: ((SmoothAnimator) -> Unit)? = null
  private var interpolator: Interpolator = AccelerateDecelerateInterpolator()

  override fun doFrame(frameTimeNanos: Long) {
    if (time == 0L) {
      startCallback?.invoke(this)
      time = frameTimeNanos
    }

    val fraction: Float
    val finished: Boolean

    val diff = (frameTimeNanos - time) / 1000000f
    if (diff >= duration) {
      finished = true
      fraction = 1f
    } else {
      finished = false
      fraction = diff / duration
    }

    onUpdate(interpolator.getInterpolation(fraction))

    if (finished) {
      isRunning = false
      endCallback?.invoke(this)
    } else {
      Choreographer.getInstance().postFrameCallback(this)
    }
  }

  abstract fun onUpdate(fraction: Float)

  fun setInterpolator(interpolator: Interpolator): SmoothAnimator {
    this.interpolator = interpolator
    return this
  }

  fun setStartListener(block: (SmoothAnimator) -> Unit): SmoothAnimator {
    startCallback = block
    return this
  }

  fun setEndListener(block: (SmoothAnimator) -> Unit): SmoothAnimator {
    endCallback = block
    return this
  }

  fun setDuration(millis: Long): SmoothAnimator {
    duration = millis
    return this
  }

  open fun start(): SmoothAnimator {
    if (!isRunning) {
      Choreographer.getInstance().postFrameCallback(this)
    }
    time = 0
    isRunning = true
    return this
  }

  open fun cancel(): SmoothAnimator {
    if (isRunning) {
      isRunning = false
      Choreographer.getInstance().removeFrameCallback(this)
    }
    return this
  }

}