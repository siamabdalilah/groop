package com.example.groop

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.groop.Util.DBManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.create_groop.*

class Groop_Create: AppCompatActivity() {
    private var activity_list: ArrayList<String> = ArrayList()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO needs to be made
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_groop)
        addItemsOnSpinner2()


        spinner2.getSelectedItem().toString()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }
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