package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.example.myapplication.model.Subject

class VideoCardPresenter(private val onClickCallback: (String) -> Unit) : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val mitem = item as Subject
        val title = mitem.title
        val imageUrl = mitem.cover
        val videoImage = viewHolder.view.findViewById<ImageView>(R.id.videoImage)
        val videoTitle = viewHolder.view.findViewById<TextView>(R.id.videoTitle)

        videoTitle.text = title
        Glide.with(viewHolder.view.context).load(imageUrl).into(videoImage)

        viewHolder.view.setOnClickListener {
            //Toast.makeText(viewHolder.view.context, "点击了: $title", Toast.LENGTH_SHORT).show()
            if (mitem.id == "tolist") {
                //打开新页面
                // 创建 Intent 来启动 ListActivity
                val context = viewHolder.view.context
                val intent = Intent(context, SourceActivity::class.java)
                // 通过 Intent.putExtra() 传递数据
                intent.putExtra("Title", mitem.title)
                intent.putExtra("Url", mitem.url)
                intent.putExtra("Desc", mitem.episodes_info)
                // intent.putExtra("VIDEO_TITLE", mitem.title) // 可以传递标题或其他信息
                context.startActivity(intent) // 使用上下文启动活动

            } else {
                onClickCallback(title) // 调用回调并传递标题
            }
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        // 清理资源
    }
}
