package com.example.groop

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groop.DataModels.User
import com.example.groop.DataModels.UserListAdapter
import com.example.groop.Util.DBManager
import com.example.groop.Util.Groop
import com.example.groop.Util.findDistance
import com.example.groop.Util.setupNav
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.display_users.*
import kotlinx.android.synthetic.main.bottom_bar.view.*
import kotlinx.android.synthetic.main.top_bar.view.*
import java.util.*

class display_users: AppCompatActivity() {
    private val myLoc = GeoPoint(0.0,0.0)

    private val auth = FirebaseAuth.getInstance()
    private val username = auth.currentUser!!.email!!
    private lateinit var user:User
    private lateinit var adapter : UserListAdapter
    private var users: ArrayList<User> = ArrayList()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_users)
        setupNav(this, display_users_top_bar.top_bar, display_users_bottom_bar.bottom_bar_layout)
        adapter = UserListAdapter(users, this)

        user_display_recycler.layoutManager = LinearLayoutManager(this)
        user_display_recycler.adapter = adapter

        search_users.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Do Nothing
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchBy = s.toString().toLowerCase().replace(",", " ")
                    .replace(Regex("[ ]+"), " ").replace(Regex("[^a-z0-9 ]"), "")
                    .trim()

                if (searchBy == ""){
                    adapter.users = users
                }else{
                    adapter.users = users.filter{
                        if (it.name.toLowerCase().startsWith(searchBy) ?: false){
                            return@filter true
                        }

                        android.util.Log.wtf("activitylistsizes", it.activities.size.toString())
                        for (activityGroop in it.activities){
                            Log.d("actname", activityGroop.name)
                            if (activityGroop.name.toLowerCase().matches(Regex(".*$searchBy.*"))){
                                return@filter true
                            }
                        }

                        false
                    } as ArrayList<User>
                }
                adapter.notifyDataSetChanged()
            }

        })

    }

    private fun parseActs(){
        var count = 0;
        for (user in users){
            db.collection("users").document(user.email)
                .collection("activities").get().addOnSuccessListener {
                    val docs = it.documents
                    for (doc in docs){
                        user.activities.add(DBManager.parseActivity(doc))
                    }
                    count++
                    if (count == users.size){
                        adapter.users = users
                        Log.d("activityList", "Got gere")
                        adapter.notifyDataSetChanged()
                    }
                }
        }
    }

    override fun onResume() {
        super.onResume()
        val gl=GroopLocation(this)
        val locationTemp = gl.getLocation()

        db.collection("users").document(username).get().addOnSuccessListener { it ->
            val bio = it.get("bio").toString()
            val name = it.get("name").toString()
            val location = it.getGeoPoint("location") as GeoPoint
            user = User(username, name, location, bio)
            user.location = GeoPoint(locationTemp.latitude, locationTemp.longitude)


            db.collection("users").get().addOnSuccessListener { snapshot ->
                users = DBManager.getAllUsers(snapshot)

                users.removeIf { it.email == user.email }
                adapter.users = users
                parseActs()

                android.util.Log.d("ActivityListSize", users[4].activities.size.toString())

                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }
}