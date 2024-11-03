package com.example.myapplication

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.myapplication.model.Item
import com.example.myapplication.model.MediaSource
import com.example.myapplication.model.Sear
import com.example.myapplication.network.ChenApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SourceBrowseFragment : BrowseSupportFragment() {

    private lateinit var progressBar: ProgressBar // 声明进度条
    private val categories = listOf("2160p", "1080p", "4K", "Other")
    private val sourcesMap = mapOf(
        "2160p" to listOf("斗破苍穹 第五季 [第118集]", "斗破苍穹 第五季 [第117集]"),
        "1080p" to listOf("斗破苍穹 第五季 [第116集]", "斗破苍穹 第五季 [第115集]"),
        "4K" to listOf("斗破苍穹 第五季 [4K 片源示例]"),
        "Other" to listOf("斗破苍穹 第五季 [其他片源示例]")
    )

    private var mtitle = ""
    private var desc = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 初始化进度条
        progressBar = requireActivity().findViewById(R.id.progress_bar) // 替换为实际的进度条 ID

        // 获取 Intent
        val intent = requireActivity().intent
        // 接收传递的数据
        val Title = intent.getStringExtra("Title")
        val url = intent.getStringExtra("Url")
        desc = intent.getStringExtra("Desc").toString()

        title = Title
        mtitle = Title.toString()
        setupUI()
        loadContent(url.toString())
    }

    private fun setupUI() {
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        setOnItemViewClickedListener { _, item, _, _ ->
            if (item is Item) {
                val videoUrl = item.url
                val videoTitle = item.title
                context?.let {
                    VideoPlayerActivity.start(
                        context = it,
                        videoUrl = videoUrl,
                        showTitle = videoTitle,
                        showDescription = item.desc
                    )
                }
            }
        }
    }

    private fun loadContent(url: String) {
        loadRows(url)
    }

    private fun loadRows(url: String) {
        progressBar.visibility = View.VISIBLE // 显示进度条

        MainScope().launch {
            try {
                val chenApi = ChenApi()
                val mydata = chenApi.GetList(Sear("", url, "", "", desc, "", "", "", mtitle, url))

                val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

                mydata?.forEach {
                    val sourceList = it.items ?: emptyList()
                    val listRowAdapter = ArrayObjectAdapter(SourceButtonPresenter())
                    sourceList.forEach { source ->
                        run {
                            source.desc = desc
                            listRowAdapter.add(source)
                        }
                    }

                    val header = HeaderItem(it.name)
                    rowsAdapter.add(ListRow(header, listRowAdapter))
                }
                // 添加行适配器
                adapter = rowsAdapter
            } catch (e: Exception) {
                e.printStackTrace() // 打印异常信息
            } finally {
                progressBar.visibility = View.GONE // 隐藏进度条
            }
        }
    }

    private fun loadDetails() {
        val movieDetails = Movie(
            title = "斗破苍穹 第五季",
            description = "三年之约后，萧炎终于在迦南学院见到了薰儿，同时他广交好友并立誓门；" +
                    "为继续提升实力以上云岚宗为父复仇，踏上了与天斗气的旅程。",
            studio = "主演：萧炎、薰儿、纳兰嫣然"
        )

        val detailsOverviewRow = DetailsOverviewRow(movieDetails)
        detailsOverviewRow.imageDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.movie)

        Glide.with(requireContext())
            .load("https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2675950683.jpg")
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    detailsOverviewRow.imageDrawable = resource
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })

        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())
        val rowsAdapter = ArrayObjectAdapter(detailsPresenter)
        rowsAdapter.add(detailsOverviewRow)

        // 结合之前的行适配器，确保所有内容都在同一个适配器中
        adapter = rowsAdapter
    }
}
