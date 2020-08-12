package com.renovavision.exoplayersimple

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.widget.FrameLayout
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.custom_controller.view.*
import kotlinx.android.synthetic.main.custom_player_view.view.*

class CustomPlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), Player.EventListener {

    private lateinit var simpleExoPlayer: SimpleExoPlayer

    init {
        inflate(context, R.layout.custom_player_view, this)
    }

    fun initExoPlayer(videoUrl: String) {
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context)

        val mediaDataSourceFactory =
            DefaultDataSourceFactory(context, Util.getUserAgent(context, "exoPlayerSample"))

        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
            Uri.parse(videoUrl)
        )

        player_view.apply {
            player = simpleExoPlayer
            keepScreenOn = true
        }

        simpleExoPlayer.apply {
            prepare(mediaSource)
            playWhenReady = true
            addListener(this@CustomPlayerView)
        }
    }

    fun setPlayWhenReady(value: Boolean) {
        simpleExoPlayer.playWhenReady = value
    }

    fun changeRepeatMode() {
        simpleExoPlayer.changeRepeatMode()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING -> progress_bar.visible()
            Player.STATE_READY -> progress_bar.gone()
        }
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        when (repeatMode) {
            Player.REPEAT_MODE_OFF -> {
                btnRepeat.setImageWithChangedColor(R.drawable.ic_repeat, R.color.light_grey)
            }
            Player.REPEAT_MODE_ALL, Player.REPEAT_MODE_ONE -> {
                btnRepeat.setImageWithChangedColor(R.drawable.ic_repeat, R.color.red)
            }
        }
    }
}