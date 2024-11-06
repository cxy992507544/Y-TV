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
import com.example.myapplication.tool.Select

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

        Select.setFocusChange(holder.itemView)

        holder.itemView.setOnClickListener {
            Log.d("VideoCard", "Clicked video index: $position")
        }
    }

    override fun getItemCount(): Int = videoList.size

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val videoTitle: TextView = view.findViewById(R.id.videoTitle)
        val videoImage: ImageView = view.findViewById(R.id.videoImage)
    }
}
