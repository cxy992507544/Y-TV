package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {

    private var backPressedTime: Long = 0
    private val backPressedInterval: Long = 2000 // 两秒内需要再次按下返回键
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            super.onBackPressed() // 或者 finish() 来关闭当前活动
        } else {
            // 显示提示
            Toast.makeText(this, "再按一次返回键退出应用", Toast.LENGTH_SHORT).show()
            backPressedTime = currentTime // 更新最后按下返回键的时间
        }
    }


}
