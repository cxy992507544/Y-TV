package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.*
import com.example.myapplication.model.Subject
import com.example.myapplication.network.ChenApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

@Serializable
data class VideoCategory(val category: String, val videos: List<Subject>)

class MainFragment : RowsSupportFragment() {

    private lateinit var rowsAdapter: ArrayObjectAdapter
    private lateinit var searchPresenter: SearchPresenter
    private var previousVideoCategories: MutableList<VideoCategory> = mutableListOf() // 用于保存之前的视频分类

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = rowsAdapter

        setupSearchRow()
        loadPreviousData()
    }

    private fun setupSearchRow() {
        searchPresenter = SearchPresenter(this)
        val searchRowAdapter = ArrayObjectAdapter(searchPresenter)
        searchRowAdapter.add("搜索框")

        val headerItem = HeaderItem(0, "搜索")
        rowsAdapter.add(ListRow(headerItem, searchRowAdapter))
    }

    fun setupVideoRows() {
        MainScope().launch {
            val chenApi = ChenApi()
            val v1: MutableList<Subject> = mutableListOf()
            val v2: MutableList<Subject> = mutableListOf()
            val v3: MutableList<Subject> = mutableListOf()

            val m1 = chenApi.GetHotMovie(0)
            val m2 = chenApi.GetHotMovie(1)
            val m3 = chenApi.GetHotMovie(2)

            // 清空之前的数据
            updateVideoRows(mutableListOf(), false)

            // 加载数据并填充列表
            m1?.subjects?.forEach { v1.add(it) }
            m2?.subjects?.forEach { v2.add(it) }
            m3?.subjects?.forEach { v3.add(it) }

            // 更新 UI
            addVideoRow("电影", v1)
            addVideoRow("电视剧", v2)
            addVideoRow("动漫", v3)

            // 保存新的数据
            previousVideoCategories.clear()
            previousVideoCategories.add(VideoCategory("电影", v1))
            previousVideoCategories.add(VideoCategory("电视剧", v2))
            previousVideoCategories.add(VideoCategory("动漫", v3))

            // 保存数据到 SharedPreferences
            saveData(previousVideoCategories)

            // 弹窗提示数据更新
            Toast.makeText(requireContext(), "数据已更新", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addVideoRow(category: String, videos: List<Subject>) {
        val videoRowAdapter = ArrayObjectAdapter(VideoCardPresenter { title ->
            searchPresenter.updateSearchBoxText(title) // 使用回调更新搜索框文字
        })
        videos.forEach { videoRowAdapter.add(it) }

        val headerItem = HeaderItem(rowsAdapter.size().toLong(), category)
        rowsAdapter.add(ListRow(headerItem, videoRowAdapter))
    }

    private fun loadPreviousData() {
        val sharedPreferences = requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("previousVideoCategories", null)

        if (json != null) {
            val videoCategories: List<VideoCategory> = Json.decodeFromString(json)
            // 保存新的数据
            previousVideoCategories.clear()
            videoCategories.forEach { category ->
                addVideoRow(category.category, category.videos)
                previousVideoCategories.add(category)
            }

        }
        else{
            setupVideoRows()
        }
    }

    private fun saveData(videoCategories: List<VideoCategory>) {
        val sharedPreferences = requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            val json = Json.encodeToString(videoCategories)
            putString("previousVideoCategories", json)
            apply()
        }
    }

    fun updateVideoRows(subjects: List<Subject>, isSearch: Boolean) {
        val videoRowAdapter = ArrayObjectAdapter(VideoCardPresenter { title ->
            searchPresenter.updateSearchBoxText(title) // 使用回调更新搜索框文字
        })

        // 清空当前的视频行并添加新的视频数据
        if (rowsAdapter.size() > 1) {
            rowsAdapter.removeItems(1, rowsAdapter.size() - 1)
        }

        if (isSearch) {
            subjects.forEach { videoRowAdapter.add(it) }
            val headerItem = HeaderItem(1, "搜索结果")
            rowsAdapter.add(ListRow(headerItem, videoRowAdapter))
        } else {
            previousVideoCategories.forEach { addVideoRow(it.category, it.videos) }
        }

    }

}
