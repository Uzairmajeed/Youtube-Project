package com.facebook.youtube_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facebook.reels_project.ReelsActivity
import com.facebook.reels_project.Video
import com.facebook.youtube_project.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    // Handle home navigation
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    // Pass the videos data to ReelsActivity
                    val intent = Intent(this, ReelsActivity::class.java)
                    val videos = createVideos() // Replace with your logic to create videos
                    intent.putExtra("videos", videos)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    // Handle notifications navigation
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun createVideos(): ArrayList<Video> {
        // Replace this with your logic to create and return a list of videos
        val videos = ArrayList<Video>()
        videos.add(Video("Video 1", "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
        videos.add(Video("Video 2", "https://storage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"))
        videos.add(Video("Video 2", "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"))
        videos.add(Video("Video 2", "https://storage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"))
        // ... add more videos
        return videos
    }
}