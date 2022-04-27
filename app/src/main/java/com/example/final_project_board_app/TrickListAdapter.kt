package com.example.final_project_board_app

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrickListAdapter(private val tricks: ArrayList<Trick>): RecyclerView.Adapter<TrickListAdapter.MyViewHolder>() {
    private val TAG = "MyRecyclerView"
    private var count = 1

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val trickName = itemView.findViewById<TextView>(R.id.trick_name)
        val trickDifficulty = itemView.findViewById<TextView>(R.id.trick_difficulty)
        val trickImage = itemView.findViewById<ImageView>(R.id.trick_image)

        init {
            itemView.setOnClickListener {
                val selectedPosition = adapterPosition
                Toast.makeText(
                    itemView.context,
                    "You clicked $selectedPosition",
                    Toast.LENGTH_SHORT
                ).show()
            }
            itemView.setOnLongClickListener {

                val selectedPosition = adapterPosition

                tricks.removeAt(selectedPosition)
                notifyItemRemoved(selectedPosition)

                return@setOnLongClickListener true

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
        holder.trickName.text = currentItem.trickName
        holder.trickDifficulty.text = currentItem.trickDifficulty

        Glide.with(holder.itemView.context)
            .load(currentItem.trickImage)
            .into(holder.trickImage)
    }

    override fun getItemCount(): Int {
        return tricks.size
    }
}