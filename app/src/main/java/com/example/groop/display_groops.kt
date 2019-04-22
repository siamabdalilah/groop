package com.example.groop


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groop.DataModels.GroopListAdapter
import com.example.groop.DataModels.User
import com.google.firebase.firestore.GeoPoint
import com.example.groop.Util.*
import com.example.groop.Util.DBManager.Paths.getAllGroops
import com.example.groop.Util.DBManager.Paths.parseUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_display_groops.*


class display_groops : AppCompatActivity(){

    private val myLoc = GeoPoint(0.0,0.0)//TODO GroopLocation.getLocation(this)
    private lateinit var user: User
    //val user = User("telemonian@gmail.com", "Billiamson McGee", GeoPoint(1.1, 0.0), "")
    private val username = user.email
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var joined_groops: ArrayList<Groop> = ArrayList()
    private var created_groops: ArrayList<Groop> = ArrayList()
    private var my_groops: ArrayList<Groop> = ArrayList()
    private var activity_list_temp: ArrayList<Groop> = ArrayList()
    private lateinit var adapter: GroopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_groops)

        adapter = GroopListAdapter(my_groops, this )


        home_groops_recycler2.layoutManager = LinearLayoutManager(this)
        home_groops_recycler2.adapter = adapter



        db.collection("users").document(auth.currentUser?.email ?: "").get()
            .addOnSuccessListener { snap ->
            user = parseUser(snap)
            db.collection("groops").get().addOnSuccessListener { snapshot ->
                my_groops = getAllGroops(snapshot,user.location)
                //my_groops = DBManager.sortGroops(my_groops, user.location)
                Log.d("groops", my_groops.size.toString())
                adapter.groops = my_groops
                activity_list_temp.addAll(my_groops)
                adapter.notifyDataSetChanged()
                Log.d("groops", adapter.groops.size.toString())
            }
        }





        adapter.notifyDataSetChanged()
        search_by_category2.addTextChangedListener(object : TextWatcher{
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
                    adapter.groops = my_groops
                }else{
                    adapter.groops = my_groops.filter{
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
    create_groop_for_display_groops2.setOnClickListener {
        var intent = Intent(this@display_groops, Groop_Create::class.java)
        startActivity(intent)
    }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

}

//
////recycler view adapter
//inner class HomeAdapter : RecyclerView.Adapter<HomeAdapter.JokeViewHolder>() {
//
//    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): JokeViewHolder {
//        val itemView = LayoutInflater.from(p0.context).inflate(R.layout.home_groop_recycler_item, p0, false)
//        return JokeViewHolder(itemView)
//    }
//
//    //what to do with each element
//    override fun onBindViewHolder(p0: JokeViewHolder, p1: Int) {
//        //gets joke aka song from the songlist and fills the fields of the itemView
//        //with the song data from the array
//        val activity = my_groops[p1]
//        p0.name.text = "Name: "+activity.name
//        p0.curParticiants.text="Currently: "+activity.members!!.size.toString()
//        p0.participants.text = "Participants: "+activity.capacity.toString()
//        var lng = myLoc.longitude*10000000
//        var lat = myLoc.latitude*1000000
//        p0.distance.text="Distance: "+findDistance(activity.location, GeoPoint(lat,lng))
//        p0.time.text="Time: "+activity.startTime.toString()
//        p0.row.setOnClickListener {
//            val intent = Intent(p0.itemView.context, com.example.groop.Groop_Join::class.java)
//            intent.putExtra("this_groop", activity.id)
//            intent.putExtra("user", user)
//            startActivity(intent)
//        }
//
//    }
//
//
//    override fun getItemCount(): Int {
//        return my_groops.size
//    }
//
//
//    inner class JokeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var name: TextView = itemView.findViewById(R.id.home_groop_name)
//        var participants: TextView = itemView.findViewById(R.id.home_groop_participants)
//        var curParticiants: TextView = itemView.findViewById(R.id.home_groop_currParticipants)
//        var distance: TextView = itemView.findViewById(R.id.home_groop_distance)
//        var time: TextView = itemView.findViewById(R.id.home_groop_time)
//        var row = itemView
//
//    }
//}