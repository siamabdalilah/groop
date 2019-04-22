package com.example.groop

import android.util.Log
import androidx.test.InstrumentationRegistry
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import com.example.groop.Util.DBManager
import com.example.groop.Util.Groop
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import junit.framework.Assert.assertTrue
import java.util.*
import kotlin.system.measureNanoTime



@RunWith(AndroidJUnit4::class)
class DBTest {

    private lateinit var dbHelper: DBManager
    private lateinit var groop: Groop
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    @Before
    fun setup() {
        Log.wtf("testRunning", "fml")
        groop=Groop( 0, "tester@tester.com","tester", "this is a test",
       GeoPoint(0.0,0.0), ArrayList(),
       "name", 1, Date(), "TEST",
      address="wheeler rd"
        )
    }



    @Test
    fun testSimpleInsert() {
        Log.wtf("testRunning", "fml")
        DBManager.createGroop(groop, fun () {
            db.collection("groops").document(groop.id.toString()).get().addOnSuccessListener { snap->
                assertTrue(snap!=null)
                DBManager.deleteGroop(groop)
                db.collection("groops").document(groop.id.toString()).get().addOnSuccessListener { snap->
                    assertTrue(snap==null)
                }
                    .addOnFailureListener {
                        assertTrue(true)
                    }
            }
                .addOnFailureListener {
                    assertTrue(false)
                }
        })



    }

    @After
    fun tearDown() {

    }
}