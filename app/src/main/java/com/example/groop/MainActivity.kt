package com.example.groop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.groop.Util.isEmail
import com.example.groop.Util.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var state = true // Login
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switch_button.setOnClickListener{switch()}
        finish_button.setOnClickListener{login()}
    }

    fun switch(){
        TransitionManager.beginDelayedTransition(login_screen)
        when(state){
            true -> {
                state = false
                log_status.text = "Sign Up"
                switch_button.text = "Login"
                finish_button.text = "Sign Up"
                finish_button.setOnClickListener{signup()}
                finish_button.setOnClickListener{signup()}
                confirm_password_container.visibility = View.VISIBLE
            }
            else -> {
                state = true
                log_status.text = "Login"
                switch_button.text = "Sign Up"
                finish_button.text = "Login"
                finish_button.setOnClickListener{login()}
                confirm_password_container.visibility = View.GONE
            }
        }
    }

    fun login(){
        val email = user_email.text.toString()
        if (!isEmail(email)) {
            toast(this, "Please enter valid email")
        }
        val password = user_password.text.toString()
/*
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
            if (it.isSuccessful){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            else {
                toast(this, "sign in failed")
            }
        }
        */
    }

    fun signup(){
        var TAG: String = "emailPassword"
        var email = user_email.text as String
        var password = user_password.text as String
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")

                    //add the new user to the database
                    addUser(email)

                    val user = auth.currentUser
                   // updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                   // updateUI(null)
                }

                // [START_EXCLUDE]
                // hideProgressDialog() //maybe put back in
                // [END_EXCLUDE]
            }

    }
/*
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("username", user?.email)
            startActivity(intent)
        }
        */
    }
    private fun addUser(email: String) {
        var TAG:String="addUser"
        // add user to database, indexed by email, including win and loss count (both 0)
        val newUserInfo = HashMap<String, Any>()
        newUserInfo["winCount"] = 0
        newUserInfo["lossCount"] = 0
        newUserInfo["moneyCount"]=100

        //users are indexed by their email
        //so we create a new document with the email as the ID
        //NOTE: overrides any previous account data that the
        // user might have
        /*
        db.collection(Dashboard.usersPath).document(email)
            .set(newUserInfo)
            .addOnSuccessListener {
                Log.d(TAG, "New user added successfully")
            }
            .addOnFailureListener {
                Log.d(TAG, "New user could not be added")
            }*/
    }

//}
