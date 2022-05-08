package com.example.final_project_board_app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val FILE_NAME = "Board"
    private val REQUEST_CODE = 123
    private val TAG = "logout"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null)
            startRegisterActivity()
        else
        {
            findViewById<Button>(R.id.resume_button).setVisibility(View.GONE)
            val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
            if(sharedPreferences.contains("player1"))
            {
                findViewById<Button>(R.id.resume_button).setVisibility(View.VISIBLE)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // User chose the "logout" item, logout the user then
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()

                AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // After logout, start the RegisterActivity again
                            startRegisterActivity()
                        }
                        else {
                            Log.e(TAG, "Task is not successful:${task.exception}")
                        }
                    }
                true
            }
            else -> {
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                super.onOptionsItemSelected(item)
            }
        }
    }
    private fun startRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        // Make sure to call finish(), otherwise the user would be able to go back to the MainActivity
        finish()
    }
    fun startGame(view: View)
    {
        val sharedPref = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        sharedPref.edit().clear().commit()
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

    fun resumeGame(view: View){
        val intent = Intent(this, TrickValidation::class.java)
        startActivity(intent)
    }
}