package com.example.groop.DataModels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.groop.R
import kotlinx.android.synthetic.main.item_message_received.view.*
import kotlinx.android.synthetic.main.item_message_sent.view.*


// Idea Reference: https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
class ChatAdapter(var messages: ArrayList<Message>, val username: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val SENT = 1
    val RECEIVED = 2


    override fun getItemCount(): Int {
        return messages.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == RECEIVED){
            return ReceivedMessageHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_received, parent, false))
        }
        else{
            return SentMessageHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sent, parent, false))
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is SentMessageHolder -> {
                holder.bind(messages[position])
            }
            is ReceivedMessageHolder -> {
                holder.bind(messages[position])
            }
            else -> {}
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (messages[position].from == username) return SENT
        return RECEIVED
    }

    inner class SentMessageHolder(view: View) : RecyclerView.ViewHolder(view){
        val message = view.text_message_body
        val time = view.text_message_time

        fun bind(mess: Message){
            message.text = mess.content
            time.text = mess.timeStamp.toString()
        }
    }

    inner class ReceivedMessageHolder(view: View): RecyclerView.ViewHolder(view){
        val message = view.text_message_body_received
        val time = view.text_message_time
        val name = view.text_message_name

        fun bind(mess: Message){
            name.text = mess.from
            time.text = mess.timeStamp.toString()
            message.text = mess.content
        }
    }

}