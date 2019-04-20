package com.example.groop

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.groop.HomePackage.home
import com.example.groop.Util.DBManager
import com.example.groop.Util.Groop
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_interest_edit.*
import kotlinx.android.synthetic.main.join_groop.*

class groop_join: AppCompatActivity() {
    //TODO
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val username:String= auth.currentUser!!.email!!
    private var user_groops:ArrayList<Groop> = ArrayList()
    private lateinit var this_groop:Groop
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join_groop)
        join_groop_btn.visibility= View.GONE
        val intent = Intent()
        val groop_id = intent.getStringExtra("this_groop")
        db.collection("groops").document(groop_id).get().addOnSuccessListener { snap->
            this_groop=DBManager.parseGroop(snap)
            groop_name.text=this_groop.name
            jgroop_creator.text=this_groop.creator
            jgroop_bio.text=this_groop.description
            jgroop_location.text=this_groop.location.toString()
            jgroop_time.text=this_groop.startTime.toString()
        }
        // var user = intent.extras.get("user") as User
        //val user = User("telemonian@gmail.com", "Billiamson McGee", GeoPoint(1.1, 0.0), "")
        db.collection("user").document(username).get().addOnSuccessListener {snapshot->
            user_groops = snapshot.get("joinGroops") as ArrayList<Groop>
            if(!user_groops.contains(this_groop)){
               join_groop_btn.visibility= View.VISIBLE
            }
        }



        join_groop_btn.setOnClickListener {
            user_groops.add(this_groop)
            db.collection("user").document(username).update("joinedGroops",user_groops)
            val intent = Intent(this@groop_join, display_groops::class.java)
            startActivity(intent)
        }
        messege_groop_btn.setOnClickListener {
            val intent = Intent(this@groop_join, home::class.java)
            intent.putExtra("user", username)
            startActivity(intent)
        }
    }

    //TODO
}