package com.zee.exoplayerfragment_lib

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.zee.exoplayerfragment_lib.databinding.FragmentExoPlayerBinding


class ExoPlayerFragment : Fragment() {

    private lateinit var exoPlayerBinding: FragmentExoPlayerBinding
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var playbackPosition = 0L
    private var currentWindow = 0
    private var fullscreen = false
    private var playBackSpeed = 0F

    private var playBackListener: PlayBackListener? = null
    private var oldHeight = 0
    private var oldWidth = 0

    companion object {
        const val TAG = "SimpleExoPlayerFragment"
    }

    private val exoTitleContainer: LinearLayout by lazy {
        exoPlayerBinding.player.findViewById(R.id.exo_controller_title_container) as LinearLayout
    }


    private val backButton: ImageButton by lazy {
        exoPlayerBinding.player.findViewById(R.id.exo_back_btn) as ImageButton
    }

    private val exoTitle: TextView by lazy {
        exoPlayerBinding.player.findViewById(R.id.exo_title) as TextView
    }

    private val exoSpeedBtn: ImageButton by lazy {
        exoPlayerBinding.player.findViewById(R.id.zee_speed) as ImageButton
    }

    private val fullScreenBtn: ImageView? by lazy {
        exoPlayerBinding.player.findViewById(R.id.exo_fullscreen_icon) as ImageView?
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exoPlayerBinding = FragmentExoPlayerBinding.inflate(inflater, container, false)
        initializePlayer()
        return exoPlayerBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton.setOnClickListener {
            if (!fullscreen)
                return@setOnClickListener
            fullscreen = false
            exitFullScreen()
        }


        exoSpeedBtn.setOnClickListener {

            val speedControllerBtmSheet = SpeedControllerBtmSheet(playBackSpeed)
            speedControllerBtmSheet.show(childFragmentManager, "speed_btm_sheet")

        }

        exoPlayerBinding.player.setControllerVisibilityListener {
            playBackListener?.isControllerVisible(it == View.VISIBLE)
        }




        fullScreenBtn?.setOnClickListener {

            if (!fullscreen) {
                setFullScreen()

            } else {
                exitFullScreen()

            }
        }
    }


    private fun exitFullScreen() {
        exoTitleContainer.visibility = View.INVISIBLE
        fullScreenBtn?.setImageResource(R.drawable.ic_fullscreen)
        showSystemUI()
        (activity as AppCompatActivity).apply {

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            if (Build.VERSION.SDK_INT >= VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(true)
            }

            supportActionBar?.show()
            val parentParams = exoPlayerBinding.root.layoutParams
            parentParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            parentParams.height = oldHeight
            exoPlayerBinding.root.layoutParams = parentParams
            fullscreen = false
            playBackListener?.onFullScreenChanged(fullscreen)
        }
    }


    private fun setFullScreen() {
        exoTitleContainer.isVisible = true
        fullScreenBtn?.setImageResource(R.drawable.ic_fullscreen_exit)
        hideSystemUI()
        (activity as AppCompatActivity).apply {
            supportActionBar?.hide()

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

            val parentParams = exoPlayerBinding.root.layoutParams
            parentParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            oldHeight = parentParams.height
            if (Build.VERSION.SDK_INT >= VERSION_CODES.R) {
                parentParams.height = windowManager.currentWindowMetrics.bounds.width()
            } else parentParams.height = windowManager.defaultDisplay.width

            exoPlayerBinding.root.layoutParams = parentParams
            fullscreen = true
            playBackListener?.onFullScreenChanged(fullscreen)

        }
    }


    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onResume() {
        super.onResume()
        player?.play()
    }

    private fun initializePlayer() {
        if (player == null) {
            player = SimpleExoPlayer.Builder(requireContext())
                .build()
                .also { exoPlayer ->
                    exoPlayerBinding.player.player = exoPlayer
                    exoPlayer.playWhenReady = playWhenReady
                    exoPlayer.addListener(listener)
                }
        }
    }


    fun setListener(listener: PlayBackListener) {
        initializePlayer()
        playBackListener = listener

    }


    fun playVideo(title: String = "", uri: Uri, position: Long = 0L) {
        exoTitle.text = title
        initializePlayer()
        val mediaItem = MediaItem.fromUri(uri)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.seekTo(position)
    }


    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }


    private val listener = object : Player.Listener {

        override fun onIsLoadingChanged(isLoading: Boolean) {
            super.onIsLoadingChanged(isLoading)
            playBackListener!!.isLoading(isLoading)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (playbackState == Player.STATE_READY)
                playBackListener!!.onVideoStarted()

            if (playbackState == Player.STATE_ENDED)
                playBackListener!!.onVideoEnded()


            if (playbackState == Player.STATE_BUFFERING)
                playBackListener!!.isBuffering(true)
            else playBackListener!!.isBuffering(false)


        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            playBackListener!!.onError(error.toString())
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            playBackListener!!.isVideoPlaying(isPlaying)
        }

        override fun onPositionDiscontinuity(
            oldPosition: Player.PositionInfo,
            newPosition: Player.PositionInfo,
            reason: Int
        ) {
            super.onPositionDiscontinuity(oldPosition, newPosition, reason)

            val seconds = newPosition.contentPositionMs.div(1000)
            playBackListener!!.onSeekTo(seconds)

        }


    }


    fun setSpeed(defSpeed: Float) {
        kotlin.runCatching {
            player?.apply {
                playBackSpeed = defSpeed
                setPlaybackSpeed(defSpeed)
            }
        }.onFailure {
            Log.d("failed", "setSpeed: $it")
        }
    }


    fun hideSystemUI() {
        (activity as AppCompatActivity).apply {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(window, exoPlayerBinding.root).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    private fun showSystemUI() {
        (activity as AppCompatActivity).apply {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(
                window,
                exoPlayerBinding.root
            ).show(WindowInsetsCompat.Type.systemBars())
        }
    }


    interface PlayBackListener {

        fun isVideoPlaying(boolean: Boolean)

        fun onFullScreenChanged(isFullScreen: Boolean)

        fun isControllerVisible(visible: Boolean)

        fun onVideoStarted()

        fun onError(error: String)

        fun onVideoEnded()

        fun onSeekTo(seconds: Long)

        fun isLoading(isLoading: Boolean)

        fun isBuffering(isBuffering: Boolean)

    }


    fun setFullScreen(fullScreen: Boolean) {
        if (fullScreen)
            setFullScreen()
        else exitFullScreen()
    }


    fun seekTo(position: Long) {
        player?.seekTo(position)
    }


    fun getVideoDuration(): Long {
        return player?.duration ?: 0L
    }
}