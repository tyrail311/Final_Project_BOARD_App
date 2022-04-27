package com.example.final_project_board_app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private val FILE_NAME = "Names"
    private val REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun startGame(view: View)
    {
        val person1 = findViewById<EditText>(R.id.editTextPersonName).text.toString()
        val person2 = findViewById<EditText>(R.id.editTextPersonName2).text.toString()

        if(person1.isEmpty())
        {
            makeDialog("Player 1 Name Empty", "Player 1 name cannot be empty. Please enter a name", R.id.editTextPersonName)
        }

        if(person2.isEmpty())
        {
            makeDialog("Player 2 Name Empty", "Player 2 name cannot be empty. Please enter a name", R.id.editTextPersonName2)
        }

        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("player1", person1)
        editor.putString("player2", person2)
        editor.apply()

        val intent = Intent(this, TrickList::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun makeDialog(title : String, message: String, view: Int){
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setNeutralButton("Okay"){dialog, which -> findViewById<EditText>(view).requestFocus() }
        builder.setIcon(android.R.drawable.ic_delete)
        val dialog = builder.create()
        dialog.show()
    }

    private fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}