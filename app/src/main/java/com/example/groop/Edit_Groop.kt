package com.example.groop

import android.content.Intent
import android.os.Bundle
import android.text.Editable
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
import kotlinx.android.synthetic.main.activity_edit_groop.*
import kotlinx.android.synthetic.main.create_groop.*
import kotlinx.android.synthetic.main.join_groop.*
import java.util.*

class Edit_Groop: AppCompatActivity() {

    private var activity_list: ArrayList<String> = ArrayList()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var gl=GroopLocation(this@Edit_Groop)
    private var username=auth.currentUser!!.email!!
    private var name = ""
    private lateinit var this_groop:Groop
    private lateinit var docRef: DocumentReference
    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO needs to be finished
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_groop)
        addItemsOnSpinner2()
        var groop_id = intent.getStringExtra("groop_id")
        db.collection("groops").document(groop_id).get().addOnSuccessListener { snap->
            this_groop=DBManager.parseGroop(snap)
            edit_groop_bio.text= Editable.Factory.getInstance().newEditable(this_groop.description)
            edit_groop_max_participants.text=Editable.Factory.getInstance().newEditable(this_groop.capacity.toString())
            edit_groop_name.text=Editable.Factory.getInstance().newEditable(this_groop.name.toString())
            edit_groop_starttime.text=Editable.Factory.getInstance().newEditable(this_groop.startTime.toString())
            //TODO set spinner to current category
        }

        db.collection("user").document(username).get().addOnSuccessListener { snap ->
            name = snap.get("name").toString()
            docRef = snap.reference
        }

        edit_groop_btn.setOnClickListener() {
            gl.pickLocation()
        }
        edit_groop_starttime.setOnClickListener {
            var newFragment: DialogFragment = DatePickerFragment()
            newFragment.show(getSupportFragmentManager(), "Date Picker")
        }
    }



override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    var address=gl.getAddress(requestCode,resultCode,data)
    var location= gl.getGeoPoint(requestCode,resultCode,data)
    if(address!=null && location!=null) {
        if (edit_groop_spinner2.getSelectedItem() != null) {
            this_groop.type = edit_groop_spinner2.getSelectedItem().toString()
            if (edit_groop_max_participants.text.toString() != "") {
               this_groop.capacity = edit_groop_max_participants.text.toString() as Int
            }
            this_groop.description = edit_groop_bio.text.toString()
            this_groop.startTime = edit_groop_starttime.text as Date
            DBManager.editGroop(this_groop)
            val intent = Intent(this@Edit_Groop, home::class.java)
            startActivity(intent)
        }
    }
}
//credit to https://www.mkyong.com/android/android-spinner-drop-down-list-example/
private fun addItemsOnSpinner2() {

    var spinner2 = findViewById(R.id.edit_groop_spinner2) as Spinner;
    var list:ArrayList<String> = ArrayList();
    db.collection("activities").get().addOnSuccessListener { snapshot ->
        activity_list= DBManager.getAllActivities(snapshot)
        for(act in activity_list){
            list.add(act)
            var dataAdapter: ArrayAdapter<String> = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(dataAdapter);
        }
    }


}

}