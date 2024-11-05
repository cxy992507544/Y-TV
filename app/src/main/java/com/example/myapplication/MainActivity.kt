package com.example.myapplication

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class MainActivity : FragmentActivity() {

    private var backPressedTime: Long = 0
    private val backPressedInterval: Long = 2000 // 两秒内需要再次按下返回键

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 设置一个默认背景，避免初次加载的空白
        val frameLayout = findViewById<FrameLayout>(R.id.main_browse_fragment)
        frameLayout.setBackgroundResource(R.drawable.default_background) // 替换为合适的默认背景

        // 生成一个 guid
        val guid = "guid_" + System.currentTimeMillis()
        val imageUrl = "https://api.suyanw.cn/api/comic?v=$guid"

        // 使用 Glide 加载新图片，并添加淡入效果
        Glide.with(this)
        .load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade()) // 添加淡入效果
        .into(object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                // 加载完成后设置新背景
                frameLayout.background = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                // 加载被清除或失败时保留当前背景
            }
        })

        // 初次加载 MainFragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_browse_fragment, MainFragment())
                .commitNow()
        }
    }




    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime < backPressedInterval) {
            // 短时间内已按下返回键两次，退出应用
            super.finish() // 或者 onBackPressed() 来关闭当前活动
        } else {
            // 显示提示
            Toast.makeText(this, "再按一次返回键退出应用", Toast.LENGTH_SHORT).show()
            backPressedTime = currentTime // 更新最后按下返回键的时间
        }
    }


}
