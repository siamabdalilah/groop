package com.example.groop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.groop.DataModels.User
import com.example.groop.Util.DBManager
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
    private var adapter = HomeAdapter() // display_groops.HomeAdapter() -> display_groops().HomeAdapater() for compilation --siam
    private var my_groops: ArrayList<User> = ArrayList()
    private var activity_list_temp: ArrayList<User> = ArrayList()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_users)
        tabs_home.setupWithViewPager(viewpager_home)
        var intent = Intent()
        var user_viewed_email = intent.getStringExtra("user_viewed_email")
        db.collection("users").document(user_viewed_email).get().addOnSuccessListener { snap->
            user_viewed=DBManager.parseUser(snap)
            user_name_1_1.text=user_viewed.name
            bio_1_1.text=user_viewed.bio

        }

    }
    //recycler view adapter
    inner class HomeAdapter : RecyclerView.Adapter<HomeAdapter.JokeViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): JokeViewHolder {
            val itemView = LayoutInflater.from(p0.context).inflate(R.layout.display_users_recycler_item, p0, false)
            return JokeViewHolder(itemView)
        }

        //what to do with each element
        override fun onBindViewHolder(p0: JokeViewHolder, p1: Int) {
            //gets joke aka song from the songlist and fills the fields of the itemView
            //with the song data from the array
            val user_viewed= my_groops[p1]
            p0.name.text = "Name: "+user_viewed.name
            p0.bio.text="Currently: "+user_viewed.bio
            Picasso.with(this@User_View).load(user_viewed.profilePicture).into(p0.img)
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


        inner class JokeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var img: ImageView = itemView.findViewById(R.id.user_img)
            var name: TextView = itemView.findViewById(R.id.user_name)
            var bio: TextView = itemView.findViewById(R.id.user_bio)
            var row = itemView

        }
    }

}