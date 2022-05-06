package com.example.final_project_board_app

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


class TrickList : AppCompatActivity() {
    private val FILE_NAME = "Names"
    val TAG = "firebase"
    private lateinit var fireBaseDb: FirebaseFirestore
    val trickList = ArrayList<Trick>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trick_list)
        fireBaseDb = FirebaseFirestore.getInstance()
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
                    Log.d(TAG, "$maxId")
                    Log.d(TAG, "${document.data["id"]}")
                    Log.d(TAG, "${document.id}=>${document.data["id"]}")

                    val test = maxId + 1
                    Log.d(TAG, "$test")

                    trickList.add(Trick(maxId, "Trick: ${document.data["trick"]}", "Difficulty: ${document.data["difficulty"]}", ""))
//                    Log.d(TAG, "${document.id}=>${document.data["trick"]}")
//                    Log.d(TAG, "${document.id}=>${document.data["trick"]}")
                }
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

    fun viewAllDataButton(view: View) {

        // Get data using addOnSuccessListener
        fireBaseDb.collection("tricks")
            .orderBy("id")  // Here you can also use orderBy to sort the results based on a field such as id
            //.orderBy("id", Query.Direction.DESCENDING)  // this would be used to orderBy in DESCENDING order
            .get()
            .addOnSuccessListener { documents ->

                val buffer = StringBuffer()

                // The result (documents) contains all the records in db, each of them is a document
                for (document in documents) {

                    Log.d(TAG, "${document.id} => ${document.data}")

                    Log.d(TAG, "contact: ${document.get("id")}, ${document.get("trick")}, ${document.get("difficulty")}")

                    // Create a string buffer (i.e., concatenate all the fields into one string)
                    buffer.append("ID : ${document.get("id")}" + "\n")
                    buffer.append("NAME : ${document.get("trick")}" + "\n")
                    buffer.append("EMAIL :  ${document.get("difficulty")}" + "\n\n")
                }

                // show all the records as a string in a dialog
                showDialog("Data Listing", buffer.toString())
            }
            .addOnFailureListener {
                Log.d(TAG, "Error getting documents")
                showDialog("Error", "Error getting documents")
            }
    }

//    fun realtimeUpdateButton(view: View) {
//
//        // Get real time update
//        fireBaseDb.collection("tricks")
//            .addSnapshotListener{ snapshots, e ->
//                if (e != null) {
//                    Log.w(TAG, "Listen failed.", e)
//                    return@addSnapshotListener
//                }
//
//
//                if (snapshots != null) {
//
//                    // This will be called every time a document is updated
//                    Log.d(TAG, "onEvent: -----------------------------")
//
//                    val contacts = snapshots.toObjects<Trick>()
//                    for (contact in contacts) {
////                        Log.d(TAG, "Current data: ${trick}")
////                        showData(trick)
//                    }
//                } else {
//                    Log.d(TAG, "Current data: null")
//                }
//            }
//    }

    private fun showData(trick: Trick) {
        trick_name.setText(trick.id.toString())
        trick_difficulty.setText(trick.trick.toString())
    }

    private fun generateContent(size: Int): ArrayList<Trick> {
        for(trick in trickList)
        {
//            val imageLink = "" // get from api
        }
        trickList.reverse()
        return trickList
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
}