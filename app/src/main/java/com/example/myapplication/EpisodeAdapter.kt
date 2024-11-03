package com.example.myapplication

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.File

class EpisodeAdapter(
    private val onEpisodeClick: (File) -> Unit
) : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    private var episodeList: List<File> = emptyList()
    private var lastSelectedPosition: Int = -1  // 保存最后被点击项的位置

    fun submitList(episodes: List<File>) {
        episodeList = episodes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_episode, parent, false)
        return EpisodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = episodeList[position]
        holder.bind(episode, position == lastSelectedPosition)

        // 点击事件：更新样式并通知列表更新
        holder.itemView.setOnClickListener {
            // 更新最后选择的项
            val previousPosition = lastSelectedPosition
            lastSelectedPosition = holder.bindingAdapterPosition

            // 更新被点击的项
            notifyItemChanged(previousPosition)
            notifyItemChanged(lastSelectedPosition)

            // 触发点击回调
            onEpisodeClick(episode)
        }
    }

    override fun getItemCount() = episodeList.size

    inner class EpisodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val episodeTextView: TextView = itemView.findViewById(R.id.tvEpisode)

        init {
            itemView.isFocusable = true // 确保项可聚焦
            itemView.isFocusableInTouchMode = true

            // 设置项获得焦点时的边框样式和放大效果
            itemView.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    view.scaleX = 1.1f
                    view.scaleY = 1.1f
                    view.background = ContextCompat.getDrawable(view.context, R.drawable.newborder)
                } else {
                    view.scaleX = 1.0f
                    view.scaleY = 1.0f
                    view.background = null
                }
            }
        }

        fun bind(episode: File, isSelected: Boolean) {
            // 获取屏幕宽度的三分之一
            val maxWidth = getScreenWidthThird(itemView.context)
            episodeTextView.text = episode.name
            episodeTextView.maxWidth = maxWidth

            // 更新背景颜色和文字颜色
            if (isSelected) {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.selected_background))
                episodeTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.selected_text))
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.default_background))
                episodeTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.default_text))
            }
        }

        private fun getScreenWidthThird(context: Context): Int {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getRealMetrics(displayMetrics)
            val screenWidth = displayMetrics.widthPixels
            return screenWidth / 3
        }
    }
}
