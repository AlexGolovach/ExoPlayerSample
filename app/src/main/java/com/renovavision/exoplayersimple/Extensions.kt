package com.renovavision.exoplayersimple

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

fun View.visible() {
    if (visibility != VISIBLE) {
        visibility = VISIBLE
    }
}

fun View.gone() {
    if (visibility != GONE) {
        visibility = GONE
    }
}