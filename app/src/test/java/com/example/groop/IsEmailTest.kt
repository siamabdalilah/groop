package com.example.groop

import android.util.Log
import com.example.groop.Util.findDistance
import com.example.groop.Util.findDistanceDumbWay
import com.example.groop.Util.isEmail
import com.google.firebase.firestore.GeoPoint
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class IsEmailTest {
    @Test
    fun recognizesEmail() {
        assertTrue(isEmail("telemonian@gmail.com"))
        assertFalse(isEmail("monkeyz"))
    }
}
