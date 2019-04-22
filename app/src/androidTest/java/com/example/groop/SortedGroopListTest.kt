package com.example.groop

import androidx.test.runner.AndroidJUnit4
import com.example.groop.Util.DBManager
import com.example.groop.Util.Groop
import com.google.firebase.firestore.GeoPoint
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SortedGroopListTest {
    @Before
    fun setup() {

    }

    @Test
    fun sortsGroopList() {
        val groops = ArrayList<Groop>()

        groops.add(Groop(location=GeoPoint(10.0, 17.0)))
        groops.add(Groop(location=GeoPoint(15.3, 7.9)))

        val sorted = DBManager.sortGroops(groops, GeoPoint(15.2, 7.8))

        assertTrue(sorted[0].location.latitude == 15.3)
    }

    @After
    fun tearDown() {

    }
}