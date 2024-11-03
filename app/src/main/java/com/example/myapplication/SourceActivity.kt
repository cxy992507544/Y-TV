package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class SourceActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SourceBrowseFragment())
                .commitNow()
        }
    }
}
