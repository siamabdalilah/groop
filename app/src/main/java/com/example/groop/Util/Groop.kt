package com.example.groop.Util

import com.example.groop.DataModels.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.model.value.TimestampValue
import java.io.Serializable
import java.util.Date
import java.sql.Time
import java.sql.Timestamp

class Groop(var capacity: Int = 0, var createdBy: String? = "", var creatorName: String? = "", var description: String = "",
            var location: GeoPoint = GeoPoint(0.0,0.0), var members: ArrayList<DocumentReference>? = null,
            var name: String = "", var numMembers: Int = 0, var startTime: Date? = null, var type: String = "",
            var id: String? = null, var address: String = ""
)

//
//class Groop: Serializable {
//
//    var capacity: Int = 0
//    var createdBy: String? = null
//    var creatorName: String? = null
//    var description: String = ""
//    var location: GeoPoint = GeoPoint(0.0,0.0)
//    var members: ArrayList<DocumentReference>? = null
//    var name: String = ""
//    var numMembers: Int = 0
//    var startTime: Date? = null
//    var type: String = ""
//    var id: String? = null
//
//    constructor(
//        capacity: Int, createdBy: String?, creatorName: String?, description: String,
//        location: GeoPoint, members: ArrayList<DocumentReference>, name: String,
//        numMembers: Int, startTime: Date, type: String
//    ) {
//        this.capacity = capacity
//        this.createdBy = createdBy
//        this.creatorName = creatorName
//        this.description = description
//        this.location = location
//        this.members = members
//        this.name = name
//        this.numMembers = numMembers
//        this.startTime = startTime
//        this.type = type
//
//    }
//
//    /**
//     * A second constructor used specifically for the Messaging function,
//     * when a Groop object might need an ID as well
//     */
//    constructor(
//        capacity: Int, createdBy: String?, creatorName: String?, description: String,
//        location: GeoPoint, members: ArrayList<DocumentReference>, name: String,
//        numMembers: Int, startTime: Date, type: String, id: String
//    ) {
//        this.capacity = capacity
//        this.createdBy = createdBy
//        this.creatorName = creatorName
//        this.description = description
//        this.location = location
//        this.members = members
//        this.name = name
//        this.numMembers = numMembers
//        this.startTime = startTime
//        this.type = type
//        this.id = id
//    }
//    constructor()
//}