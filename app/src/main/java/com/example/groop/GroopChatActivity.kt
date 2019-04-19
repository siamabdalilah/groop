package com.example.groop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.groop.DataModels.Message
import com.example.groop.Util.DBManager
import com.example.groop.Util.Groop
import com.google.firebase.firestore.FirebaseFirestore

class GroopChatActivity : AppCompatActivity() {

    private var messages: ArrayList<Message> = ArrayList()
    private val TAG = "GROOP_CHAT"

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groop_chat)

        //get the database up and running
        db = FirebaseFirestore.getInstance()

        //should have passed in a Groop object when entering this activity,
        // so let's get that li'l guy
        //key should be "groop"
        val groop = intent.getSerializableExtra("groop") as Groop

        //must include the groop id
        if (groop.id == null) {
            Log.d(TAG, "Groop not passed in with ID")
        }

        //okay, first off, we've got to connect to the database and
        // load all of the messages
        db.collection(DBManager.Paths.groops).document(groop.id!!)
            .collection(DBManager.Paths.messages).get()
            .addOnSuccessListener { snapshot ->
                messages = DBManager.getMessageHistory(snapshot)
                updateMessageUI()
            }
        //then set up a listener to do the exact same thing every
        // time there is a change in the database
        db.collection(DBManager.Paths.groops).document(groop.id!!)
            .collection(DBManager.Paths.messages)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    messages = DBManager.getMessageHistory(snapshot)
                    updateMessageUI()
                }
            }

    }

    /**
     * Updates the message UI to reflect the current version
     * of the message array
     */
    fun updateMessageUI() {
        //TODO
    }
}
