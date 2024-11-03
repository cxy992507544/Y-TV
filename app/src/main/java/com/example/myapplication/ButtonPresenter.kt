package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.widget.Presenter

class ButtonPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        (viewHolder.view as TextView).text = item as String
        viewHolder.view.setOnClickListener {
            // 搜索按钮点击逻辑
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        // 清理资源
    }
}
