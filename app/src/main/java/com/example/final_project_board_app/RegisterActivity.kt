package com.example.final_project_board_app

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private val SIGN_IN_REQUEST_CODE = 125

    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun loginButton(view: View) {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
            .setLogo(R.drawable.ic_snowflake_snow_svgrepo_com)
            .setAlwaysShowSignInMethodScreen(true) // use this if you have only one provider and really want the see the signin page
            .setIsSmartLockEnabled(false)
            .build()

        // launch the sign-in intent above
        startActivityForResult(intent, SIGN_IN_REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if ( (requestCode == SIGN_IN_REQUEST_CODE) and (resultCode == Activity.RESULT_OK) ) {
            // The user has successfully signed in or he/she is a new user

            val user = FirebaseAuth.getInstance().currentUser
            Log.d(TAG, "onActivityResult: $user")

            if (user?.metadata?.creationTimestamp == user?.metadata?.lastSignInTimestamp) {
                Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show()
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        } else {

            val response = IdpResponse.fromResultIntent(data)
            if (response == null) {
                Log.d(TAG, "onActivityResult: the user has cancelled the sign in request")
            } else {
                Log.e(TAG, "onActivityResult: ${response.error?.errorCode}")
            }
        }
    }

}