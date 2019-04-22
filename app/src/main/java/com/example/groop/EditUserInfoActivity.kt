package com.example.groop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import com.example.groop.DataModels.User
import com.example.groop.Util.DBManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_edit_user_info.*

class EditUserInfoActivity : AppCompatActivity() {
    private var user: User? = null
    private lateinit var gl :GroopLocation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_info)

        gl = GroopLocation(this)

        user = DBManager.getUserByEmail(FirebaseAuth.getInstance().currentUser!!.email!!)
        if (user == null) finish()

        edit_user_bio_text.setText(user!!.bio)
        edit_user_bio.setOnClickListener{editBio()}
        update_user_location.setOnClickListener { updateLoc() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var gp = gl.getGeoPoint(requestCode, resultCode, data)

        if (gp != null){
            user!!.location = gp
            DBManager.updateUser(user!!)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun editBio(){
        edit_user_bio_text.isEnabled = true
        edit_user_bio.text = "Done"
        edit_user_bio.setOnClickListener{done()}
    }

    fun done(){
        edit_user_bio_text.isEnabled = false
        edit_user_bio.text = "Edit Bio"

        user!!.bio = edit_user_bio_text.text.toString()
        DBManager.updateUser(user!!)

        edit_user_bio.setOnClickListener { editBio() }
    }

    fun updateLoc(){
        gl.pickLocation(user!!.location)
    }
}
