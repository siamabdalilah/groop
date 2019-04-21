package com.example.groop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groop.DataModels.GroopListAdapter
import com.example.groop.DataModels.User
import com.example.groop.Util.DBManager
import com.example.groop.Util.Groop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_display.*
import kotlinx.android.synthetic.main.user_viewer.*
import java.util.ArrayList

class User_View : AppCompatActivity() {
    //TODO needs to be made
    private val auth = FirebaseAuth.getInstance()
    private val username = auth.currentUser!!.email!!
    private lateinit var user_viewed: User
    private lateinit var adapter : GroopListAdapter
    private var my_groops: ArrayList<Groop> = ArrayList()
    private var activity_list_temp: ArrayList<String> = ArrayList()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_viewer)
        recycler_1_1.layoutManager = LinearLayoutManager(this@User_View)
        adapter = GroopListAdapter(my_groops,this@User_View)
        recycler_1_1.adapter = adapter
        var lvAdapter=HomeAdapter2()
        listview_1_1.layoutManager=LinearLayoutManager(this@User_View)
        listview_1_1.adapter=lvAdapter

        var user_viewed_email = intent.getStringExtra("user_viewed_email")
        db.collection("users").document(user_viewed_email).get().addOnSuccessListener { snap->
            user_viewed=DBManager.parseUser(snap)
            user_name_1_1.text=user_viewed.name
            bio_1_1.text=user_viewed.bio
            DBManager.getGroopsJoinedBy(user_viewed_email,this::GetJoinedArray)
            db.collection("users").document(user_viewed.email).collection("activities").get().addOnSuccessListener { snap->
                    for(doc in snap.documents){
                        activity_list_temp.add(doc.id)
                        lvAdapter.notifyDataSetChanged()
                    }
                }
        }
        message_1_1.setOnClickListener {
            var intent = Intent(this@User_View,UserChatActivity::class.java)
            intent.putExtra("user", username)
            intent.putExtra("otherUser", user_viewed.email)
            startActivity(intent)
        }
    }
    fun GetJoinedArray(arr:ArrayList<Groop>){
        my_groops.addAll(arr)
        adapter.notifyDataSetChanged()

    }


    inner class HomeAdapter2 : RecyclerView.Adapter<HomeAdapter2.JokeViewHolder2>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): JokeViewHolder2 {
            val itemView = LayoutInflater.from(p0.context).inflate(R.layout.simple_list_item_1, p0, false) //TODO change layout back
            return JokeViewHolder2(itemView)
        }

        //what to do with each element
        override fun onBindViewHolder(p0: JokeViewHolder2, p1: Int) {
            //gets joke aka song from the songlist and fills the fields of the itemView
            //with the song data from the array
            val activity_viewed= activity_list_temp[p1]
            p0.name.text=activity_viewed
            p0.row.setOnClickListener {
                //TODO add this in when user_info added
                //val intent = Intent(p0.itemView.context, user_info::class.java)
                //intent.putExtra("user_viewed", user_viewed)
                //intent.putExtra("user", user)
                //startActivity(intent)
            }

        }


        override fun getItemCount(): Int {
            return my_groops.size
        }


        inner class JokeViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var name: TextView = itemView.findViewById(R.id.arraylist_name)
            var row = itemView

        }
    }

}