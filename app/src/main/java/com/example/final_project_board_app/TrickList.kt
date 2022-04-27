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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
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

//    private fun scanDatabase(){
//        val tricks = fireBaseDb.collection("tricks")
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Get Post object and use the values to update the UI
//                val post = dataSnapshot.getValue<Post>()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
//            }
//        }
//        postReference.addValueEventListener(postListener)
//
//        fireBaseDb.child("users").child(id).get().addOnSuccessListener {
//            Log.i("firebase", "Got value ${it.value}")
//        }.addOnFailureListener{
//            Log.e("firebase", "Error getting data", it)
//        }
//        tricks
//            .get()
//            .addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { document ->
//                if (document.isSuccessful) {
//                    var count = 0
//                    for (document in document.result) {
//                        count++
//                    }
////                    for(trick in 0..count)
////                    {
////                        trickList.add(trick)
////                    }
//                } else {
//                    Log.d(TAG, "Error getting documents: ", document.exception)
//                }
//            })
//    }

    fun addTrick(view: View) {
        val tricks = fireBaseDb.collection("tricks")
        var id = tricks.orderBy("id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents)
                {
                    Log.d(TAG, "${document.id})=>${document.data.get("id")}")
                }

            }
        val trick = hashMapOf(
//            "id" to maxId++,
            "trick" to trick_name_input.text.toString(),
            "difficulty" to trick_difficulty_input.text.toString()
        )
        val documentId = tricks.document().id
        tricks.document(documentId).set(trick)
//        trickList.add(trick.get(trick))
        showDialog("Success", "Contact has been added.")
        findViewById<EditText>(R.id.trick_difficulty_input).hideKeyboard()
        findViewById<EditText>(R.id.trick_name_input).hideKeyboard()
    }

    private fun generateContent(size: Int): ArrayList<Trick> {
        for(i in 1..size)
        {
            val imageLink = "" // get from api
            trickList.add(Trick("", "", ""))
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