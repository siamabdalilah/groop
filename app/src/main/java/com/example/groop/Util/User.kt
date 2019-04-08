package com.example.groop.Util

import com.google.firebase.firestore.GeoPoint

class User {

    private lateinit var email: String
    private lateinit var activities: ArrayList<String>
    private lateinit var createdGroops: ArrayList<Groop>
    private lateinit var joinedGroops: ArrayList<Groop>
    private lateinit var location: GeoPoint
    private lateinit var name: String
    private lateinit var profilePicture: String

    constructor(email: String, activities: ArrayList<String>, createdGroops: ArrayList<Groop>,
                joinedGroops: ArrayList<Groop>, location: GeoPoint, name: String, profilePicture: String) {
        this.email = email
        this.activities = activities
        this.createdGroops = createdGroops
        this.joinedGroops = joinedGroops
        this.location = location
        this.name = name
        this.profilePicture = profilePicture
    }

}