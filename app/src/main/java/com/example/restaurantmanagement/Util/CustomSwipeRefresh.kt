package com.example.restaurantmanagement.Util

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.view.MotionEventCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class CustomSwipeRefresh (context: Context, attrs: AttributeSet) :
    SwipeRefreshLayout(context, attrs) {

    private var startX: Float = 0f
    private var startY: Float = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (MotionEventCompat.getActionMasked(ev)) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x
                startY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val xDiff = Math.abs(ev.x - startX)
                val yDiff = Math.abs(ev.y - startY)

                // Only intercept the touch event if the vertical swipe gesture is larger than the horizontal one
                return yDiff > xDiff
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}