package com.example.hritage.activities

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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

        if (url != null) {
            if (url.contains(".pdf")) {
                webView.settings.javaScriptEnabled = true
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


        webView.setDownloadListener { url1, userAgent, contentDisposition, mimeType, contentLength ->
            val request = DownloadManager.Request(Uri.parse(url1))
            request.setMimeType(mimeType)
            //------------------------COOKIE!!------------------------
            val cookies = CookieManager.getInstance().getCookie(url1)
            request.addRequestHeader("cookie", cookies)
            //------------------------COOKIE!!------------------------
            request.addRequestHeader("User-Agent", userAgent)
            request.setDescription("Downloading file...")
            request.setTitle(URLUtil.guessFileName(url1, contentDisposition, mimeType))
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, URLUtil.guessFileName(url1, contentDisposition, mimeType))
            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            Toast.makeText(this, "PDF Downloaded", Toast.LENGTH_LONG).show()

        }


    }

//    private fun savePdfName(pdfName: String) {
//        val dataBase = DatabaseForFileDataBase.getInstance(this@WebWiewActivity)
//        if (pdfName.contains("Notice")) {
//            lifecycleScope.launch {
//                dataBase.databaseForListDao()
//                    .insertNote(DocumentLidtClassEntity(0, Constant.NOTICEPDFS, pdfName))
//            }
//        }
//    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
       if (webView.canGoBack()){
           webView.goBack()
       }else{
           super.onBackPressed()
       }
    }

}