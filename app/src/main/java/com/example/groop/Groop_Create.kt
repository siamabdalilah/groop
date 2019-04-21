package com.example.groop

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.groop.HomePackage.home
import com.example.groop.Util.DBManager
import com.example.groop.Util.Groop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.create_groop.*
import kotlinx.android.synthetic.main.home_display.*
import kotlinx.android.synthetic.main.join_groop.*
import java.util.*

import android.app.Activity;
import android.view.View;
import java.time.LocalDate


class Groop_Create: AppCompatActivity() {
    private var activity_list: ArrayList<String> = ArrayList()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var gl=GroopLocation(this@Groop_Create)
    private var username=auth.currentUser!!.email!!
    private var name = ""
    private lateinit var docRef: DocumentReference
    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO needs to be finished
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_groop)
        addItemsOnSpinner2()
        db.collection("user").document(username).get().addOnSuccessListener { snap->
            name=snap.get("name").toString()
            docRef=snap.reference
        }

        create_id.setOnClickListener(){
            gl.pickLocation()
        }
        starttime_id.setOnClickListener {
            var newFragment:DialogFragment= DatePickerFragment()
            newFragment.show(getSupportFragmentManager(),"Date Picker")
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var address=gl.getAddress(requestCode,resultCode,data)
        var location= gl.getGeoPoint(requestCode,resultCode,data)
        if(address!=null && location!=null) {
            if (spinner2.getSelectedItem() != null) {
                var activityU = spinner2.getSelectedItem().toString()
                var capacity = 0
                if (max_participants_id.text.toString() != "") {
                    capacity = max_participants_id.text.toString().toInt()
                } else {
                    capacity = 10
                }
                var createdBy =username
                var creatorName = name
                var members: ArrayList<DocumentReference> = ArrayList()
                members.add(docRef)
                var description = jgroop_bio.text.toString()
                var startTime = LocalDate.parse(starttime_id.text.toString())
                val groop = Groop(capacity, createdBy,creatorName,description,location,members,
                    name,1,startTime,activityU,address=address)
                DBManager.createGroop(groop)
                val intent = Intent(this@Groop_Create, home::class.java)
                startActivity(intent)
            }
        }
    }
    //credit to https://www.mkyong.com/android/android-spinner-drop-down-list-example/
    private fun addItemsOnSpinner2() {

        var spinner2 = findViewById(R.id.spinner2) as Spinner;
        var list:ArrayList<String> = ArrayList();
        db.collection("activities").get().addOnSuccessListener { snapshot ->
            activity_list= DBManager.getAllActivities(snapshot)
            for(act in activity_list){
                list.add(act)
                var dataAdapter:ArrayAdapter<String>  = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(dataAdapter);
            }
        }


    }
}