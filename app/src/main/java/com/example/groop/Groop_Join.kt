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
import com.example.groop.Util.DBManager
import com.example.groop.Util.Groop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.home_display.*
import kotlinx.android.synthetic.main.home_recycler_frag.*
import kotlinx.android.synthetic.main.join_groop.*

class Groop_Join: AppCompatActivity() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var username:String
    private var user_groops:ArrayList<Groop> = ArrayList()
    private lateinit var this_groop:Groop
    private var adapter = HomeAdapter()
    private var jusers: ArrayList<String> = ArrayList()
    private var hasher: Map<String,String> = HashMap()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join_groop)
       username = auth.currentUser!!.email!!
        join_groop_btn.visibility= View.GONE
        //TODO delete_groop_for_groop_join.visibility=View.GONE
        home_recycler_join_groop.layoutManager = LinearLayoutManager(this@Groop_Join)
        home_recycler_join_groop.adapter=adapter

        val groop_id = intent.getStringExtra("this_groop")
        db.collection("groops").document(groop_id).get().addOnSuccessListener { snap->
            this_groop=DBManager.parseGroop(snap)
            groop_name.text=this_groop.name
            jgroop_creator.text=this_groop.creatorName
            jgroop_bio.text=this_groop.description
            jgroop_location.text=this_groop.location.toString()
            jgroop_time.text=this_groop.startTime.toString()
            if(this_groop.members!=null){
                for(usr in this_groop.members!!){
                    usr.get().addOnSuccessListener { snap->
                        jusers.add(snap.get("name").toString())
                        hasher.plus(Pair(snap.get("name")as String,snap.id))
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
        // var user = intent.extras.get("user") as User
        //val user = User("telemonian@gmail.com", "Billiamson McGee", GeoPoint(1.1, 0.0), "")
        DBManager.getGroopsJoinedBy(username,this::GetJoinedArray)




        join_groop_btn.setOnClickListener {
            user_groops.add(this_groop)
            db.collection("user").document(username).update("joinedGroops",user_groops)
//            val intent = Intent(this@Groop_Join, display_groops::class.java)
////            startActivity(intent)
            finish()
        }
        messege_groop_btn.setOnClickListener {
            val intent = Intent(this@Groop_Join, GroopChatActivity::class.java)
            intent.putExtra("groopId",this_groop.id)
            var keys:ArrayList<String> = ArrayList()
            var vals:ArrayList<String> = ArrayList()
            for(pair in hasher!!){
                keys.add(pair.key)
                vals.add(pair.value)
            }
            intent.putExtra("groop_hash_keys",keys)
            intent.putExtra("groop_hash_emails",vals)
            startActivity(intent)
        }
    }
    fun GetJoinedArray(arr:ArrayList<Groop>){
        user_groops.addAll(arr)
        if(!user_groops.contains(this_groop)){
            if(this_groop.capacity!=this_groop.numMembers){
                join_groop_btn.visibility= View.VISIBLE
            }
        }
        else{
            if(this_groop.createdBy!=username) {
               //TODO leave_groop_for_groop_join.visibility = View.VISIBLE
            }
        }
        if(this_groop.createdBy==username){
           //TODO delete_groop_for_groop_join.visibility=View.VISIBLE
        }
    }
    //recycler view adapter
    inner class HomeAdapter : RecyclerView.Adapter<HomeAdapter.JokeViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): JokeViewHolder {
            val itemView = LayoutInflater.from(p0.context).inflate(R.layout.recycler_user_item, p0, false)
            return JokeViewHolder(itemView)
        }

        //what to do with each element
        override fun onBindViewHolder(p0: JokeViewHolder, p1: Int) {
            //gets joke aka song from the songlist and fills the fields of the itemView
            //with the song data from the array
            val activity = jusers[p1]
            p0.juser_name.text = activity
            p0.row.setOnClickListener {
                val intent = Intent(p0.itemView.context, User_View::class.java)
                intent.putExtra("user_name", activity)
                intent.putExtra("user_email",hasher?.get(activity))
                startActivity(intent)
            }

        }


        override fun getItemCount(): Int {
            return jusers.size
        }


        inner class JokeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var juser_name: TextView = itemView.findViewById(R.id.user_name)
            var row = itemView

        }
    }


}