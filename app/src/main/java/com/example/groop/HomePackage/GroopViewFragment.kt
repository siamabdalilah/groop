package com.example.groop.HomePackage


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groop.DataModels.GroopListAdapter

import com.example.groop.R
import com.example.groop.Util.DBManager
import com.example.groop.Util.Groop
import com.example.groop.Util.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.home_groop_view.*
import javax.annotation.RegEx


class GroopViewFragment : Fragment() {

    private val myLoc = GeoPoint(0.0, 0.0)//TODO GroopLocation.getLocation(this.context as Context)
    private val auth = FirebaseAuth.getInstance()
    private var userEmail = auth.currentUser!!.email!!

//    private var joined_groops: ArrayList<Groop> = ArrayList()
//    private var created_groops: ArrayList<Groop> = ArrayList()

    private var myGroops: ArrayList<Groop> = ArrayList()
    private lateinit var adapter: GroopListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groop_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userEmail = auth.currentUser!!.email!!
        myGroops.add(Groop())
        adapter = GroopListAdapter(myGroops, activity as AppCompatActivity)


        home_groops_recycler.layoutManager = LinearLayoutManager(activity)
        home_groops_recycler.adapter = adapter


        DBManager.getGroopsBy(userEmail, this::getCreatedGroops) //TODO this is not working
        DBManager.getGroopsJoinedBy(userEmail, this::getJoinedArray)
        adapter.notifyDataSetChanged()


        search_by_category.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do Nothing
            }

            override fun afterTextChanged(s: Editable?) {
                val searchBy = s.toString().toLowerCase().replace(",", " ")
                    .replace(Regex("[ ]+"), " ").replace(Regex("[^a-z0-9 ]"), "")
                    .trim()

                if (searchBy == ""){
                    adapter.groops = myGroops
                }else{
                    adapter.groops = myGroops.filter{
                        if (it.creatorName?.toLowerCase()?.startsWith(searchBy) ?: false){
                            return@filter true
                        }

                        if (it.name.toLowerCase().matches(Regex(".*$searchBy.*"))){
                            return@filter true
                        }

                        if (it.type.toLowerCase().matches(Regex(".*$searchBy.*"))){
                            return@filter true
                        }
                        /*
                        if (it.address.toLowerCase().matches(Regex(searchBy))){
                            return@filter true
                        }
                        */

                        false
                    } as ArrayList<Groop>
                }
                adapter.notifyDataSetChanged()
            }

        })
    }

    fun getJoinedArray(arr: ArrayList<Groop>) {
        myGroops.addAll(arr)
        Log.d("gettinggroops", myGroops.toString())
        Log.d("gettinggroops", adapter.groops.toString())
        adapter.notifyDataSetChanged()

    }

    fun getCreatedGroops(arr: ArrayList<Groop>) {
        myGroops.addAll(arr)
        adapter.notifyDataSetChanged()
    }


}
