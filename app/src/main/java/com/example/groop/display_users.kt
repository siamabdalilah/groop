package com.example.groop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groop.DataModels.User
import com.example.groop.Util.DBManager
import com.example.groop.Util.findDistance
import com.example.groop.Util.setupNav
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.display_users.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_display.*
import java.util.*

class display_users: AppCompatActivity() {
    private val myLoc = GeoPoint(0.0,0.0)//TODO GroopLocation.getLocation(this, LocationServices.getFusedLocationProviderClient(this))
    //private val user= intent.getSerializableExtra("user") as User

    private val auth = FirebaseAuth.getInstance()
    private val username = auth.currentUser!!.email!!
    private lateinit var user:User
    private var adapter = HomeAdapter() // display_groops.HomeAdapter() -> display_groops().HomeAdapater() for compilation --siam
    private var my_groops: ArrayList<User> = ArrayList()
    private var activity_list_temp: ArrayList<User> = ArrayList()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_users)
        user_display_recycler.layoutManager = LinearLayoutManager(this)
        user_display_recycler.adapter = adapter
        var locationTemp = GeoPoint(0.0,0.0)//TODO GroopLocation.getLocation(this, LocationServices.getFusedLocationProviderClient(this))
        db.collection("users").document(username).get().addOnSuccessListener { it ->
            var bio = it.get("bio").toString()
            var name = it.get("name").toString()
            var location = it.getGeoPoint("location") as GeoPoint
            user = User(username, name, location, bio)
            user.location = GeoPoint(locationTemp.latitude, locationTemp.longitude)


            db.collection("users").get().addOnSuccessListener { snapshot ->
                my_groops = DBManager.getAllUsers(snapshot)
                my_groops.remove(user)
                activity_list_temp.addAll(my_groops)
                adapter.notifyDataSetChanged()
            }
        }
//        var searchBy: String = user_search_by_category.text as String
        user_search_by_category.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
               searchBy()
            }
        }
        user_search_by_distance.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                searchBy()
            }
        }
    }


    fun searchBy(){
        my_groops.clear()
        my_groops.addAll(activity_list_temp)
        var searchByDist = user_search_by_distance.text.toString().toIntOrNull()
        if(searchByDist!=null){
            my_groops.clear()
            for(usr in my_groops){
                if(findDistance(usr.location,user.location) <=searchByDist){
                    my_groops.remove(usr)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        adapter.notifyDataSetChanged()

        var searchByCat = "" + user_search_by_category.text
        if(searchByCat!=""){
            //checking if each user is interested in the activity
            for(usr in my_groops){
                var hasActivity=false
                //for each activity in the given user check if he is interested in the specified activity
                for(activityT in usr.activities){
                    if(activityT.name==searchByCat){
                        hasActivity=true
                    }
                }
                if(!hasActivity){
                    my_groops.remove(usr)
                }
            }
        }
        adapter.notifyDataSetChanged()
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
            Picasso.with(this@display_users).load(user_viewed.profilePicture).into(p0.img)
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