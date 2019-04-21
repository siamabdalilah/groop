package com.example.groop.DataModels

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.groop.GroopLocation
import com.example.groop.Groop_Join
import com.example.groop.R
import com.example.groop.Util.DBManager
import com.example.groop.Util.Groop
import com.example.groop.Util.findDistance
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.groop_card.view.*
import kotlin.math.roundToInt

class GroopListAdapter(var groops: ArrayList<Groop>, val activity: AppCompatActivity) : RecyclerView.Adapter<GroopListAdapter.GroopCardHolder>(){
    private val gl = GroopLocation(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroopCardHolder {
        Log.d("gettinggroops", "getItemcount called")
        return GroopCardHolder(LayoutInflater.from(activity).inflate(R.layout.groop_card, parent, false))
    }

    override fun getItemCount(): Int {
        Log.d("gettinggroops", "getItemcount called")
        return groops.size
    }

    override fun onBindViewHolder(holder: GroopCardHolder, position: Int) {
        val g = groops[position]

        val loc = gl.getLocation()

        holder.activityName.text = g.type
        holder.creator.text = g.creatorName
        holder.distance.text = truncateDist(findDistance(GeoPoint(loc.latitude, loc.longitude), g.location))
        holder.groopName.text = g.name
        holder.numParticipants.text = g.members?.size.toString() // guessing g.numMembers is something different
        holder.location.text = g.address

        holder.viewHolder.setOnClickListener{
            val intent = Intent(activity, Groop_Join::class.java)
            intent.putExtra("this_groop", g.id)
            activity.startActivity(intent)
        }
    }

    inner class GroopCardHolder(view: View) : RecyclerView.ViewHolder(view) {
        val creator = view.created_by
        val groopName = view.groop_name
        val activityName = view.activity_name
        val numParticipants = view.num_participants
        val distance = view.groop_distance
        val location = view.loc_name
        val viewHolder = view

    }

    private fun truncateDist(dist: Double) : String{
        var d = dist
        if (dist > 100){
            d = d.roundToInt() + 0.0
        }

        if (d > 1000){
            d /= 1000
            return d.roundToInt().toString() + "k"
        }
        return d.roundToInt().toString() + "k"
    }

}

