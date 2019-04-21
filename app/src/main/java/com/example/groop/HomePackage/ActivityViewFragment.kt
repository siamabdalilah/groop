package com.example.groop.HomePackage


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.groop.R
import com.example.groop.Util.DBManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_activity_view.*
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class ActivityViewFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private var username = ""
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var adapter = ActivityRecyclerAdapter()
    private var activity_list = ArrayList<String>()
    private var activity_list_temp = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_view, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        username = auth.currentUser!!.email!!

        home_recycler.layoutManager = LinearLayoutManager(context)
        home_recycler.adapter = adapter

        db.collection("activities").get().addOnSuccessListener { snapshot ->
            activity_list = DBManager.getAllActivities(snapshot)
            activity_list_temp.addAll(activity_list)
            adapter.notifyDataSetChanged()
        }

        activity_search_home.setOnFocusChangeListener { _, hasFocus ->
            var searchBy = "" + activity_search_home.text
            if (!hasFocus) {
                if (searchBy != "") {
                    activity_list.clear()
                    if (activity_list_temp.contains(searchBy)) {
                        activity_list.add(searchBy)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    activity_list = activity_list_temp

                }
                adapter.notifyDataSetChanged()
            }
        }
    }


    inner class ActivityRecyclerAdapter : RecyclerView.Adapter<ActivityRecyclerAdapter.ActivityHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ActivityHolder {
            val itemView = LayoutInflater.from(p0.context).inflate(R.layout.recycler_view_item, p0, false)
            return ActivityHolder(itemView)
        }

        override fun onBindViewHolder(p0: ActivityHolder, p1: Int) {
            val activity = activity_list[p1]
            p0.category_text.text = activity
            p0.row.setOnClickListener {

                val intent = Intent(p0.itemView.context, edit_activity_info::class.java)
                intent.putExtra("activity", activity)
                startActivity(intent)
            }

        }


        override fun getItemCount(): Int {
            return activity_list.size
        }


        inner class ActivityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var category_text: TextView = itemView.category_text
            var row = itemView

        }
    }
}
