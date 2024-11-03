package com.example.myapplication.diy

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.view.View

class CustomEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : EditText(context, attrs, defStyle) {

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_DOWN) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            // 判断键盘是否打开
            val isKeyboardVisible = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val rootView = (context as? Activity)?.window?.decorView?.rootView
                rootView?.height?.let { rootView.height < rootView.height - getKeyboardHeight() } ?: false
            } else {
                // 在较旧版本上使用简单的方式判断
                false
            }

            when (keyCode) {
                KeyEvent.KEYCODE_DPAD_LEFT -> {
                    if (!isKeyboardVisible) {
                        // 移动焦点到左侧控件
                        focusSearch(View.FOCUS_LEFT)?.requestFocus()
                        return true
                    }
                }
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    if (!isKeyboardVisible) {
                        // 移动焦点到右侧控件
                        focusSearch(View.FOCUS_RIGHT)?.requestFocus()
                        return true
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    // 获取键盘高度
    private fun getKeyboardHeight(): Int {
        val metrics = context.resources.displayMetrics
        return (metrics.heightPixels - getHeight())
    }
}

