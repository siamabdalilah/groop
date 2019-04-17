package com.example.groop.HomePackage


import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import 	androidx.recyclerview.widget.RecyclerView


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.groop.DataModels.User
import com.example.groop.LocationServices
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.home_display.*


import com.example.groop.R
import com.example.groop.Util.DBManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class home : AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var adapter = HomeAdapter()
    private var activity_list: ArrayList<String> =ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_display)
        val fragmentAdapter = MyPagerAdapter(supportFragmentManager, this)
        //var pager=findViewById<view_pager>(R.id.viewpager_home)
        findViewById<ViewPager>(R.id.viewpager_home).adapter=fragmentAdapter
       // viewpager_home.adapter = fragmentAdapter
        tabs_home.setupWithViewPager(viewpager_home)
    }


    //recycler view adapter
    inner class HomeAdapter : RecyclerView.Adapter<HomeAdapter.JokeViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): JokeViewHolder {
            val itemView = LayoutInflater.from(p0.context).inflate(R.layout.home_recycler_frag, p0, false)
            return JokeViewHolder(itemView)
        }

        //what to do with each element
        override fun onBindViewHolder(p0: JokeViewHolder, p1: Int) {
            //gets joke aka song from the songlist and fills the fields of the itemView
            //with the song data from the array
            val joke = activity_list[p1]
            p0.category_text.text = joke

        }


        override fun getItemCount(): Int {
            return activity_list.size
        }


        inner class JokeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var category_text: TextView = itemView.findViewById(R.id.category_text)
            var row = itemView

        }
    }
    class MyPagerAdapter(fm: FragmentManager, context: Context) : FragmentPagerAdapter(fm) {
        var context=context
        val intent = Intent()
       // var user = intent.getSerializableExtra("user") as User
        val user = User("telemonian@gmail.com", "Billiamson McGee", GeoPoint(1.1, 0.0), "")
        //var locationTemp = LocationServices.getLocation(context)
        //user.location = GeoPoint(locationTemp.latitude,locationTemp.longitude)

        //gets which tab you are on and calls that method to inflate the fragment
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    home_activity_view.home(context, user)
                }
                else -> home_groop_view.home(context, user)
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> "HOME"
                else -> {
                    return "My Activites"
                }
            }
        }
    }
}
