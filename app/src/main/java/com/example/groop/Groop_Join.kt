package com.example.groop

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groop.DataModels.User
import com.example.groop.DataModels.UserListAdapter
import com.example.groop.Util.DBManager
import com.example.groop.Util.DBManager.Paths.parseUser
import com.example.groop.Util.Groop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.join_groop.*
import java.text.SimpleDateFormat

class Groop_Join : AppCompatActivity() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var username: String
    private var btn_status = "JOIN"
    private var user_groops: ArrayList<Groop> = ArrayList()
    private lateinit var this_groop: Groop
    private lateinit var adapter: UserListAdapter
    //    private var jusers: ArrayList<String> = ArrayList()
    private var hasher: MutableMap<String, String> = HashMap()
    private var users = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join_groop)
        username = auth.currentUser!!.email!!

        adapter = UserListAdapter(users, this)

        edit_groop_for_groop_join.visibility = View.GONE
        messege_groop_btn.visibility = View.GONE
        home_recycler_join_groop.layoutManager = LinearLayoutManager(this@Groop_Join)
        home_recycler_join_groop.adapter = adapter

        val groop_id = intent.getStringExtra("this_groop")
        db.collection("groops").document(groop_id).get().addOnSuccessListener { snap ->

            this_groop = DBManager.parseGroop(snap)

            groop_name.text = this_groop.name
            jgroop_creator.text = this_groop.creatorName
            jgroop_bio.text = this_groop.description
            jgroop_location.text = this_groop.address

            val formatter = SimpleDateFormat("dd MMMM yyyy")

            jgroop_time.text = formatter.format(this_groop.startTime)

            if (this_groop.members != null) {
                for (usr in this_groop.members!!) {
                    usr.get().addOnSuccessListener { doc ->
                        //                        jusers.add(doc.get("name").toString())
                        hasher.put(doc.get("name").toString(), doc.get("email").toString())
                        users.add(parseUser(doc))
                        adapter.users = users
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        DBManager.getGroopsJoinedBy(username, this::GetJoinedArray)


        join_groop_btn.setOnClickListener {
            if (btn_status == "JOIN") {
                user_groops.add(this_groop)
                // db.collection("user").document(username).update("joinedGroops",user_groops)

                db.collection("users").document(username).get().addOnSuccessListener { snap ->
                    DBManager.joinGroop(DBManager.parseUser(snap), this_groop, ::finish)
                }
            } else if (btn_status == "DELETE") {
                DBManager.deleteGroop(this_groop)
                finish()
            } else if (btn_status == "LEAVE") {
                DBManager.leaveGroop(this_groop, username, ::finish)
            } else {

            }
            //finish()
        }
        messege_groop_btn.setOnClickListener {
            val intent = Intent(this@Groop_Join, GroopChatActivity::class.java)
            intent.putExtra("groopId", this_groop.id)
            var keys: ArrayList<String> = ArrayList()
            var vals: ArrayList<String> = ArrayList()
            for (pair in hasher!!) {
                keys.add(pair.key)
                vals.add(pair.value)
            }
            intent.putExtra("groop_hash_keys", keys)
            intent.putExtra("groop_hash_emails", vals)
            intent.putExtra("groop_name", this_groop.name)
            startActivity(intent)
        }




        edit_groop_for_groop_join.setOnClickListener {
            val intent = Intent(this@Groop_Join, Edit_Groop::class.java)
            intent.putExtra("groop_id", this_groop.id)
            startActivity(intent)

        }


    }

    fun GetJoinedArray(arr: ArrayList<Groop>) {
        user_groops.addAll(arr)
        var b = true
        for (grp in user_groops) {
            if (grp.id == this_groop.id) {
                b = false
                continue
            }
        }
        if (b) {
            if (this_groop.capacity != this_groop.numMembers) {
                join_groop_btn.visibility = View.VISIBLE
                join_groop_btn.text = "JOIN"
                btn_status = "JOIN"
            }
        } else {
            if (this_groop.createdBy != username) {
                join_groop_btn.text = "LEAVE GROOP"
                messege_groop_btn.visibility = View.VISIBLE
                btn_status = "LEAVE"
                //TODO leave_groop_for_groop_join.visibility = View.VISIBLE
            }
        }
        if (this_groop.createdBy == username) {
            join_groop_btn.text = "DELETE GROOP"
            messege_groop_btn.visibility = View.VISIBLE
            btn_status = "DELETE"
            //TODO delete_groop_for_groop_join.visibility=View.VISIBLE
            edit_groop_for_groop_join.visibility = View.VISIBLE
        }
    }

}