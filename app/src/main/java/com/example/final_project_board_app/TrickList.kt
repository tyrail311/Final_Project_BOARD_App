package com.example.final_project_board_app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.util.ArrayList

class TrickList : AppCompatActivity() {
    private val FILE_NAME = "Names"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trick_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.adapter = TrickListAdapter(generateContent(40))

        recyclerView.layoutManager = LinearLayoutManager(this)


        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(divider)

        val swipeToRefresh = findViewById<SwipeRefreshLayout>(R.id.swipe_to_refresh)
        swipeToRefresh.setOnRefreshListener {
            Toast.makeText(this, "You Refreshed", Toast.LENGTH_SHORT).show()
            recyclerView.adapter = TrickListAdapter(generateContent(40))

            swipeToRefresh.isRefreshing = false
        }
    }

    fun generateContent(size: Int): ArrayList<Trick> {
        val trickList = ArrayList<Trick>()
        for(i in 1..size)
        {
            val imageLink = "" // get from api
            trickList.add(Trick("", "", ""))
        }
        trickList.reverse()

        return trickList
    }
}