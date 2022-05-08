package com.example.final_project_board_app

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class TrickListAdapter(private val tricks: List<Trick>, private val context: Context): RecyclerView.Adapter<TrickListAdapter.MyViewHolder>() {
    private val TAG = "MyRecyclerView"
    private var count = 1

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trickName = itemView.findViewById<TextView>(R.id.trick_name)
        val trickDifficulty = itemView.findViewById<TextView>(R.id.trick_difficulty)
//        val trickImage = itemView.findViewById<ImageView>(R.id.trick_image)

        init {

            itemView.setOnClickListener {
                val selectedPosition = adapterPosition
                val myIntent = Intent(itemView.context, TrickValidation::class.java)
                myIntent.putExtra("link", tricks.get(selectedPosition).link)
                myIntent.putExtra("trick", tricks.get(selectedPosition).trick)
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

//        Glide.with(holder.itemView.context)
//            .load(currentItem.link)
////            .into(holder.trickImage)
    }

    override fun getItemCount(): Int {
        return tricks.size
    }
}