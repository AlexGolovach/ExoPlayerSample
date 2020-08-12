package com.renovavision.exoplayersimple

import android.app.PictureInPictureParams
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Rational
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_controller.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customPlayerView.initExoPlayer("https://i.imgur.com/7bMqysJ.mp4")

        initBtnFullScreen()
        initBtnRepeat()
        initBtnPip()
    }

    private fun initBtnFullScreen() {
        btnFullScreen.setOnClickListener {
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    btnFullScreen.setImage(R.drawable.ic_fullscreen)
                }
                else -> {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    btnFullScreen.setImage(R.drawable.ic_fullscreen_exit)
                }
            }
        }
    }

    private fun initBtnRepeat() {
        btnRepeat.setOnClickListener {
            customPlayerView.changeRepeatMode()
        }
    }

    private fun initBtnPip() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            btnPip.setOnClickListener {
                val pipParams = PictureInPictureParams.Builder()
                val display = windowManager.defaultDisplay
                val point = Point()

                display.getSize(point)
                pipParams.setAspectRatio(Rational(point.x, point.y))
                enterPictureInPictureMode(pipParams.build())
            }
        } else {
            btnPip.gone()
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        if (isInPictureInPictureMode) {
            controllersLayout.gone()
            trickPlayLayout.gone()
        } else {
            controllersLayout.visible()
            trickPlayLayout.visible()
        }

        customPlayerView.setPlayWhenReady(true)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> btnFullScreen.setImage(R.drawable.ic_fullscreen_exit)
            else -> btnFullScreen.setImage(R.drawable.ic_fullscreen)
        }
    }

    override fun onResume() {
        super.onResume()

        window.hideStatusBarAndNavigationBar()

        customPlayerView.setPlayWhenReady(true)
    }

    override fun onPause() {
        super.onPause()

        customPlayerView.setPlayWhenReady(false)
    }
}