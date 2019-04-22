package com.example.groop

import android.util.Log
import com.example.groop.Util.findDistance
import com.example.groop.Util.findDistanceDumbWay
import com.google.firebase.firestore.GeoPoint
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DistanceTest {
    val earthIsFlat = false

    @Test
    fun samePoint() {
        val one: GeoPoint = GeoPoint(10.0, 3.0)
        assertEquals(findDistance(one, one), 0.0, 0.1)
    }

    @Test
    fun earthIsRound() {
        assertFalse(earthIsFlat)
    }

    @Test
    fun expectedVal() {
        //checked against data received from here:
        //https://www.nhc.noaa.gov/gccalc.shtml
        val one: GeoPoint = GeoPoint(10.0, 17.0)
        val two: GeoPoint = GeoPoint(10.1, 17.1)
        //we'll give it a pretty big delta--this is hardly an exact science
        val dist = findDistance(one, two)

        val expectedDist = 9.94194 //converted to miles
        val delta = 3.0

        val diff = Math.abs(dist - expectedDist)

        assertTrue("Expected: "+expectedDist+".  Was " + dist.toString() + ".  Differed by " + diff,
            diff < delta)
        //assertEquals("Nope--was " + findDistance(one, two), 1935.0, findDistance(one, two), 100.0)
    }
}
