package com.example.groop

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.view_users_activity.*
import java.util.ArrayList

class View_Users_Activity:AppCompatActivity() {

    private var activity_list: ArrayList<String> = ArrayList()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var username:String
    private lateinit var docRef: DocumentReference
    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO needs to be finished
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_users_activity)

        var activity=intent.getStringExtra("activity")
         username = intent.getStringExtra("username")
        activity_name_user_view.text=activity

        db.collection("users").document(username).collection("activities")
            .document(activity).get().addOnSuccessListener { snap->
            bio_user_view.text= Editable.Factory.getInstance().newEditable(snap.get("description").toString())
                bio_user_view.setInputType(InputType.TYPE_NULL)
                bio_user_view.setTextIsSelectable(false)
                bio_user_view.setEnabled(false);
            }
    }
}