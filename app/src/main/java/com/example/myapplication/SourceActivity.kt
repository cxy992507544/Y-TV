package com.example.myapplication

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class SourceActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source)

//        // 设置一个默认背景，避免初次加载的空白
//        val frameLayout = findViewById<FrameLayout>(R.id.fragment_container)
//        frameLayout.setBackgroundResource(R.drawable.default_background) // 替换为合适的默认背景
//
//        // 生成一个 guid
//        val guid = "guid_" + System.currentTimeMillis()
//        val imageUrl = "https://api.suyanw.cn/api/comic?v=$guid"
//
//        // 使用 Glide 加载新图片，并添加淡入效果
//        Glide.with(this)
//            .load(imageUrl)
//            .transition(DrawableTransitionOptions.withCrossFade()) // 添加淡入效果
//            .into(object : CustomTarget<Drawable>() {
//                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                    // 加载完成后设置新背景
//                    frameLayout.background = resource
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    // 加载被清除或失败时保留当前背景
//                }
//            })

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SourceBrowseFragment())
                .commitNow()
        }
    }
}
