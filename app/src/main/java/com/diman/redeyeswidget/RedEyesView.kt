package com.diman.redeyeswidget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class RedEyesView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FF0000")
        style = Paint.Style.FILL
    }
    
    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#55FF0000")
        style = Paint.Style.FILL
        maskFilter = android.graphics.BlurMaskFilter(30f, android.graphics.BlurMaskFilter.Blur.NORMAL)
    }

    private var openProgress = 0f
    private val path = Path()

    init {
        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1500
            interpolator = AccelerateDecelerateInterpolator()
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                openProgress = it.animatedValue as Float
                invalidate()
            }
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val w = width.toFloat()
        val h = height.toFloat()
        
        val eyeWidth = w * 0.35f
        val eyeHeight = eyeWidth * 0.5f * openProgress
        
        val leftX = w * 0.25f
        val rightX = w * 0.75f
        val centerY = h * 0.5f

        // Левый глаз
        drawEye(canvas, leftX, centerY, eyeWidth, eyeHeight)
        // Правый глаз
        drawEye(canvas, rightX, centerY, eyeWidth, eyeHeight)
    }

    private fun drawEye(canvas: Canvas, cx: Float, cy: Float, width: Float, height: Float) {
        if (height <= 0f) return
        
        path.reset()
        // Рисуем миндалевидный разрез глаза
        path.moveTo(cx - width / 2, cy)
        path.quadTo(cx, cy - height, cx + width / 2, cy)
        path.quadTo(cx, cy + height, cx - width / 2, cy)
        path.close()

        canvas.drawPath(path, glowPaint)
        canvas.drawPath(path, paint)
    }
}
