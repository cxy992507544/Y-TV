package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.leanback.widget.Presenter
import com.example.myapplication.diy.CustomEditText
import com.example.myapplication.model.Item
import com.example.myapplication.model.Subject
import com.example.myapplication.network.ChenApi
import com.example.myapplication.tool.Select
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SearchPresenter(private val fragment: MainFragment) : Presenter() {

    private lateinit var searchEditText: CustomEditText
    private lateinit var searchButton: TextView
    private lateinit var loadingSpinner: ProgressBar
    private lateinit var backgg: Drawable

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        searchEditText = viewHolder.view.findViewById(R.id.searchBox)
        val restButton = viewHolder.view.findViewById<Button>(R.id.restButton)
        val testButton = viewHolder.view.findViewById<Button>(R.id.testButton)
        val rsButton = viewHolder.view.findViewById<Button>(R.id.rsButton)
        searchButton = viewHolder.view.findViewById(R.id.searchButton)
        loadingSpinner = viewHolder.view.findViewById(R.id.loadingSpinner)

//        testButton.visibility = View.GONE // 隐藏跳转按钮

//        searchEditText.isFocusable = true
//        searchEditText.isFocusableInTouchMode = true


        Select.setFocusChange(restButton)
        Select.setFocusChange(testButton)
        Select.setFocusChange(rsButton)
        Select.setFocusChange(searchButton)
        Select.setFocusChange(searchEditText)



        // 输入框失去焦点时操作
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && searchEditText.text.isBlank()) {
                clearSearchBox()
            }
        }
        //刷新按钮
        rsButton.setOnClickListener {
            Toast.makeText(searchEditText.context, "正在为您刷新导航剧集~", Toast.LENGTH_SHORT).show()
            fragment.setupVideoRows()
        }

        // 跳转按钮点击事件
        testButton.setOnClickListener {
            val context = viewHolder.view.context
            val videoUrl = "/tdown/18576742.html"
            val videoTitle = "夏目友人帐"
            VideoPlayerActivity.start(
                context = context,
                videoUrl = videoUrl,
                showTitle = videoTitle,
                showDescription = "【迦南学院篇】三年之约后，萧炎终于在迦南学院见到了薰儿，此后他广交挚友并成立磐门；为继续提升实力以三上云岚宗为父复仇，他以身犯险深入天焚炼气塔吞噬陨落心炎……"
            )
        }

        // 重置按钮点击事件
        restButton.setOnClickListener {
            clearSearchBox()
            fragment.updateVideoRows(emptyList(), false)
        }

        // 搜索按钮点击事件
        searchButton.setOnClickListener { performSearch() }
    }


    private fun performSearch() {
        searchButton.requestFocus()
        val query = searchEditText.text.toString()
        if (query.isNotBlank()) {
            Toast.makeText(searchEditText.context, "(*^▽^*)开始搜索: $query", Toast.LENGTH_SHORT).show()
            fragment.updateVideoRows(emptyList(), true)

            // 显示加载动画
            loadingSpinner.visibility = View.VISIBLE
            MainScope().launch {
                val chenApi = ChenApi()
                val searchResults = chenApi.GetSear(query)

                // 隐藏加载动画
                loadingSpinner.visibility = View.GONE

                val subjects = searchResults?.map {
                    Subject(
                        episodes_info = it.desc,
                        rate = it.tags,
                        cover_x = 0,
                        title = it.title,
                        url = it.detailUrl,
                        playable = false,
                        cover = it.cover,
                        id = "tolist",
                        cover_y = 0,
                        is_new = false
                    )
                } ?: emptyList()

                fragment.updateVideoRows(subjects, true)
                val message = if (subjects.isNotEmpty()) {
                    "(*^▽^*) 搜索到了 ${subjects.size} 个结果！"
                } else {
                    "o(╥﹏╥)o 没有搜索到结果！"
                }
                Toast.makeText(searchEditText.context, message, Toast.LENGTH_SHORT).show()
            }
        } else {
            fragment.updateVideoRows(emptyList(), false)
        }
    }

    // 清空搜索框内容
    private fun clearSearchBox() {
        searchEditText.text.clear()
    }

    // 设置搜索框的文字内容
    fun updateSearchBoxText(text: String) {
        searchEditText.setText(text)
        performSearch() // 更新文字后自动执行搜索
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        // 清理资源
    }
}
