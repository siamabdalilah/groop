package com.example.groop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var state = true // Login

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

    }

    fun signup(){

    }

}
