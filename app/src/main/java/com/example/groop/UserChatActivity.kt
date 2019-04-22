package com.example.groop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.example.groop.DataModels.Message
import com.example.groop.Util.DBManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class UserChatActivity : AppCompatActivity() {

    val TAG = "USER_CHAT"
    lateinit var db: FirebaseFirestore
    var messages: ArrayList<Message> = ArrayList()
    //UI ELEMENTS
    lateinit var list: ListView
    lateinit var messageEntry: EditText
    lateinit var sendButton: Button

    //INTENT PARAMETERS
    //the current user's email
    lateinit var currentUser: String
    //and the user that will be messaged
    lateinit var otherUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_chat)

        //get the database up and running
        db = FirebaseFirestore.getInstance()

        //INSTANTIATE UI ELEMENTS
        //point to the list
        list = findViewById(R.id.message_list)
        //where you enter your new message
        messageEntry = findViewById(R.id.new_message)
        //the button
        sendButton = findViewById(R.id.send)

        //onclick listener for the send button
        sendButton.setOnClickListener {
            //take the content from what is currently entered in the thing
            val content = messageEntry.text.toString()
            if (content != "") {
                sendNewMessage(content)
            }
            //and eliminate the stuff from the EditText
            messageEntry.text.clear()
        }

        //should have passed in a Groop object when entering this activity,
        // so let's get that li'l guy
        //key should be "groopId"
        otherUser = intent.getStringExtra("otherUser")
        //also need the current user
        currentUser = intent.getStringExtra("user")

        //must include the other user
        if (otherUser == null) {
            Log.d(TAG, "intent does not include other user")
        }
        //likewise must include the current user
        if (currentUser == null) {
            Log.d(TAG, "intent does not include current user")
        }

        //okay, first off, we've got to connect to the database and
        // load all of the messages
        db.collection(DBManager.Paths.users).document(currentUser)
            .collection(DBManager.Paths.messages).get()
            .addOnSuccessListener { snapshot ->
                updateMessageUI(snapshot)
            }
        //then set up a listener to do the exact same thing every
        // time there is a change in the database
        db.collection(DBManager.Paths.users).document(currentUser)
            .collection(DBManager.Paths.messages)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    updateMessageUI(snapshot)
                }
            }
    }

    /**
     * Updates the ListView to reflect a new message
     * being sent or received
     */
    fun updateMessageUI(snapshot: QuerySnapshot) {
        //update the messages arraylist
        //pass in other user to filter the results
        messages = DBManager.getMessageHistory(snapshot, otherUser)
        //then update the associated UI element
        messages.sortBy { it.timeStamp }
        list.adapter = ArrayAdapter<Message>(this,
            android.R.layout.simple_list_item_1, messages)
    }

    /**
     * Sends a new message!
     */
    fun sendNewMessage(content: String) {
        DBManager.sendMessageToUser(currentUser, otherUser, content)
    }
}
