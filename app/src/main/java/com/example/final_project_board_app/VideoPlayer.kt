package com.example.final_project_board_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class VideoPlayer: YouTubeBaseActivity(){

    val REQUEST_CODE = 555
    private val TAG = "VideoPlayer"
    val API_KEY = "AIzaSyAzGHbTYqdm8-iB416awVQwo3yPODxwWw4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_player)
        val link = intent.getStringExtra("link")?: ""
        Log.d(TAG, "$link")
        val youtubePlayer = findViewById<YouTubePlayerView>(R.id.YoutubePlayerView)
        val playButton = findViewById<Button>(R.id.play)

        playButton.setOnClickListener{
            youtubePlayer.initialize(API_KEY, object : YouTubePlayer.OnInitializedListener{
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider?,
                    player: YouTubePlayer?,
                    p2: Boolean
                ) {
                    player?.loadVideo(link)
                    player?.play()
                }

                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?
                ) {
                    Toast.makeText(this@VideoPlayer, "Failed to load video", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    fun letsgo(view: View){
        val intent = Intent(this, TrickValidation::class.java)
        startActivity(intent)
    }
}