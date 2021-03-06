package com.example.final_project_board_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.android.synthetic.main.trick.*
import kotlinx.android.synthetic.main.trick_list.*
import java.lang.StringBuilder


class TrickList : AppCompatActivity() {
    private val FILE_NAME = "Board"

    lateinit var player1 : String
    lateinit var player2 : String
    var player1turn = true
    val TAG = "firebase"
    private lateinit var fireBaseDb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trick_list)
        fireBaseDb = FirebaseFirestore.getInstance()
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(divider)
        realtimeUpdate()
        val swipeToRefresh = findViewById<SwipeRefreshLayout>(R.id.swipe_to_refresh)
        swipeToRefresh.setOnRefreshListener {
            realtimeUpdate()
//            recyclerView.adapter = TrickListAdapter(tricks)
            swipeToRefresh.isRefreshing = false
        }

        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        player1turn =  sharedPreferences.getBoolean("player1turn", true)
        player1 = sharedPreferences.getString("player1", "")?: ""
        player2 = sharedPreferences.getString("player2", "")?: ""

        if(player1turn)
            findViewById<TextView>(R.id.player_turn).text = "It is $player1's turn..."
        else
            findViewById<TextView>(R.id.player_turn).text = "It is $player2's turn..."

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Get real time update
        fireBaseDb.collection("tricks")
            .orderBy("id")
            .addSnapshotListener{ snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    // This will be called every time a document is updated
                    Log.d(TAG, "onEvent: -----------------------------")

                    val stringBuilder = StringBuilder()
                    // Convert documents to a collection of Contact
                    val tricks = snapshots.toObjects<Trick>()

                    // Show all the records in a recyclerView with updated data
                    showDataInRecyclerView(tricks)

                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
    }

    fun addTrick(view: View) {
        val tricks = fireBaseDb.collection("tricks")
        var maxId = 0
        var docId = ""
        tricks.orderBy("id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents)
                {
                    docId = "${document.data["id"]}"
                    maxId = docId.toInt()
                }
                val trick = hashMapOf(
                    "id" to maxId + 1,
                    "trick" to trick_name_input.text.toString(),
                    "difficulty" to trick_difficulty_input.text.toString()
                )

                val documentId = tricks.document().id
                tricks.document(documentId).set(trick)
                showDialog("Success", "Contact has been added.")
                findViewById<EditText>(R.id.trick_difficulty_input).hideKeyboard()
                findViewById<EditText>(R.id.trick_name_input).hideKeyboard()
            }
    }

    private fun realtimeUpdate() {

        fireBaseDb.collection("tricks")
            .orderBy("id")
            .addSnapshotListener{ snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    // This will be called every time a document is updated
                    Log.d(TAG, "onEvent: -----------------------------")

                    val stringBuilder = StringBuilder()
                    // Convert documents to a collection of Contact
                    val tricks = snapshots.toObjects<Trick>()

                    // Show all the records in a recyclerView with updated data
                    showDataInRecyclerView(tricks)

                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
    }

    private fun showDataInRecyclerView(tricks: List<Trick>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = TrickListAdapter(tricks)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun showDialog(title : String,Message : String){
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(Message)
        builder.show()
    }

    private fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun youtube2(view: View){
        val intent = Intent(this, VideoPlayer::class.java)
        startActivity(intent)
    }
}