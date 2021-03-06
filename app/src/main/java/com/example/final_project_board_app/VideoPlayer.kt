package com.example.final_project_board_app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import java.util.ArrayList

class VideoPlayer: YouTubeBaseActivity(){

    val REQUEST_CODE = 555

    val API_KEY = "AIzaSyAzGHbTYqdm8-iB416awVQwo3yPODxwWw4"
    val VIDEO = "t8rCNbK7Aio"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_player)
        val youtubePlayer = findViewById<YouTubePlayerView>(R.id.YoutubePlayerView)
        val playButton = findViewById<Button>(R.id.play)

        playButton.setOnClickListener{
            youtubePlayer.initialize(API_KEY, object : YouTubePlayer.OnInitializedListener{
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider?,
                    player: YouTubePlayer?,
                    p2: Boolean
                ) {
                    player?.loadVideo(VIDEO)
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