package com.example.keyword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class ArticleActivity : AppCompatActivity() {

    private val webview: WebView by lazy {
        findViewById(R.id.wv)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val address = intent.getStringExtra("address")
        address?.let { webview.loadUrl(it) }
    }
}