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
import com.example.groop.DataModels.groop
import com.example.groop.LocationServices
import com.example.groop.R
import com.example.groop.Util.DBManager
import com.example.groop.Util.Groop
import com.example.groop.Util.findDistance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.home_groop_view.*
import kotlinx.android.synthetic.main.home_recycler_frag.*


class home_groop_view : AppCompatActivity(){

    @SuppressLint("ValidFragment")
    class home(contexter: Context, user: User) : Fragment() {

        private val myLoc = LocationServices.getLocation(this.context as Context)
        private val user= user
        private val username = user.email
        private val auth = FirebaseAuth.getInstance()
        private var adapter = HomeAdapter()
        private var joined_groops: ArrayList<Groop> = ArrayList()
        private var created_groops: ArrayList<Groop> = ArrayList()
        private var my_groops: ArrayList<Groop> = ArrayList()
        private var activity_list_temp: ArrayList<Groop> = ArrayList()


        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.home_groop_view, container, false)
        }


        override fun onStart() {
            super.onStart()
            search_by_distance.visibility= View.GONE
            home_groops_recycler.layoutManager = LinearLayoutManager(context)
            home_groops_recycler.adapter = adapter
            DBManager.getGroopsBy(user.email,{this::GetCreatedGroops})
            DBManager.getGroopsJoinedBy(user.email,{this::GetJoinedArray})
            my_groops.addAll(created_groops)
            my_groops.addAll(joined_groops)
            adapter.notifyDataSetChanged()
            var searchBy: String = activity_search_home.text as String
            activity_search_home.setOnFocusChangeListener { v, hasFocus ->
                var searchBy = activity_search_home.text as String
                if(!hasFocus){
                    activity_list_temp=my_groops
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
        }

        fun GetJoinedArray(arr:ArrayList<Groop>){
            joined_groops=arr
        }

        fun GetCreatedGroops(arr:ArrayList<Groop>){
            created_groops=arr
        }

        fun groopsContainsActivity(g:ArrayList<groop>, category:String):ArrayList<groop>{
            var result: ArrayList<groop> = ArrayList()
            for(p in g){
                if(p.getCategory()==category){
                    result.add(p)
                }
            }
            return result
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
                    search_by_distance.visibility= View.VISIBLE
                    val intent = Intent(p0.itemView.context, com.example.groop.groop_info::class.java)
                    intent.putExtra("activity", activity)
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
}