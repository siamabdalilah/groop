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
import com.example.groop.R
import com.example.groop.groop
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.home_groop_view.*
import kotlinx.android.synthetic.main.home_recycler_frag.*

class home_groop_view : AppCompatActivity(){

    @SuppressLint("ValidFragment")
    class home(contexter: Context, username: String) : Fragment() {

        private val username = username
        private val auth = FirebaseAuth.getInstance()
        private var adapter = HomeAdapter()
        private var joined_groops: ArrayList<groop> = ArrayList()
        private var created_groops: ArrayList<groop> = ArrayList()
        private var my_groops: ArrayList<groop> = ArrayList()
        private var activity_list_temp: ArrayList<groop> = ArrayList()


        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.home_groop_view, container, false)
        }

        override fun onStart() {
            super.onStart()
            home_groops_recycler.layoutManager = LinearLayoutManager(context)
            home_groops_recycler.adapter = adapter
            created_groops=DBManager.getGroopsBy(username);
            joined_groops=DBManager.getGroopsJoinedBy(username);
            my_groops.addAll(created_groops)
            my_groops.addAll(joined_groops)
            adapter.notifyDataSetChanged()
            var searchBy: String = activity_search_home.text as String
            activity_search_home.setOnFocusChangeListener { v, hasFocus ->
                var searchBy = activity_search_home.text as String
                if(!hasFocus){
                    activity_list_temp=my_groops
                    if(searchBy!=""){
                        my_groops=groopsContainsActivity(activity_list_temp,searchBy)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
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
                p0.name.text = "Name: "+activity.getName()
                p0.curParticiants.text="Currently: "+activity.getMembers().size.toString()
                p0.participants.text = "Participants: "+activity.getCapacity().toString()
                p0.distance.text="Distance: "+findDistance(activity.getLocation(),myLoc)
                p0.time.text="Time: "+activity.getStartTime().toString()
                p0.row.setOnClickListener {
                    val intent = Intent(p0.itemView.context, com.example.groop.groop_info::class.java)
                    intent.putExtra("activity", activity)
                    intent.putExtra("user", username)
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