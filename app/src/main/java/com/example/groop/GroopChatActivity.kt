package com.example.groop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.groop.Util.Groop

class GroopChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groop_chat)

        //should have passed in a Groop object when entering this activity,
        // so let's get that li'l guy
        //key should be "groop"
        val groop = intent.getSerializableExtra("groop") as Groop

        //okay, first off, we've got to connect to the thing
    }
}
