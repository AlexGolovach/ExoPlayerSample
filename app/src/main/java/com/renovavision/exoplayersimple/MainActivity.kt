package com.renovavision.exoplayersimple

import android.app.PictureInPictureParams
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Point
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Rational
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_controller.*

class MainActivity : AppCompatActivity() {

    private lateinit var simpleExoPlayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initExoPlayer()
        initBtnFullScreen()
        initBtnRepeat()
        initBtnPip()
    }

    private fun initExoPlayer() {
        val videoUrl = Uri.parse("https://i.imgur.com/7bMqysJ.mp4")

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this)

        val mediaDataSourceFactory =
            DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoPlayerSample"))

        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
            videoUrl
        )

        player_view.apply {
            player = simpleExoPlayer
            keepScreenOn = true
        }

        simpleExoPlayer.apply {
            prepare(mediaSource)
            playWhenReady = true
            addListener(getPlayerEventListener())
        }
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
            simpleExoPlayer.changeRepeatMode()
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

        simpleExoPlayer.playWhenReady = true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> btnFullScreen.setImage(R.drawable.ic_fullscreen_exit)
            else -> btnFullScreen.setImage(R.drawable.ic_fullscreen)
        }
    }

    private fun getPlayerEventListener() = object : EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                STATE_BUFFERING -> progress_bar.visible()
                STATE_READY -> progress_bar.gone()
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            when (repeatMode) {
                REPEAT_MODE_OFF -> {
                    btnRepeat.setImageWithChangedColor(R.drawable.ic_repeat, R.color.light_grey)
                }
                REPEAT_MODE_ALL, REPEAT_MODE_ONE -> {
                    btnRepeat.setImageWithChangedColor(R.drawable.ic_repeat, R.color.red)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        window.hideStatusBarAndNavigationBar()

        simpleExoPlayer.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()

        simpleExoPlayer.playWhenReady = false
    }
}