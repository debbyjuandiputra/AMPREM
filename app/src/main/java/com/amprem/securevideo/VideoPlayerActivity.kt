package com.amprem.securevideo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        // Cegah screenshot & screen recording saat video diputar
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        // Jika sesi sudah habis, jangan izinkan menonton
        if (!SessionManager.isSessionValid(this)) {
            finish()
            return
        }

        val youtubeId = intent.getStringExtra("youtubeId") ?: ""
        val title = intent.getStringExtra("title") ?: "Video"

        findViewById<TextView>(R.id.txtVideoTitle).text = title
        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }

        webView = findViewById(R.id.webViewPlayer)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE

        val html = """
            <html>
            <body style="margin:0;padding:0;background:#000;">
              <div style="position:relative;padding-top:56.25%;">
                <iframe
                  style="position:absolute;top:0;left:0;width:100%;height:100%;"
                  src="https://www.youtube.com/embed/$youtubeId?rel=0&modestbranding=1&playsinline=1"
                  frameborder="0"
                  allow="autoplay; encrypted-media"
                  allowfullscreen>
                </iframe>
              </div>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(
            "https://www.youtube.com",
            html,
            "text/html",
            "utf-8",
            null
        )
    }

    override fun onResume() {
        super.onResume()
        if (!SessionManager.isSessionValid(this)) {
            finish()
        }
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}
