package com.example.groop.DataModels

import com.example.groop.Util.Activity_groop
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

data class User(val email: String, val name: String, val location: GeoPoint, var bio: String,
                var profilePicture: String? = null, val createdGroops: ArrayList<DocumentReference> = ArrayList(),
                val joinedGroops: ArrayList<DocumentReference> = ArrayList(),
                val activities: ArrayList<Activity_groop>)