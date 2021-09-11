package com.zee.simpleexoplayerfragment

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.zee.exoplayerfragment_lib.ExoPlayerFragment
import com.zee.simpleexoplayerfragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ExoPlayerFragment.PlayBackListener {


    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var isFullScreen = false
    private var playerFragment: ExoPlayerFragment? = null


    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        playerFragment =
            supportFragmentManager.findFragmentById(R.id.player_fragment) as ExoPlayerFragment
        playerFragment?.setListener(this)
        playerFragment?.playVideo(
            title = "video",
            "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4".toUri()
        )

    }

    override fun isVideoPlaying(boolean: Boolean) {
        Log.d(TAG, "isVideoPlaying: playing $boolean")
    }


    override fun onFullScreenChanged(isFullScreen: Boolean) {
        this.isFullScreen = isFullScreen
        binding.scrollView.setEnableScrolling(!isFullScreen)
    }

    override fun isControllerVisible(visible: Boolean) {
        Log.d(TAG, "isControllerVisible: $visible")
    }

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed: full $isFullScreen")
        if (isFullScreen)
            playerFragment?.setFullScreen(false)
        else super.onBackPressed()

    }

}