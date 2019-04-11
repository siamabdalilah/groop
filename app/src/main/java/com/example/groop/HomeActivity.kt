package com.example.groop

//import android.content.Intent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.groop.Util.setupBottomNav
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_bar.view.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNav(this, bottom_bar.bottom_bar_layout)

//        val a= bottom_bar.bottom_bar_layout
//        bottom_bar.bottom_bar_layout.setOnNavigationItemSelectedListener {
//            lateinit var intent: Intent
//            when(it.title){
//                "Home"      -> {
//                    intent = Intent(this, MainActivity::class.java)
//                    Log.d("title", "Home")
//                }
//                "People"    -> {
//                    intent = Intent(this, MainActivity::class.java)
//                    Log.d("title", "People")
//                }
//
//                else        -> {
//                    intent = Intent(this, MainActivity::class.java)
//                    Log.d("title", "Title")
//                }
//            }
//            startActivity(intent)
//            true
//        }
    }
}
