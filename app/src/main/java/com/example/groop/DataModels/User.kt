package com.example.groop.DataModels

import com.example.groop.Util.Activity_groop
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import java.io.Serializable

    data class User(var email: String, var name: String="", var location: GeoPoint=GeoPoint(0.0,0.0), var bio: String = "",
                    var profilePicture: String? = null, var createdGroops: ArrayList<DocumentReference> = ArrayList(),
                    var joinedGroops: ArrayList<DocumentReference> = ArrayList(),
                    var activities: ArrayList<Activity_groop> = ArrayList()): Serializable



//   class User:Serializable{
//
//    var email:String = ""
//    var name:String = ""
//    var location: GeoPoint = GeoPoint(0.0,0.0)
//    var bio: String = ""
//    var profilePicture:String = ""
//    var createdGroops: ArrayList<DocumentReference> = ArrayList()
//    var joinedGroops: ArrayList<DocumentReference> = ArrayList()
//    var activities: ArrayList<Activity_groop> = ArrayList()
//
//
//
//    constructor( email: String,
//                 name: String,
//                 location: GeoPoint,
//                 bio: String,
//                 profilePicture: String?,
//                 createdGroops: ArrayList<DocumentReference>,
//                 joinedGroops: ArrayList<DocumentReference>,
//                 activities: ArrayList<Activity_groop>
//    )   {
//        this.email=email
//        this.name=name
//        this.location=location
//        this.bio=bio
//        this.profilePicture=""
//        this.createdGroops= createdGroops
//        this.joinedGroops = joinedGroops
//        this.activities = activities
//    }
//       constructor( email: String,
//                    name: String,
//                    location: GeoPoint,
//                    bio: String
//       )   {
//           this.email=email
//           this.name=name
//           this.location=location
//           this.bio=bio
//           this.profilePicture=""
//           this.createdGroops= ArrayList()
//           this.joinedGroops = ArrayList()
//           this.activities = ArrayList()
//       }
//       constructor(email:String){
//           this.email=email
//       }

}