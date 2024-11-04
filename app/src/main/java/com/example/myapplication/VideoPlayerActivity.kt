package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.leanback.widget.HorizontalGridView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R.*
import com.example.myapplication.model.File
import com.example.myapplication.model.FileListResponse
import com.example.myapplication.model.Item
import com.example.myapplication.model.VideoQuality
import com.example.myapplication.network.ChenApi
import com.example.myapplication.play.GSPlayerSystem
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class VideoPlayerActivity : FragmentActivity() {

    private lateinit var videoPlayer: StandardGSYVideoPlayer
    private lateinit var episodeGridView: HorizontalGridView
    private lateinit var vdlist: FileListResponse
    private lateinit var progressBar: ProgressBar
    private val chen = ChenApi()
    private var playhx = 2 //播放内核
    private var purl = "" //当前播放的url
    private var ptitle = "" //当前播放的标题

    // 用于处理长按手势
    private var isRewinding = false
    private var isFastForwarding = false
    private lateinit var durationTextView: TextView // 用于显示当前视频总时长和目标时间
    private var buttonDownTime: Long = 0 // 记录按下时间
    private var isLongPressKey = false //是否为长按操作
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_video_player)

        videoPlayer = findViewById(id.videoPlayer)

        videoPlayer.isFocusable = true
        videoPlayer.isFocusableInTouchMode = true//允许获取焦点

        GSPlayerSystem.setPlayHx(playhx)//设置播放内核
        episodeGridView = findViewById(id.episodeGridView)
        progressBar = findViewById(id.pabrogressBar)
        durationTextView = TextView(this) // 初始化 TextView
        durationTextView.setTextColor(Color.WHITE)
        durationTextView.textSize = 16f
        durationTextView.visibility = View.GONE
        // 添加到布局中
        (findViewById<FrameLayout>(id.mainLayout)).addView(durationTextView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            })

        displayShowInfo()
        setupPlayer()
    }


    private fun setupPlayer() {
        progressBar.visibility = View.VISIBLE // 显示加载进度条
        val videoUrl = intent.getStringExtra(EXTRA_VIDEO_URL) ?: "https://your.default.video.url"
        val videoTitle = intent.getStringExtra(EXTRA_SHOW_TITLE) ?: "未知标题"
        MainScope().launch {
            try {
                //1.传入地址获取到磁力链接
                val mg = chen.GetMgUrl(Item(videoUrl, videoTitle, "10086"))
                val vmlist: MutableList<VideoQuality> = mutableListOf()

                if (mg != null && mg.url.isNotEmpty()) {
                    //2.添加云任务
                    val d1 = chen.Getupuri(mg.url)
                    //3.等待云任务完成
                    var d2 = d1?.task?.let { it1 -> chen.Getfilestr(it1.id) }
                    val maxRetries = 30 // 最大重试次数
                    (1..maxRetries).forEach { _ ->
                        if (d2?.tasks?.get(0)?.message == "已存到云盘") {
                            return@forEach
                        }
                        Thread.sleep(1000) //睡着一秒
                        d2 = d1?.task?.let { it1 -> chen.Getfilestr(it1.id) }
                    }

                    if (d2?.tasks?.get(0)?.message == "已存到云盘") {
                        //4.获取文件夹内容
                        val d3 = d2?.tasks?.get(0)?.let { it1 -> chen.Getfiles(it1.fileId) }
                        if (d3?.files?.count()!! > 0) {
                            vdlist = d3!!
                            //--对列表进行排序
                            vdlist.files.sortBy { it.name }
                            // 定义文件大小阈值
                            val MIN_FILE_SIZE = 1024 * 1024 * 50L
                            // 移除过小的文件
                            vdlist.files.removeAll { it.size.toLong() < MIN_FILE_SIZE }
                            //5.获取第一项视频的画质列表
                            val d4 = d3?.files?.get(0)?.let { it1 -> chen.Getfileplay(it1.id) }
                            if(d4 == null){
                                //--进入此处说明没有文件夹 就是文件列表
                                //5.获取第一项视频的画质列表
                                val d4r = d2?.tasks?.get(0)?.let { it1 -> chen.Getfileplay(it1.fileId) }
                                //--清除掉播放地址为空对象
                                d4r?.removeAll { it.url.isNullOrEmpty() }
                                //6.添加画质列表
                                d4r?.forEach { vmlist.add(it) }
                            }
                            else{
                                //--清除掉播放地址为空对象
                                d4.removeAll { it.url.isNullOrEmpty() }
                                //6.添加画质列表
                                d4?.forEach { vmlist.add(it) }
                            }
                        } else {
                            vdlist = d3!!
                            //--进入此处说明没有文件夹 就是文件列表
                            vdlist.kind = d2?.tasks?.get(0)?.fileId.toString()//文件ID
                            //5.获取第一项视频的画质列表
                            val d4 = d2?.tasks?.get(0)?.let { it1 -> chen.Getfileplay(it1.fileId) }
                            //6.添加画质列表
                            d4?.forEach { vmlist.add(it) }
                        }
                    }
                }

                //继续实例化播放器等操作
                if (vdlist.files.isNotEmpty()) {
                    purl = vmlist[0].url.toString()
                    ptitle = "$videoTitle-${vdlist.files[0].name}"
                    videoPlayer.setUp(purl, true, "$videoTitle-${vdlist.files[0].name}")
                    videoPlayer.titleTextView.visibility = View.GONE
                    videoPlayer.backButton.visibility = View.GONE
                    videoPlayer.fullscreenButton.visibility = View.GONE
                    // 设置播放器获得焦点时的边框样式
                    videoPlayer.setOnFocusChangeListener { view, hasFocus ->
                        view.background =
                            if (hasFocus) getDrawable(drawable.focused_border) else null
                    }
                    // 设置播放器的点击事件以进入全屏
                    videoPlayer.setOnClickListener {
                        if (!videoPlayer.isIfCurrentIsFullscreen) {
                            enterFullScreen()
                        }
                        else{
                            if (videoPlayer.currentState == GSYVideoView.CURRENT_STATE_PAUSE)
                                videoPlayer.onVideoResume()
                            else
                                videoPlayer.onVideoPause()
                        }
                    }
                    // 启动播放器逻辑
                    GSPlayerSystem.setEnableHardwareAcceleration(true)
                    videoPlayer.startPlayLogic()
                    videoPlayer.requestFocus() // 确保请求焦点
                    setupEpisodeGridView()
                } else {
                    //弹窗提示错误
                    Toast.makeText(
                        this@VideoPlayerActivity,
                        "视频资源获取失败...重试或更换其它资源！",
                        Toast.LENGTH_SHORT
                    ).show()
                    //返回上一层页面
                    finish()
                }

            } catch (e: Exception) {
                Toast.makeText(this@VideoPlayerActivity, e.message, Toast.LENGTH_SHORT)
                    .show()
                finish()
            } finally {
                progressBar.visibility = View.GONE // 加载完成后隐藏进度条
            }

        }
    }

    private fun enterFullScreen() {
        videoPlayer.startWindowFullscreen(this, true, true)
    }

    private fun setupEpisodeGridView() {
        val videoTitle = intent.getStringExtra(EXTRA_SHOW_TITLE) ?: "未知标题"
        val episodeAdapter = EpisodeAdapter { episodeUrl ->
            //--获取播放画质列表
            MainScope().launch {
                GSYVideoManager.releaseAllVideos()//先释放所有
                //1.获取视频的画质列表
                val d4 = chen.Getfileplay(episodeUrl.id)
                //--清除掉播放地址为空对象
                d4?.removeAll { it.url.isNullOrEmpty() }
                //2.默认播放最高画质的一项 也就是第一项
                purl = d4?.get(0)?.url.toString()
                ptitle = "$videoTitle-${episodeUrl.name}"
                videoPlayer.setUp(purl, true, "$videoTitle-${episodeUrl.name}")
                //--开始播放前关闭硬件加速
                GSPlayerSystem.setEnableHardwareAcceleration(true)
                videoPlayer.startPlayLogic()
            }
        }

        episodeGridView.apply {
            layoutManager =
                LinearLayoutManager(this@VideoPlayerActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = episodeAdapter
        }

        // 绑定数据
        if (vdlist.files.isNotEmpty()) {
            episodeAdapter.submitList(vdlist.files)
        } else {
            //--生成唯一的文件播放
            val videoFile = File(id = vdlist.kind, name = "正片")
            episodeAdapter.submitList(listOf(videoFile))
        }
    }

    private fun displayShowInfo() {
        val showTitle = intent.getStringExtra(EXTRA_SHOW_TITLE) ?: "剧集标题示例"
        val showDescription = intent.getStringExtra(EXTRA_SHOW_DESCRIPTION) ?: "剧集简介内容..."

        val titleTextView: TextView = findViewById(id.tvTitle)
        val descriptionTextView: TextView = findViewById(id.tvDescription)

        titleTextView.text = showTitle
        descriptionTextView.text = showDescription

        // 设置字体颜色为亮色，以确保可读性
        titleTextView.setTextColor(getColor(color.white))
        descriptionTextView.setTextColor(getColor(color.white))
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayer.release()
        GSPlayerSystem.clear(this) // 释放播放器所有缓存
        GSYVideoManager.releaseAllVideos()// 释放所有视频
    }

    //长按自定义处理
    fun lbtn_click(event: KeyEvent?): Boolean {
        //--处理长按判断 始终返回True是表示事件已经被处理
        if (event?.repeatCount == 0) {
            event.startTracking();
            isLongPressKey = false;
            buttonDownTime = System.currentTimeMillis()
            return true
        } else {
            isLongPressKey = true;
            return true
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (videoPlayer.isIfCurrentIsFullscreen) {
            when (keyCode) {
                KeyEvent.KEYCODE_DPAD_LEFT -> {
                    isRewinding = true // 设置为正在快退
                    return lbtn_click(event)//处理长按
                }

                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    isFastForwarding = true // 设置为正在快退
                    return lbtn_click(event)//处理长按
                }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        isLongPressKey = true;
        return super.onKeyLongPress(keyCode, event);
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (isLongPressKey) {
            isLongPressKey = false
        }
        if (videoPlayer.isIfCurrentIsFullscreen) {
            when (keyCode) {
                KeyEvent.KEYCODE_DPAD_LEFT -> {
                    handleButtonRelease(buttonDownTime)
                    // 停止快退
                    stopRewind()
                }

                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    handleButtonRelease(buttonDownTime)
                    // 停止快进
                    stopFastForward()
                }

                KeyEvent.KEYCODE_DPAD_UP -> {
                    //处理向上按钮
                    if (playhx > 0) {
                        playhx -= 1
                        GSPlayerSystem.setPlayHx(playhx)
                        //--切换内核完成后重置播放器
                        var seekTo = videoPlayer.getCurrentPositionWhenPlaying()
                        videoPlayer.setUp("", true, ptitle)
                        videoPlayer.startPlayLogic()
                        MainScope().launch {
                            videoPlayer.setUp(purl, true, ptitle)
                            GSPlayerSystem.setEnableHardwareAcceleration(true)
                            videoPlayer.startPlayLogic()
                            MainScope().launch {
                                videoPlayer.seekTo(seekTo)
                            }
                        }
                        Toast.makeText(this, "已切换${playhx + 1}号内核!", Toast.LENGTH_SHORT)
                            .show()
                    } else
                        Toast.makeText(
                            this,
                            "当前已经是第一个播放内核了，当前为系统内核!",
                            Toast.LENGTH_SHORT
                        ).show()
                }

                KeyEvent.KEYCODE_DPAD_DOWN -> {
                    //处理向下按钮
                    if (playhx < 3) {
                        playhx += 1
                        GSPlayerSystem.setPlayHx(playhx)
                        //--切换内核完成后重置播放器
                        var seekTo = videoPlayer.getCurrentPositionWhenPlaying()
                        videoPlayer.setUp("", true, ptitle)
                        videoPlayer.startPlayLogic()
                        MainScope().launch {
                            videoPlayer.setUp(purl, true, ptitle)
                            GSPlayerSystem.setEnableHardwareAcceleration(true)
                            videoPlayer.startPlayLogic()
                            MainScope().launch {
                                videoPlayer.seekTo(seekTo)
                            }
                        }
                        Toast.makeText(this, "已切换${playhx + 1}号内核!", Toast.LENGTH_SHORT)
                            .show()
                    } else
                        Toast.makeText(
                            this,
                            "当前已经是最后一个播放内核了，当前为AliPlayer内核!",
                            Toast.LENGTH_SHORT
                        ).show()
                }
                KeyEvent.KEYCODE_DPAD_CENTER -> {
                    //处理确认按钮
                    if (videoPlayer.currentState == GSYVideoView.CURRENT_STATE_PAUSE)
                        onResume()
                    else
                        onPause()
                }
                KeyEvent.KEYCODE_BACK -> {
                    //处理返回按钮
                    //退出全屏
                    videoPlayer.onBackFullscreen()
                }
            }
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    private fun handleButtonRelease(startTime: Long) {
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        if (duration >= 1000) { // 超过1秒为长按
            // 计算快进或快退的时间
            val timeToAdjust = (duration / 500) * 60000 // 每0.5秒调整1分钟
            Toast.makeText(this, "当前调整时间为: ${(duration / 500)} 分钟!", Toast.LENGTH_SHORT)
                .show()
            if (isRewinding) {
                val currentPosition = videoPlayer.getCurrentPositionWhenPlaying()
                if (currentPosition != null) {
                    val newPosition =
                        if (currentPosition - timeToAdjust <= 0) 0 else currentPosition - timeToAdjust
                    videoPlayer.seekTo(newPosition)
                }
            } else if (isFastForwarding) {
                val currentPosition = videoPlayer.getCurrentPositionWhenPlaying()
                if (currentPosition != null) {
                    val newPosition =
                        if (currentPosition + timeToAdjust >= videoPlayer.getDuration())
                            videoPlayer.getDuration()-3000 else currentPosition + timeToAdjust
                    videoPlayer.seekTo(newPosition)
                }
            }
        } else {
            // 短按处理
            if (isRewinding) {
                videoPlayer.seekTo(videoPlayer.getCurrentPositionWhenPlaying() - 10000) // 快退10秒
            } else if (isFastForwarding) {
                videoPlayer.seekTo(videoPlayer.getCurrentPositionWhenPlaying() + 10000) // 快进10秒
            }
        }
    }


    private fun stopRewind() {
        isRewinding = false
        durationTextView.visibility = View.GONE // 隐藏时长文本
    }

    private fun stopFastForward() {
        isFastForwarding = false
        durationTextView.visibility = View.GONE // 隐藏时长文本
    }


    companion object {
        private const val EXTRA_VIDEO_URL = "EXTRA_VIDEO_URL"
        private const val EXTRA_SHOW_TITLE = "EXTRA_SHOW_TITLE"
        private const val EXTRA_SHOW_DESCRIPTION = "EXTRA_SHOW_DESCRIPTION"
        fun start(context: Context, videoUrl: String, showTitle: String, showDescription: String) {
            val intent = Intent(context, VideoPlayerActivity::class.java).apply {
                putExtra(EXTRA_VIDEO_URL, videoUrl)
                putExtra(EXTRA_SHOW_TITLE, showTitle)
                putExtra(EXTRA_SHOW_DESCRIPTION, showDescription)
            }
            context.startActivity(intent)
        }
    }
}
