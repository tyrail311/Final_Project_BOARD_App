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

class TrickListAdapter(private val tricks: List<Trick>): RecyclerView.Adapter<TrickListAdapter.MyViewHolder>() {
    private val TAG = "MyRecyclerView"
    private var count = 1
    private lateinit var fireBaseDb: FirebaseFirestore


    inner class MyViewHolder(itemView: View, context : Context?) : RecyclerView.ViewHolder(itemView) {

        val trickName = itemView.findViewById<TextView>(R.id.trick_name)
        val trickDifficulty = itemView.findViewById<TextView>(R.id.trick_difficulty)
//        val trickImage = itemView.findViewById<ImageView>(R.id.trick_image)

        init {
            itemView.setOnClickListener {
                val selectedPosition = adapterPosition

                val myIntent = Intent(context, TrickValidation::class.java)
                if (context != null) {
                    startActivity(context, myIntent, null)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.d(TAG, "onCreateViewHolder: ${count++}")

        val view = LayoutInflater.from(parent.context).inflate(R.layout.trick, parent, false)
        return MyViewHolder(view, null)
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