package com.example.groop.HomePackage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.groop.DataModels.User
import com.example.groop.R
import com.example.groop.Util.DBManager
import com.example.groop.Util.DBManager.Paths.updateActivityInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
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
        var username = auth.currentUser!!.email as String
        home_activity_edit_name.text=activity

        home_activity_edit_button.setOnClickListener {
            var bio = ""+home_activity_edit_description.text
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