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
    private var activity = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity = sharedPreferences.getString("activity", "") ?: ""
        Log.d(TAG, "$activity")
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null)
            startRegisterActivity()
        else
        {
            findViewById<Button>(R.id.resume_button).setVisibility(View.GONE)
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
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()

                AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startRegisterActivity()
                        }
                        else {
                            Log.e(TAG, "Task is not successful:${task.exception}")
                        }
                    }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
    private fun startRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
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
            return
        }

        if(person2.isEmpty())
        {
            makeDialog("Player 2 Name Empty", "Player 2 name cannot be empty. Please enter a name", R.id.editTextPersonName2)
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
        var intent: Intent? = null
        if (activity == "TrickList")
            intent = Intent(this, TrickList::class.java)
        else
            intent = Intent(this, TrickValidation::class.java)

        startActivity(intent)
    }
}