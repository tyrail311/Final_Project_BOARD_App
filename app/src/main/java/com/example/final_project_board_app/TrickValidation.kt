package com.example.final_project_board_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

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

    lateinit var continueButton: Button
    lateinit var p1_no: Button
    lateinit var p1_yes: Button
    lateinit var p2_no: Button
    lateinit var p2_yes: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trick_validation)

        val link = intent.getStringExtra("link")?: ""
        val trick = intent.getStringExtra("trick")?: ""
        val id = intent.getIntExtra("id", -1)

        Log.d(TAG, "link: $link, trick: $trick, id: $id")
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        player1score = sharedPreferences.getString("player1score", "") ?: ""
        player2score = sharedPreferences.getString("player2score", "") ?: ""
        player1count = sharedPreferences.getInt("player1count", 0)
        player2count = sharedPreferences.getInt("player2count", 0)
        player1turn =  sharedPreferences.getBoolean("player1turn", true)
        player1 = sharedPreferences.getString("player1", "")?: ""
        player2 = sharedPreferences.getString("player2", "")?: ""

        p1_no = findViewById<Button>(R.id.player1_no)
        p1_yes = findViewById<Button>(R.id.player1_yes)
        p2_no = findViewById<Button>(R.id.player2_no)
        p2_yes = findViewById<Button>(R.id.player2_yes)
        continueButton = findViewById<Button>(R.id.continue_button)

        findViewById<TextView>(R.id.player1_trick_land).text = "Did $player1 land the $trick?"
        findViewById<TextView>(R.id.player2_trick_land).text = "Did $player2 land the $trick?"

        findViewById<TextView>(R.id.player1_score_text).text = player1score
        findViewById<TextView>(R.id.player2_score_text).text = player2score

        checkGameFinish()
        if (player1turn) //Player 1's turn to choose trick
        {
            p2_yes.setVisibility(View.GONE)
            p2_no.setVisibility(View.GONE)
            p1_no.setOnClickListener{
                player1turn = false
                Toast.makeText(this, "$player1 didn't land his trick, onto $player2's turn", Toast.LENGTH_SHORT).show()
                buttonDisable(p1_no)
                buttonDisable(p1_yes)

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
            }
            p2_no.setOnClickListener{
                player2count++
                player2score = updateScore(player2count)
                findViewById<TextView>(R.id.player2_score_text).text = player2score
                buttonDisable(p2_no)
                buttonDisable(p2_yes)
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
            }
            p1_no.setOnClickListener{
                player1count++
                player1score = updateScore(player1count)
                findViewById<TextView>(R.id.player1_score_text).text = player1score
                buttonDisable(p1_no)
                buttonDisable(p1_yes)
                if(player1count == 5)
                {
                    findViewById<TextView>(R.id.gameover_text).text = "GAME OVER! $player2 wins!!"
                    buttonDisable(continueButton)
                }
            }
        }

        continueButton.setOnClickListener{
            sharePref()
            val intent = Intent(this, TrickList::class.java)
            intent.putExtra("player1turn", player1turn)
            startActivity(intent)
        }

    }

    override fun onStop() {
        super.onStop()
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
        editor.apply()
    }

    fun buttonDisable(button: Button){
        button.isEnabled = false
        button.isClickable = false
        button.setTextColor(getApplication().getResources().getColor(R.color.white))
        button.setBackgroundColor(button.getContext().getResources().getColor(androidx.appcompat.R.color.material_grey_800))
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