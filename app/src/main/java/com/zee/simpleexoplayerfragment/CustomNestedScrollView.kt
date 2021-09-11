package com.upskill.userapp.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class CustomNestedScrollView(context: Context,attributeSet: AttributeSet?=null):NestedScrollView(context,attributeSet) {

    private var enableScrolling = true

     fun isEnableScrolling(): Boolean {
        return enableScrolling
    }

    fun setEnableScrolling(enableScrolling: Boolean) {
        this.enableScrolling = enableScrolling
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (isEnableScrolling())
        super.onInterceptTouchEvent(ev)
        else false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (isEnableScrolling()) super.onTouchEvent(ev)
        else false
    }

}