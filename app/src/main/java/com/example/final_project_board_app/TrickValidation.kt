package com.example.final_project_board_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.trick_validation.*

class TrickValidation: AppCompatActivity() {
    private val TAG = "TrickValidation"
    private val FILE_NAME = "Board"

    var player1 = ""
    var player2 = ""

    var player1count = 0
    var player2count = 0

    var player1score = ""
    var player2score = ""

    var player1turn = true

    var trick = ""

    var clickable = true

    var link = ""

    lateinit var continueButton: Button
    lateinit var p1_no: Button
    lateinit var p1_yes: Button
    lateinit var p2_no: Button
    lateinit var p2_yes: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trick_validation)

        //val link = intent.getStringExtra("link")?: ""
        //val trick = intent.getStringExtra("trick")?: ""
        val id = intent.getIntExtra("id", -1)

        //Log.d(TAG, "link: $link, trick: $trick, id: $id")
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        player1score = sharedPreferences.getString("player1score", "") ?: ""
        player2score = sharedPreferences.getString("player2score", "") ?: ""
        player1count = sharedPreferences.getInt("player1count", 0)
        player2count = sharedPreferences.getInt("player2count", 0)
        player1turn =  sharedPreferences.getBoolean("player1turn", true)
        player1 = sharedPreferences.getString("player1", "")?: ""
        player2 = sharedPreferences.getString("player2", "")?: ""
        trick = sharedPreferences.getString("trick", "")?:""
        clickable = sharedPreferences.getBoolean("clickable", true)
        link = sharedPreferences.getString("link", "")?: ""

        p1_no = findViewById<Button>(R.id.player1_no)
        p1_yes = findViewById<Button>(R.id.player1_yes)
        p2_no = findViewById<Button>(R.id.player2_no)
        p2_yes = findViewById<Button>(R.id.player2_yes)
        continueButton = findViewById<Button>(R.id.continue_button)
        player1_text.text = "$player1"
        player2_text.text = "$player2"

        findViewById<TextView>(R.id.player1_trick_land).text = "Did $player1 land the $trick?"
        findViewById<TextView>(R.id.player2_trick_land).text = "Did $player2 land the $trick?"

        findViewById<TextView>(R.id.player1_score_text).text = player1score
        findViewById<TextView>(R.id.player2_score_text).text = player2score

        buttonDisable(continueButton)
        checkGameFinish()
        if(clickable == false){
            buttonEnable(continueButton)
            buttonDisable(p1_no)
            buttonDisable(p1_yes)
            buttonDisable(p2_no)
            buttonDisable(p2_yes)
        }

        if (player1turn) //Player 1's turn to choose trick
        {
            p2_yes.setVisibility(View.GONE)
            p2_no.setVisibility(View.GONE)
            p1_no.setOnClickListener{
                player1turn = false
                Toast.makeText(this, "$player1 didn't land his trick, onto $player2's turn", Toast.LENGTH_SHORT).show()
                buttonDisable(p1_no)
                buttonDisable(p1_yes)
                buttonEnable(continueButton)
                clickable = false

            }
            p1_yes.setOnClickListener{
                Toast.makeText(this, "Nice one $player1", Toast.LENGTH_SHORT).show()
                p2_yes.setVisibility(View.VISIBLE)
                p2_no.setVisibility(View.VISIBLE)
                buttonDisable(p1_no)
                buttonDisable(p1_yes)

            }
            p2_yes.setOnClickListener{
                Toast.makeText(this, "Both of you landed your trick! Still $player1's turn", Toast.LENGTH_SHORT).show()
                buttonDisable(p2_no)
                buttonDisable(p2_yes)
                buttonEnable(continueButton)
                clickable = false
            }
            p2_no.setOnClickListener{
                player2count++
                player2score = updateScore(player2count)
                findViewById<TextView>(R.id.player2_score_text).text = player2score
                buttonDisable(p2_no)
                buttonDisable(p2_yes)
                buttonEnable(continueButton)
                clickable = false
                if(player2count == 5)
                {
                    findViewById<TextView>(R.id.gameover_text).text = "GAME OVER! $player1 wins!!"
                    buttonDisable(continueButton)
                }
            }

        }
        else //Player 2's turn to choose trick
        {
            p1_yes.setVisibility(View.GONE)
            p1_no.setVisibility(View.GONE)
            p2_no.setOnClickListener{
                player1turn = true
                Toast.makeText(this, "$player2 didn't land his trick, onto $player1", Toast.LENGTH_SHORT).show()
                buttonDisable(p2_no)
                buttonDisable(p2_yes)
                buttonEnable(continueButton)
                clickable = false
            }
            p2_yes.setOnClickListener{
                Toast.makeText(this, "There we go $player2!", Toast.LENGTH_SHORT).show()
                p1_yes.setVisibility(View.VISIBLE)
                p1_no.setVisibility(View.VISIBLE)
                buttonDisable(p2_no)
                buttonDisable(p2_yes)
            }
            p1_yes.setOnClickListener{
                Toast.makeText(this, "Both of you landed your trick! Still $player2's turn", Toast.LENGTH_SHORT).show()
                buttonDisable(p1_no)
                buttonDisable(p1_yes)
                buttonEnable(continueButton)
                clickable = false
            }
            p1_no.setOnClickListener{
                player1count++
                player1score = updateScore(player1count)
                findViewById<TextView>(R.id.player1_score_text).text = player1score
                buttonDisable(p1_no)
                buttonDisable(p1_yes)
                buttonEnable(continueButton)
                clickable = false
                if(player1count == 5)
                {
                    findViewById<TextView>(R.id.gameover_text).text = "GAME OVER! $player2 wins!!"
                    buttonDisable(continueButton)
                }
            }
        }

        continueButton.setOnClickListener{
            clickable = true
            sharePref()
            val intent = Intent(this, TrickList::class.java)
            startActivity(intent)
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

    override fun onStop() {
        super.onStop()
        sharePref()
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("activity", "TrickValidation")
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        sharePref()
    }

    fun checkGameFinish(){
        if(player1count >= 5)
        {
            findViewById<TextView>(R.id.gameover_text).text = "GAME OVER! $player2 wins!!"
            buttonDisable(continueButton)
            buttonDisable(p1_no)
            buttonDisable(p1_yes)
            buttonDisable(p2_no)
            buttonDisable(p2_yes)
        }
        if(player2count >= 5)
        {
            findViewById<TextView>(R.id.gameover_text).text = "GAME OVER! $player1 wins!!"
            buttonDisable(continueButton)
            buttonDisable(p1_no)
            buttonDisable(p1_yes)
            buttonDisable(p2_no)
            buttonDisable(p2_yes)
        }
    }

    fun updateScore(count: Int): String{
        var score = when(count){
            0 -> ""
            1 -> "B."
            2 -> "B.O."
            3 -> "B.O.A"
            4 -> "B.O.A.R."
            else -> "B.O.A.R.D."
        }
        return score
    }

    fun sharePref(){
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("player1turn", player1turn)
        editor.putString("player1score", player1score)
        editor.putString("player2score", player2score)
        editor.putInt("player1count", player1count)
        editor.putInt("player2count", player2count)
        editor.putBoolean("clickable", clickable)
        editor.apply()
    }

    fun buttonDisable(button: Button){
        button.isEnabled = false
        button.isClickable = false
        button.setTextColor(getApplication().getResources().getColor(R.color.white))
        button.setBackgroundColor(button.getContext().getResources().getColor(androidx.appcompat.R.color.material_grey_800))
    }

    fun buttonEnable(button: Button){
        button.isEnabled = true
        button.isClickable = true
        button.setTextColor(getApplication().getResources().getColor(R.color.white))
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
    }

    fun trickHelp(view: View){
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("clickable", clickable)
        editor.apply()
        if(sharedPreferences.contains("link"))
        {
            val intent = Intent(this, VideoPlayer::class.java)
            startActivity(intent)
        }
        else{
            Toast.makeText(this, "Sorry, there is no YouTube video for this trick", Toast.LENGTH_SHORT).show()
        }
    }

    fun restartGame(view: View){
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Restart Game?")
        builder.setMessage("All player names and scores will be deleted. Are you sure you want to continue?")
        builder.setIcon(android.R.drawable.ic_delete)
        builder.setPositiveButton("Yes"){dialog, which ->
            sharedPreferences.edit().clear().commit()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        builder.setNegativeButton("No"){dialog, which -> }
        val dialog = builder.create()
        dialog.show()
    }

}