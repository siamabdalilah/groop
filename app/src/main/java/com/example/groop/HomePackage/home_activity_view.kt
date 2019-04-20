package com.example.groop.HomePackage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groop.DataModels.User
import com.example.groop.GroopLocation
import com.example.groop.R
import com.example.groop.Util.DBManager
import com.example.groop.Util.Groop
import com.example.groop.activity_create
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.home_recycler_frag.*

class home_activity_view: AppCompatActivity() {

    @SuppressLint("ValidFragment")
    class home(contexter: Context) : Fragment() {
        private val auth = FirebaseAuth.getInstance()
        //private val user = user
        private var username = ""
        private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        private var adapter = HomeAdapter()
        private var activity_list: ArrayList<String> = ArrayList()
        private var activity_list_temp: ArrayList<String> = ArrayList()


        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.home_recycler_frag, container, false)
        }

        override fun onStart() {
            super.onStart()
            username =auth.currentUser!!.email!!

            home_recycler.layoutManager = LinearLayoutManager(context)
            home_recycler.adapter = adapter
            db.collection("activities").get().addOnSuccessListener { snapshot ->
                activity_list= DBManager.getAllActivities(snapshot)
                activity_list_temp.addAll(activity_list)
                adapter.notifyDataSetChanged()
            }
            //activity_list= DBManager.getAllActivities();
            adapter.notifyDataSetChanged()
            activity_search_home.setOnFocusChangeListener { v, hasFocus ->
                var searchBy = ""+activity_search_home.text
                if(!hasFocus){
                    if(searchBy!=""){
                        activity_list.clear()
                        if(activity_list_temp.contains(searchBy)){
                            activity_list.add(searchBy)
                            adapter.notifyDataSetChanged()
                        }
                    }
                    else{
                        activity_list=activity_list_temp

                    }
                    adapter.notifyDataSetChanged()
                }
            }

        }


        //recycler view adapter
        inner class HomeAdapter : RecyclerView.Adapter<HomeAdapter.JokeViewHolder>() {

            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): JokeViewHolder {
                val itemView = LayoutInflater.from(p0.context).inflate(R.layout.recycler_view_item, p0, false)
                return JokeViewHolder(itemView)
            }

            //what to do with each element
            override fun onBindViewHolder(p0: JokeViewHolder, p1: Int) {
                //gets joke aka song from the songlist and fills the fields of the itemView
                //with the song data from the array
                val activity = activity_list[p1]
                p0.category_text.text = activity
                p0.row.setOnClickListener {
                    val intent = Intent(p0.itemView.context, edit_activity_info::class.java)
                    intent.putExtra("activity", activity)
                    //intent.putExtra("user", user) //TODO cannot be serialized
                    startActivity(intent)
                }

            }


            override fun getItemCount(): Int {
                return activity_list.size
            }


            inner class JokeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                var category_text: TextView = itemView.findViewById(R.id.category_text)
                var row = itemView

            }
        }

    }
}