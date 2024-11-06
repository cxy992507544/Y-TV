package com.example.myapplication.tool

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation

// 此类为动画类的选择动画类，用于对控件选中/取消选中进行样式动画切换
object Select {

    private const val ANIMATION_SCALE = 1.2F  // 缩放比例
    private const val ANIMATION_TIME = 230L  // 动画时长
    private const val D_S_COLOR = "#bd7ed9"  // 选中边框色
    private const val D_S_B_COLOR = "#74628b"  // 选中背景色

    // 保存初始状态
    private var initialBackground: Drawable? = null
    private var initialScaleX = 1.0f
    private var initialScaleY = 1.0f

    private fun createScaleAnimation(fromScale: Float, toScale: Float): ScaleAnimation {
        return ScaleAnimation(
            fromScale, toScale, fromScale, toScale,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = ANIMATION_TIME
            fillAfter = true  // 动画结束后保持状态
        }
    }

    // 放大动画
    private fun zoomIn(view: View) {
        val animation = createScaleAnimation(1.0f, ANIMATION_SCALE)
        view.clearAnimation()
        view.startAnimation(animation)
    }

    // 缩小动画
    private fun zoomOut(view: View) {
        val animation = createScaleAnimation(ANIMATION_SCALE, 1.0f)
        view.clearAnimation()
        view.startAnimation(animation)
    }

    // 创建带边框的背景
    private fun createBorderDrawable(color: String): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.parseColor(D_S_B_COLOR))
            setStroke(4, Color.parseColor(color))
            cornerRadius = 8f
        }
    }

    /**
     * 给控件设置获取焦点动画并添加默认的边框颜色（与选中边框的颜色）
     **/
    fun setFocusChange(view: View) {
        // 使控件可以获取焦点
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        //判断其是否有父级 有则将其父级别的裁剪超出部分功能关闭
        if (view.parent != null) {
            // 父视图存在，可以强制转换为 ViewGroup 进行操作
            val parentLayout = view.parent as ViewGroup
            parentLayout.clipChildren = false
            parentLayout.clipToPadding = false
        }


        view.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // 第一次获取焦点时保存初始状态
                if (initialBackground == null) {
                    initialBackground = v.background
                    initialScaleX = v.scaleX
                    initialScaleY = v.scaleY
                }
                // 放大动画
                zoomIn(v)
                // 设置选中边框颜色，但保持背景色
                v.background = createBorderDrawable(D_S_COLOR)  // 确保这里只设置边框，不改变背景
            } else {
                // 缩小动画
                zoomOut(v)
                // 还原初始背景和缩放比例
                v.background = initialBackground
                v.scaleX = initialScaleX
                v.scaleY = initialScaleY
            }
        }
    }



}
