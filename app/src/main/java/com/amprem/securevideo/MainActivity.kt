package com.amprem.securevideo

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerVideos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = VideoAdapter(VideoRepository.getVideos()) { video ->
            val intent = Intent(this, VideoPlayerActivity::class.java)
            intent.putExtra("youtubeId", video.youtubeId)
            intent.putExtra("title", video.title)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Setiap kembali ke halaman ini, cek apakah sesi 15 menit sudah habis
        if (!SessionManager.isSessionValid(this)) {
            SessionManager.clearSession(this)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
