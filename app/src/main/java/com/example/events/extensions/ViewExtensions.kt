package com.example.events.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator

fun View.showComponent() {
    this.visibility = View.VISIBLE

}

fun View.hideComponent() {
    this.visibility = View.GONE
}

fun View.animateView(oa1Duration: Long, oa2Duration: Long) {
    val oa1 = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0f)
    val oa2 = ObjectAnimator.ofFloat(this, "scaleX", 0f, 1f)

    oa1.interpolator = DecelerateInterpolator()
    oa2.interpolator = AccelerateDecelerateInterpolator()

    oa1.duration = oa1Duration
    oa2.duration = oa2Duration

    oa1.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            oa2.start()
        }

    })
    oa1.start()

}