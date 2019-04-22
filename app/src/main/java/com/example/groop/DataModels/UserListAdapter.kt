package com.example.groop.DataModels

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.groop.R
import com.example.groop.User_View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.user_card.view.*

class UserListAdapter(var users: ArrayList<User>, val activity: Activity) : RecyclerView.Adapter<UserListAdapter.UserHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListAdapter.UserHolder {
        return UserHolder(LayoutInflater.from(activity).inflate(R.layout.user_card, parent, false))
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserListAdapter.UserHolder, position: Int) {
        holder.username.text = users[position].name
        holder.holder.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            if(auth.currentUser!!.email!=users[position].email) {
                val intent = Intent(activity, User_View::class.java)

                intent.putExtra("user_name", users[position].name)
                intent.putExtra("user_email", users[position].email)
                activity.startActivity(intent)
            }
        }
    }

    inner class UserHolder(view: View) : RecyclerView.ViewHolder(view){
        val username = view.name_of_user
        val holder = view
    }


}