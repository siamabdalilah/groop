package com.example.groop.DataModels

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.groop.R
import com.example.groop.Util.Groop
import kotlinx.android.synthetic.main.groop_card.view.*

class GroopListAdapter(var groops: ArrayList<Groop>, val context: Context) : RecyclerView.Adapter<GroopListAdapter.GroopCardHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroopCardHolder {
        Log.d("gettinggroops", "getItemcount called")
        return GroopCardHolder(LayoutInflater.from(parent.context).inflate(R.layout.groop_card, parent, false))
    }

    override fun getItemCount(): Int {
        Log.d("gettinggroops", "getItemcount called")
        return groops.size
    }

    override fun onBindViewHolder(holder: GroopCardHolder, position: Int) {
        val g = groops[position]

        holder.activityName.text = g.type
        holder.creator.text = g.creatorName
        holder.distance.text = "" + 0 // nope
        holder.groopName.text = g.name
        holder.numParticipants.text = g.members?.size.toString() // guessing g.numMembers is something different
        holder.location.text = "St Louis, MO" //nope
    }

    inner class GroopCardHolder(view: View) : RecyclerView.ViewHolder(view) {
        val creator = view.created_by
        val groopName = view.groop_name
        val activityName = view.activity_name
        val numParticipants = view.num_participants
        val distance = view.groop_distance
        val location = view.loc_name

    }

}

