package com.example.groop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.groop.DataModels.User
import com.example.groop.HomePackage.home
import com.example.groop.Util.DBManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_interest_edit.*
import kotlinx.android.synthetic.main.join_groop.*

class groop_info: AppCompatActivity() {
    //TODO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join_groop)
        //val intent = Intent()
        //val extras = intent.extras
        val auth = FirebaseAuth.getInstance()
        // var user = intent.extras.get("user") as User
        //val user = User("telemonian@gmail.com", "Billiamson McGee", GeoPoint(1.1, 0.0), "")
        var username = auth.currentUser!!.email as String


        join_groop_btn.setOnClickListener {
            var bio = ""+home_activity_edit_description.text
            //DBManager.joinGroop
            val intent = Intent(this@groop_info, home::class.java)
            intent.putExtra("user", username)
            startActivity(intent)
        }
        messege_groop_btn.setOnClickListener {
            val intent = Intent(this@groop_info, home::class.java)
            intent.putExtra("user", username)
            startActivity(intent)
        }
    }
    //TODO
}