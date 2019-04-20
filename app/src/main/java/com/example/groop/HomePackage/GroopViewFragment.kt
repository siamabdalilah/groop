package com.example.groop.HomePackage


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groop.DataModels.GroopListAdapter

import com.example.groop.R
import com.example.groop.Util.DBManager
import com.example.groop.Util.Groop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.home_groop_view.*


class GroopViewFragment : Fragment() {

    private val myLoc = GeoPoint(0.0,0.0)//TODO GroopLocation.getLocation(this.context as Context)
    private val auth = FirebaseAuth.getInstance()
    //        private val username = auth.currentUser!!.email as String // commented because of merge conflict -- siam
    private var username = ""

    private var joined_groops: ArrayList<Groop> = ArrayList()
    private var created_groops: ArrayList<Groop> = ArrayList()
    private var my_groops: ArrayList<Groop> = ArrayList()
    private var activity_list_temp: ArrayList<Groop> = ArrayList()
    private lateinit var adapter : GroopListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groop_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        username=auth.currentUser!!.email!!
        adapter = GroopListAdapter(my_groops, activity as Context)

        search_by_distance.visibility= View.GONE
        textView2.visibility=View.GONE
        home_groops_recycler.layoutManager = LinearLayoutManager(context)
        home_groops_recycler.adapter = adapter

        //DBManager.getGroopsBy(username,this::GetCreatedGroops) //TODO this is not working
        DBManager.getGroopsJoinedBy(username,this::GetJoinedArray)
        adapter.notifyDataSetChanged()


        search_by_category.setOnFocusChangeListener { v, hasFocus ->
            var searchBy = ""+search_by_category.text
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
        my_groops.addAll(arr)
        adapter.notifyDataSetChanged()
    }

    fun GetCreatedGroops(arr:ArrayList<Groop>){
        my_groops.addAll(arr)
        adapter.notifyDataSetChanged()
    }


}
