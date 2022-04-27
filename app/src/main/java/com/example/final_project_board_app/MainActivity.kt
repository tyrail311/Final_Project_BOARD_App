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
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private val FILE_NAME = "Board"
    private val REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun startGame(view: View)
    {
        val person1 = findViewById<EditText>(R.id.editTextPersonName).text.toString()
        val person2 = findViewById<EditText>(R.id.editTextPersonName2).text.toString()


        val builder1 = AlertDialog.Builder(this)
        val builder2 = AlertDialog.Builder(this)
        builder1.setTitle("Player 1 Name Empty")
        builder1.setMessage("Player 1 name cannot be empty. Please enter a name")
        builder1.setIcon(android.R.drawable.ic_delete)
        builder1.setNeutralButton("Okay"){dialog, which -> findViewById<EditText>(R.id.editTextPersonName).requestFocus() }

        builder2.setTitle("Player 2 Name Empty")
        builder2.setMessage("Player 2 name cannot be empty. Please enter a name")
        builder2.setIcon(android.R.drawable.ic_delete)
        builder2.setNeutralButton("Okay"){dialog, which -> findViewById<EditText>(R.id.editTextPersonName2).requestFocus() }

        if(person1.isEmpty())
        {
            val dialog = builder1.create()
            dialog.show()
            return
        }

        if(person2.isEmpty())
        {
            val dialog = builder2.create()
            dialog.show()
            return
        }

        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("player1", person1)
        editor.putString("player2", person2)
        editor.apply()

        val intent = Intent(this, TrickList::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }

    fun youtube(view: View){
        val intent = Intent(this, VideoPlayer::class.java)
        startActivity(intent)
    }

    private fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}