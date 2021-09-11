package com.zee.exoplayerfragment_lib

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zee.exoplayerfragment_lib.databinding.VideoSpeedControllerBtmSheetBinding


class SpeedControllerBtmSheet(var speed: Float = 0F) : BottomSheetDialogFragment() {

    private lateinit var binding: VideoSpeedControllerBtmSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onStart() {
        super.onStart()
        //this forces the sheet to appear at max height even on landscape
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
//        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
//                    behavior.state = BottomSheetBehavior.STATE_EXPANDED;
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//
//            }
//        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = VideoSpeedControllerBtmSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDefaultCheckBox(speed)

        binding.apply {



            speedN025X.setOnClickListener {
                onSpeedSelected(0.25F)
            }

            speedN050X.setOnClickListener {
                onSpeedSelected(0.5F)
            }

            speed075X.setOnClickListener {
                onSpeedSelected(0.75F)
            }

            speedNormal.setOnClickListener {
                onSpeedSelected(1F)
            }

            speed15X.setOnClickListener {
                onSpeedSelected(1.5F)
            }


            speed2X.setOnClickListener {
                onSpeedSelected(2F)
            }

            speed25X.setOnClickListener {
                onSpeedSelected(2.5F)
            }


        }
    }


    private fun checkDefSpeedBox(defSpeed: Float) {
        when (defSpeed) {
            0.25F -> binding.speedN025XImg.visibility = View.VISIBLE
            0.5F -> binding.speedN050XImg.visibility = View.VISIBLE
            0.75F -> binding.speed075XImg.visibility = View.VISIBLE
            1.5F -> binding.speed15XImg.visibility = View.VISIBLE
            2F -> binding.speed2XImg.visibility = View.VISIBLE
            2.5F -> binding.speed25XImg.visibility = View.VISIBLE
            else -> binding.speedNormalImg.visibility = View.VISIBLE
        }
    }


    private fun unnCheckBox(defSpeed: Float) {
        when (defSpeed) {
            0.25F -> binding.speedN025XImg.visibility = View.INVISIBLE
            0.5F -> binding.speedN050XImg.visibility = View.INVISIBLE
            0.75F -> binding.speed075XImg.visibility = View.INVISIBLE
            1.5F -> binding.speed15XImg.visibility = View.INVISIBLE
            2F -> binding.speed2XImg.visibility = View.INVISIBLE
            2.5F -> binding.speed25XImg.visibility = View.INVISIBLE
            else -> binding.speedNormalImg.visibility = View.INVISIBLE
        }
    }


    private fun onSpeedSelected(speed: Float) {
        unnCheckBox(this.speed)
        this.speed = speed
        setSpeed()
        dismiss()

    }

    private fun setSpeed() {
        runCatching {
            (parentFragment as ExoPlayerFragment).apply {
                setSpeed(speed)
            }
        }.onFailure {
            Log.d("failed", "onDismiss: $it")
        }
    }

    fun setDefaultCheckBox(playBackSpeed: Float) {
        Log.d("fail", "setDefaultCheckBox: $playBackSpeed")
        checkDefSpeedBox(playBackSpeed)
    }


    override fun dismiss() {
        super.dismiss()
        (parentFragment as ExoPlayerFragment).apply{
            hideSystemUI()
        }
    }
}