package com.example.groop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView
import com.example.groop.DataModels.Message
import com.example.groop.Util.DBManager
import com.example.groop.Util.Groop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class GroopChatActivity : AppCompatActivity() {

    val TAG = "GROOP_CHAT"
    lateinit var db: FirebaseFirestore
    var messages: ArrayList<Message> = ArrayList()
    //UI ELEMENTS
    lateinit var list: ListView
    lateinit var messageEntry: EditText
    lateinit var sendButton: Button

    //INTENT PARAMETERS
    //the Groop of interest
    lateinit var groopId: String
    lateinit var groopHashKeys: ArrayList<String>
    lateinit var groopHashEmails: ArrayList<String>

    private val auth = FirebaseAuth.getInstance()

    var currentUser: String=auth.currentUser!!.email!!


        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groop_chat)

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
        groopId = intent.getStringExtra("groopId")
            groopHashKeys = intent.getStringArrayListExtra("groop_hash_keys")
            groopHashEmails = intent.getStringArrayListExtra("groop_hash_emails")


        //must include the groop id
        if (groopId == null) {
            Log.d(TAG, "Groop not passed in with ID")
        }


        //okay, first off, we've got to connect to the database and
        // load all of the messages
        db.collection(DBManager.Paths.groops).document(groopId)
            .collection(DBManager.Paths.messages).get()
            .addOnSuccessListener { snapshot ->
                updateMessageUI(snapshot)
            }
        //then set up a listener to do the exact same thing every
        // time there is a change in the database
        db.collection(DBManager.Paths.groops).document(groopId)
            .collection(DBManager.Paths.messages)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    updateMessageUI(snapshot)
                }
            }
    }

    /**
     * Just resets the adapter as we are often wont to do
     * Hoo BOY this is gonna look atrocious lol
     */
    fun updateMessageUI(snapshot: QuerySnapshot) {
        //update the messages arraylist
        messages = DBManager.getMessageHistory(snapshot)
        //then update the associated UI element
        list.adapter = ArrayAdapter<Message>(this,
            android.R.layout.simple_list_item_1, messages)
    }

    /**
     * Sends a new message!
     */
    fun sendNewMessage(content: String) {
        //pretty straightforward
        DBManager.sendMessageToGroop(currentUser, groopId, content)
    }
}
