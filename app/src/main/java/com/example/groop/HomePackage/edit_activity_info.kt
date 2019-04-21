package com.example.groop.HomePackage

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import com.example.groop.R
import com.example.groop.Util.DBManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_interest_edit.*

class edit_activity_info : AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest_edit)
        //val extras = intent.extras
        val auth = FirebaseAuth.getInstance()
        var username = auth.currentUser!!.email!!

        var activity: String = intent.getStringExtra("activity")
        db.collection("users").document(username).collection("activities")
            .document(activity).get().addOnSuccessListener { snap ->
                home_id.text = Editable.Factory.getInstance().newEditable(snap.get("description").toString())
            }
        // var user = intent.extras.get("user") as User
        // val user = User("telemonian@gmail.com", "Billiamson McGee", GeoPoint(1.1, 0.0), "")

        home_activity_edit_name.text = activity

        create_id.setOnClickListener {
            var bio = "" + home_id.text
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