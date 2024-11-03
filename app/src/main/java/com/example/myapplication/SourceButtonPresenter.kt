package com.example.myapplication

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.leanback.widget.Presenter
import com.example.myapplication.model.Item

class SourceButtonPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val button = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_source_button, parent, false) as Button
        return ViewHolder(button)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val button = viewHolder.view as Button
        val ditem = item as Item

        // 获取屏幕宽度的三分之一
        val maxWidth = getScreenWidthThird(viewHolder.view.context)
        button.maxWidth = maxWidth
        button.text = ditem.title

        button.setOnClickListener {
            // 点击事件处理，例如打开播放页面
            val context = viewHolder.view.context
            val videoUrl = ditem.url
            val videoTitle = ditem.title
            VideoPlayerActivity.start(
                context = context,
                videoUrl = videoUrl,
                showTitle = videoTitle,
                showDescription = ditem.desc
            )
        }

        // 设置按钮的焦点变化监听
        button.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                button.isSelected = true // 开始滚动
                button.requestFocus() // 确保按钮获得焦点
            } else {
                button.isSelected = false // 停止滚动
                button.clearAnimation() // 清除动画
            }
        }
    }

    private fun getScreenWidthThird(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        return screenWidth / 3
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        // 清理资源
    }
}
