package com.example.hritage.activities

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hritage.Constant
import com.example.hritage.R


class WebWiewActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    @SuppressLint("MissingInflatedId", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_wiew)
        val intent = intent

        webView = findViewById(R.id.web_view_layout)
        val url = intent.getStringExtra(Constant.SEND_LINK_TO_WEB_ACTIVITY)
        Toast.makeText(this, "" + url, Toast.LENGTH_LONG).show()
        webView.settings.javaScriptEnabled = true
        if (url != null) {
            if (url.contains(".pdf")) {
                webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=$url")
            } else {
                webView.loadUrl(url)
            }

        }
        webView.canGoBack()
        webView.goBack()
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                findViewById<ProgressBar>(R.id.loding_progress_webview).visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                findViewById<ProgressBar>(R.id.loding_progress_webview).visibility = View.GONE
            }
        }

        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false

        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW



        webView.setDownloadListener { url1, userAgent, contentDisposition, mimeType, _ ->
            if(url1.contains("blob")){
                Toast.makeText(this,"Nothing will happen on click it's just a gimmick Web developer's issue" ,
                    Toast.LENGTH_LONG).show()
                Toast.makeText(this,"Rather take a screenshot and be happy Thank You 😁" ,
                    Toast.LENGTH_LONG).show()
            }else {
                Log.i("Result URl", url1.toString())
                Toast.makeText(this, "" + url1.toString(), Toast.LENGTH_LONG).show()
                val request = DownloadManager.Request(Uri.parse(url1))
                request.setMimeType(mimeType)
                val cookies = CookieManager.getInstance().getCookie(url1)
                request.addRequestHeader("cookie", cookies)

                request.addRequestHeader("User-Agent", userAgent)
                request.setDescription("Downloading file...")
                request.setTitle(URLUtil.guessFileName(url1, contentDisposition, mimeType))
                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOCUMENTS,
                    URLUtil.guessFileName(url1, contentDisposition, mimeType)
                )
                val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                dm.enqueue(request)
                Toast.makeText(this, "PDF Downloaded", Toast.LENGTH_LONG).show()
            }

        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
       if (webView.canGoBack()){
           webView.goBack()
       }else{
           super.onBackPressed()
       }
    }

}