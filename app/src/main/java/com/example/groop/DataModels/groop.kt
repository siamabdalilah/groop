package com.example.groop.DataModels

import com.google.firebase.firestore.GeoPoint
import java.io.FileDescriptor
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
class groop: Serializable {
    private var name: String = ""
    private var category: String =""
    private var members: ArrayList<String> = ArrayList()
    private var capacity: Int = 0
    private var createdBy: String = ""
    private var description: String = ""
    private var location: GeoPoint = GeoPoint(0.0,0.0)
    private var numMembers: Int = 0
    private var startTime: Date = Date()

    constructor(
        name: String,
        category: String,
        members: ArrayList<String>,
        capacity: Int,
        createdBy: String,
        description: String,
        location: GeoPoint,
        numMembers: Int,
        startTime: Date
    )  {
        this.name = name
        this.category = category
        this.members = members
        this.capacity=capacity
        this.createdBy=createdBy
        this.description=description
        this.location=location
        this.numMembers=numMembers
        this.startTime=startTime

    }

    fun getName():String{
        return this.name
    }
    fun getCategory():String{
        return this.category
    }
    fun getMembers():ArrayList<String>{
        return this.members
    }
    fun getCapacity():Int{
        return this.capacity
    }
    fun getCreatedBy():String{
        return this.createdBy
    }
    fun getDescription():String{
        return this.description
    }
    fun getLocation():GeoPoint{
        return this.location
    }
    fun getNumMembers():Int{
        return this.numMembers
    }
    fun getStartTime():Date{
        return this.startTime
    }

}