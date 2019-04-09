package com.example.groop.HomePackage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.groop.R
import kotlinx.android.synthetic.main.activity_interest_edit.*

class edit_activity_info: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var username = intent.extras!!.getSerializable("user") as String
        var activity: String = intent.extras!!.getSerializable("activity") as String
        home_activity_edit_name.text=activity

        home_activity_edit_button.setOnClickListener {
            var bio = home_activity_edit_description.text as String
            DBManager.updateActivityInfo(username, activity, bio)
            val intent = Intent(this@edit_activity_info, home_activity_view::class.java)
            intent.putExtra("user", username)
            startActivity(intent)
        }
        home_activity_edit_back_button.setOnClickListener {
            val intent = Intent(this@edit_activity_info, home_activity_view::class.java)
            intent.putExtra("user", username)
            startActivity(intent)
        }
    }
}