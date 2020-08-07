package com.renovavision.exoplayersimple

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.*
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
        bt_fullscreen.setOnClickListener {
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    bt_fullscreen.setImageDrawable(
                        resources.getDrawable(
                            R.drawable.ic_fullscreen,
                            theme
                        )
                    )
                }
                else -> {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    bt_fullscreen.setImageDrawable(
                        resources.getDrawable(
                            R.drawable.ic_fullscreen_exit,
                            theme
                        )
                    )
                }
            }
        }
    }

    private fun getPlayerEventListener() = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING -> progress_bar.visible()
                Player.STATE_READY -> progress_bar.gone()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        simpleExoPlayer.playWhenReady = false
    }

    override fun onRestart() {
        super.onRestart()

        simpleExoPlayer.playWhenReady = true
    }
}