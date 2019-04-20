package com.example.groop.HomePackage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.groop.R
import com.example.groop.Util.DBManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_interest_edit.*

class edit_activity_info: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest_edit)
        val intent = Intent()
        //val extras = intent.extras
        val auth = FirebaseAuth.getInstance()
        var activity: String = intent.getStringExtra("activity")
       // var user = intent.extras.get("user") as User
       // val user = User("telemonian@gmail.com", "Billiamson McGee", GeoPoint(1.1, 0.0), "")
        var username = auth.currentUser!!.email!!
        home_activity_edit_name.text=activity

        create_id.setOnClickListener {
            var bio = ""+home_id.text
            DBManager.updateActivityInfo(username, activity, bio)
            val intent = Intent(this@edit_activity_info, home::class.java)
            intent.putExtra("user", username)
            startActivity(intent)
        }
        home_activity_edit_back_button.setOnClickListener {
            val intent = Intent(this@edit_activity_info, home::class.java)
            intent.putExtra("user", username)
            startActivity(intent)
        }
    }
}