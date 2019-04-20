package com.example.groop


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groop.DataModels.User
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.home_groop_view.*
import com.example.groop.Util.*
import com.example.groop.Util.DBManager.Paths.getAllGroops
import com.google.firebase.firestore.FirebaseFirestore


class display_groops : AppCompatActivity(){

    private val myLoc = GeoPoint(0.0,0.0)//TODO GroopLocation.getLocation(this)
//    private val user= intent.getSerializableExtra("user") as User //TODO
val user = User("telemonian@gmail.com", "Billiamson McGee", GeoPoint(1.1, 0.0), "")
    private val username = user.email
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var adapter = HomeAdapter()
    private var joined_groops: ArrayList<Groop> = ArrayList()
    private var created_groops: ArrayList<Groop> = ArrayList()
    private var my_groops: ArrayList<Groop> = ArrayList()
    private var activity_list_temp: ArrayList<Groop> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_groop_view)

            home_groops_recycler.layoutManager = LinearLayoutManager(this)
            home_groops_recycler.adapter = adapter
            var locationTemp = GeoPoint(0.0,0.0)//TODO GroopLocation.getLocation(this)
            user.location= GeoPoint(locationTemp.latitude,locationTemp.longitude)
            db.collection("groops").get().addOnSuccessListener { snapshot ->
                my_groops=getAllGroops(snapshot)
                adapter.notifyDataSetChanged()
            }
            //my_groops=DBManager.getSortedGroopList(my_groops,user.location)
            activity_list_temp=my_groops
            adapter.notifyDataSetChanged()
            search_by_category.setOnFocusChangeListener { v, hasFocus ->
                var searchBy = ""+search_by_category.text
                if(!hasFocus){
                    if(searchBy!=""){
                        my_groops.clear()
                        for(grp in activity_list_temp){
                            if(grp.type==searchBy){
                                my_groops.add(grp)
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        search_by_distance.setOnFocusChangeListener { v, hasFocus ->
            var searchBy = (""+search_by_distance.text).toIntOrNull()

            if(!hasFocus){
                if(searchBy!=null){
                    my_groops.clear()
                    for(grp in activity_list_temp){
                        if(findDistance(grp.location,user.location)<=searchBy){
                            my_groops.add(grp)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }
        }
    }




        //recycler view adapter
        inner class HomeAdapter : RecyclerView.Adapter<HomeAdapter.JokeViewHolder>() {

            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): JokeViewHolder {
                val itemView = LayoutInflater.from(p0.context).inflate(R.layout.home_groop_recycler_item, p0, false)
                return JokeViewHolder(itemView)
            }

            //what to do with each element
            override fun onBindViewHolder(p0: JokeViewHolder, p1: Int) {
                //gets joke aka song from the songlist and fills the fields of the itemView
                //with the song data from the array
                val activity = my_groops[p1]
                p0.name.text = "Name: "+activity.name
                p0.curParticiants.text="Currently: "+activity.members!!.size.toString()
                p0.participants.text = "Participants: "+activity.capacity.toString()
                var lng = myLoc.longitude*10000000
                var lat = myLoc.latitude*1000000
                p0.distance.text="Distance: "+findDistance(activity.location, GeoPoint(lat,lng))
                p0.time.text="Time: "+activity.startTime.toString()
                p0.row.setOnClickListener {
                    val intent = Intent(p0.itemView.context, com.example.groop.Groop_Join::class.java)
                    intent.putExtra("this_groop", activity.id)
                    intent.putExtra("user", user)
                    startActivity(intent)
                }

            }


            override fun getItemCount(): Int {
                return my_groops.size
            }


            inner class JokeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                var name: TextView = itemView.findViewById(R.id.home_groop_name)
                var participants: TextView = itemView.findViewById(R.id.home_groop_participants)
                var curParticiants: TextView = itemView.findViewById(R.id.home_groop_currParticipants)
                var distance: TextView = itemView.findViewById(R.id.home_groop_distance)
                var time: TextView = itemView.findViewById(R.id.home_groop_time)
                var row = itemView

            }
        }



}