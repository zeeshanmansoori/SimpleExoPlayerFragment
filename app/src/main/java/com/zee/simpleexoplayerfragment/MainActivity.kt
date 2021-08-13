package com.zee.simpleexoplayerfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import com.zee.exoplayerfragment_lib.ExoPlayerFragment
import com.zee.simpleexoplayerfragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ExoPlayerFragment.PlayBackListener {


    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var playerFragment: ExoPlayerFragment? = null


    companion object{
        const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        playerFragment = supportFragmentManager.findFragmentById(R.id.player_fragment) as ExoPlayerFragment
        playerFragment?.setListener(this)
        playerFragment?.playVideo("video uri".toUri())

    }

    override fun isVideoPlaying(boolean: Boolean) {
        Log.d(TAG, "isVideoPlaying: playing $boolean")
    }


    override fun onFullScreenChanged(isFullScreen: Boolean) {
        Log.d(TAG, "onFullScreenChanged: fullscreen $isFullScreen")
    }

}