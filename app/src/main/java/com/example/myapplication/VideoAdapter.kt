package com.example.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.animation.ObjectAnimator
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class VideoAdapter(private val context: Context, private val videoList: List<Pair<String, String>>) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.video_card, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val (title, imageUrl) = videoList[position]
        holder.videoTitle.text = title
        Glide.with(context).load(imageUrl).into(holder.videoImage)

        setupFocusAnimations(holder.itemView)
        holder.itemView.setOnClickListener {
            Log.d("VideoCard", "Clicked video index: $position")
        }
    }

    override fun getItemCount(): Int = videoList.size

    private fun setupFocusAnimations(view: View) {
        view.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // 获取焦点时放大
                ObjectAnimator.ofFloat(v, "scaleX", 1.0f, 1.1f).apply {
                    duration = 200
                    start()
                }
                ObjectAnimator.ofFloat(v, "scaleY", 1.0f, 1.1f).apply {
                    duration = 200
                    start()
                }
                v.setBackgroundResource(R.drawable.focused_background) // 自定义获得焦点的背景
            } else {
                // 失去焦点时恢复
                ObjectAnimator.ofFloat(v, "scaleX", 1.1f, 1.0f).apply {
                    duration = 200
                    start()
                }
                ObjectAnimator.ofFloat(v, "scaleY", 1.1f, 1.0f).apply {
                    duration = 200
                    start()
                }
                v.setBackgroundResource(android.R.color.transparent) // 恢复默认背景
            }
        }
    }

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val videoTitle: TextView = view.findViewById(R.id.videoTitle)
        val videoImage: ImageView = view.findViewById(R.id.videoImage)
    }
}
