package com.example.final_project_board_app

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class TrickListAdapter(private val tricks: List<Trick>, private val context: Context): RecyclerView.Adapter<TrickListAdapter.MyViewHolder>() {
    private val TAG = "MyRecyclerView"
    private var count = 1
    private val FILE_NAME = "Board"
    val sharedPreferences = context.getSharedPreferences(FILE_NAME, AppCompatActivity.MODE_PRIVATE)
    var editor = sharedPreferences.edit()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trickName = itemView.findViewById<TextView>(R.id.trick_name)
        val trickDifficulty = itemView.findViewById<TextView>(R.id.trick_difficulty)

        init {

            itemView.setOnClickListener {
                val selectedPosition = adapterPosition
                editor.putString("link",tricks[selectedPosition].link)
                editor.putString("trick",tricks[selectedPosition].trick)
                editor.apply()
                val myIntent = Intent(itemView.context, TrickValidation::class.java)
                myIntent.putExtra("id", tricks.get(selectedPosition).id)
                itemView.context.startActivity(myIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.d(TAG, "onCreateViewHolder: ${count++}")

        val view = LayoutInflater.from(parent.context).inflate(R.layout.trick, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = tricks[position]
        holder.trickName.text = currentItem.trick
        holder.trickDifficulty.text = currentItem.difficulty
    }

    override fun getItemCount(): Int {
        return tricks.size
    }
}