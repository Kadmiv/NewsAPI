package com.kadmiv.newsapi.activity_show_news

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.*
import com.kadmiv.newsapi.R
import kotlinx.android.synthetic.main.activity_news.*
import com.kadmiv.newsapi.R.id.webView
import android.net.http.SslCertificate.saveState


const val NEWS_URL = "NEWS_URL"

class ActivityNews : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        var newsUrl = intent.getStringExtra(NEWS_URL)

        webView.webChromeClient = Client()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                pageLoadingProgress.visibility = View.VISIBLE
                tryAgainBtn.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView, url: String) {
                pageLoadingProgress.visibility = View.GONE

            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                tryAgainBtn.visibility = View.VISIBLE
            }
        }

        webView.settings.javaScriptEnabled = true
        if (savedInstanceState != null)
            webView.restoreState(savedInstanceState);
        else
            webView.loadUrl(newsUrl)

        tryAgainBtn.setOnClickListener { webView.loadUrl(newsUrl) }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    inner class Client() : WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            Log.d("12", "Loading progress $newProgress")
            try {
                pageLoadingProgress.progress = newProgress
            } catch (ex: Exception) {
            }
        }
    }

    override fun onDestroy() {
        webView.webChromeClient = null
        webView.webViewClient = null
        super.onDestroy()
    }
}
