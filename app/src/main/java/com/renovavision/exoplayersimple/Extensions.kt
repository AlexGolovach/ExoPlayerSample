package com.renovavision.exoplayersimple

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.exoplayer2.BasePlayer
import com.google.android.exoplayer2.Player

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

fun ImageView.setImage(@DrawableRes image: Int) {
    setImageDrawable(
        resources.getDrawable(
            image,
            context.theme
        )
    )
}

fun ImageView.setImageWithChangedColor(@DrawableRes image: Int, @ColorRes color: Int) {
    setImage(image)
    DrawableCompat.setTint(drawable, ContextCompat.getColor(context, color))
}

fun BasePlayer.changeRepeatMode() {
    repeatMode = when (repeatMode) {
        Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
        else -> Player.REPEAT_MODE_OFF
    }
}

fun Window.hideStatusBarAndNavigationBar() {
    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
}