package com.example.groop.Util

import android.app.Activity
import android.util.Log
import com.example.groop.DataModels.Message
import com.example.groop.DataModels.User
import com.example.groop.DataModels.UserWithoutEmail
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.model.DocumentKey
import com.google.firebase.firestore.model.ResourcePath
import com.google.firebase.firestore.model.value.TimestampValue
import java.lang.IllegalArgumentException
import java.sql.Timestamp
import java.util.*

class DBManager {


    companion object Paths {
        private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val activities: String = "activities"
        val groops: String = "groops"
        val users: String = "users"
        val messages: String = "messages"


        //okay boys, here we go
        /////////////////////////PARSERS
        /**
         * Parse an Activity_groop object from a document snapshot
         * containing a representation of an activity
         * DEPRECATED
         */
        fun parseActivity(doc: DocumentSnapshot): Activity_groop {
            return Activity_groop(
                doc.id,
                doc.get("description").toString()
            )
        }

        /**
         * Parse a Groop object from a document snapshot
         * containing a representation of an activity
         * DEPRECATED
         */
        fun parseGroop(doc: DocumentSnapshot): Groop {
            //TODO: not quite sure how firebase handles arrays, actually


            val members = doc.get("members") as ArrayList<DocumentReference>

            var d: Date? = doc.getDate("startTime")
            Log.d("ANDROID",d.toString())

            val adr = if (doc.get("address") == null) "" else doc.get("address") as String

            return Groop(
                (doc.get("capacity") as Long).toInt(), doc.get("createdBy").toString(),
                doc.get("creatorName").toString(),
                doc.get("description").toString(), doc.get("location") as GeoPoint,
                members, doc.get("name").toString(),
                (doc.get("numMembers") as Long).toInt(), doc.getDate("startTime") as Date,
                doc.get("type").toString(), doc.id, adr
            )
        }

        /**
         * Parse a User object from a document snapshot
         * containing a representation of a user
         * DEPRECATED
         */
        fun parseUser(doc: DocumentSnapshot): User {


                //TODO: Again, not really sure how arrays work
                val createdGroops = doc.get("createdGroops") as ArrayList<DocumentReference>
                val joinedGroops = doc.get("joinedGroops") as ArrayList<DocumentReference>

                val activityList: ArrayList<Activity_groop> = ArrayList<Activity_groop>()
                //each user document contains a collection of arraylists
                // so we need to go through every document in that there
                // collection and add it to an arraylist
                doc.getReference().collection("activities")
                    .get().addOnSuccessListener { query ->
                        val docList = query.documents
                        //for every document, call the parseActivity function
                        // in order to get an Activity object
                        for (activityDoc in docList) {
                            activityList.add(parseActivity(activityDoc))
                        }
                        Log.d("activitylistsize", doc.get("name").toString() + " " + activityList.size.toString())
                    }

                return User(
                    doc.id, doc.get("name").toString(), doc.get("location") as GeoPoint,
                    doc.get("bio").toString(), doc.get("profilePicture").toString(),
                    createdGroops, joinedGroops//, activityList
                )


        }

        fun parseMessage(doc: DocumentSnapshot) : Message {
            return Message(
                doc.get("from").toString(), (doc.get("timeStamp")as com.google.firebase.Timestamp).toDate() as Date,
                doc.get("content").toString(), doc.get("to").toString()
            )
        }


        /////////////////////////GET ALL
        /**
         * Returns a list of Strings representing the names of
         * all the activities in the database
         */
        fun getAllActivities(query: QuerySnapshot): ArrayList<String> {
            val activityList = ArrayList<String>()

            val docList = query.documents
            //activities are indexed by their name, so we just add that name to the list
            for (doc in docList) {
                activityList.add(doc.id)
            }

            return activityList
        }

        /**
         * Returns an arraylist of every Groop that has been
         * created and remains active
         */
        fun getAllGroops(query: QuerySnapshot, reference: GeoPoint? = null): ArrayList<Groop> {
            val groopList = ArrayList<Groop>()

            val docList = query.documents
            //add each group to the list through this ridiculous process
            for (doc in docList) {
                //add a new Groop to the list
                var g:Groop? = null
                if(doc.contains("name")) {
                    g = parseGroop(doc)//doc.toObject(Groop::class.java) //TODO this does not convert
                }
                if (g != null) {
                    groopList.add(g)
                }
            }

            //if the caller has specified that they want the list of groops to be
            // sorted based on a particular point
            if (reference != null) {
//                return sortGroops(groopList, reference)
                return getSortedGroopList(groopList, reference)
            } else {
                return groopList
            }

        }

        /**
         * Returns an arraylist of every User registered
         * in  the database
         */
        fun getAllUsers(query: QuerySnapshot): ArrayList<User> {
            val userList = ArrayList<User>()

            val docList = query.documents
            //add each user to the list
            for (doc in docList) {
                //add a new user to the list by automatically parsing it
                   val u = parseUser(doc)//doc.toObject(User::class.java)

                if (u != null) {
                    userList.add(u)
                }
            }

            return userList
        }

        ////////////////////////SORTING

        /**
         * Sorts a list of Groops relative to the GeoPoint
         * Uses the UtilFun methods findDistance and firstPointIsCloser
         * A QuickSort algorithm
         * This was useful: https://github.com/gazolla/Kotlin-Algorithm/blob/master/QuickSort/QuickSort.kt
         */
        private class CustomComparator(reference:GeoPoint) : Comparator<Groop>{
            val reference=reference
           override fun compare(o1:Groop,o2:Groop):Int{
                return findDistance(o1.location,reference).compareTo(findDistance(o2.location,reference))
            }
        }
        fun sortGroops(groops: ArrayList<Groop>, reference: GeoPoint): ArrayList<Groop> {
            /*//BASE CASE
            if (groops.size < 2) {
                return groops
            }
            Collections.sort(groops, CustomComparator(reference))
            return groops
            //RECURSIVE CASE
            //filter all of the lists based on
            val pivot = groops.get(groops.size / 2)
            //do quicksort things based on the arbitrarily chosen pivot
            val equal: ArrayList<Groop> = groops.filter { thisGroop ->
                findDistance(thisGroop.location, reference) == findDistance(pivot.location, reference)
            } as ArrayList<Groop>
            val less: ArrayList<Groop> = groops.filter { thisGroop ->
                findDistance(thisGroop.location, reference) < findDistance(pivot.location, reference)
            } as ArrayList<Groop>
            val more: ArrayList<Groop> = groops.filter { thisGroop ->
                findDistance(thisGroop.location, reference) > findDistance(pivot.location, reference)
            } as ArrayList<Groop>

            //apparently this is how array concatenation works lol
            return (sortGroops(less, reference) + equal + sortGroops(more, reference)) as ArrayList<Groop>*/

            //bubble sort babyyyy
            for (i: Int in 0 until groops.size) {
                var lowest: Double = 10000000.0
                var lowestLoc: Int = i
                for (j: Int in i until groops.size) {
                    val dist = findDistance(groops[j].location, reference)
                    if (dist < lowest) {
                        lowest = dist
                        lowestLoc = j
                    }
                }
                val temp = groops[lowestLoc]
                groops[i] = groops[lowestLoc]
                groops[lowestLoc] = temp
            }

            return groops.reversed() as ArrayList<Groop>
        }

        /////////////////////////GETS LIST OF GROOPS
        /**
         * Compiles an arraylist of Groop objects associated with
         * a given activity, looking in the activities collection of the database
         * Be sure to check for subactivities
         *
         * IMPORTANT:  THIS OPERATES ON THE LEVEL OF CALLBACK FUNCTIONS--WILL CALL THE CALLBACK
         * FUNCTION INSTEAD OF RETURNING UPON COMPLETION         */
        fun getGroopsFromActivity(doc: DocumentSnapshot, gotten: (ArrayList<Groop>) -> Any?) {
            val groops = ArrayList<Groop>()
            //bear with me
            var nulls = 0

            //again, I'm not all that certain how this all is going
            // to work, but here we go

            //still don't know what's up with how arrays return in firebase
            val listOfGroopDocuments = doc.get("listOfGroops")
                    as ArrayList<DocumentReference>
            //parse each groop and add it to the list
            for (groop in listOfGroopDocuments) {
                //mostly, we're just going to invoke the parseGroop method
                groop.get().addOnSuccessListener { snapshot ->
                    var g: Groop? = null
                    if(snapshot.contains("name")) {
                        g = parseGroop(snapshot)
                    }
                    if (g != null) {
                        groops.add(g)
                    }
                    else {
                        ++nulls
                    }

                    //okay, bear with me
                    //we need to discover if this is the last ever iteration
                    // in order to do so, check to see whether the array has
                    // been completely filled up with groops, and/or if null
                    // has been encountered enough times to merit moving on
                    if (groops.size + nulls >= listOfGroopDocuments.size) {
                        //so now we've finished all of the get requests
                        //safe to call the callback function
                        gotten(groops)
                    }
                }
            }
        }

//        /**
//         * Returns an ArrayList of groops that have been created by
//         * the user specified by the email
//         */
//        fun getGroopsBy(email: String, gotten: (ArrayList<Groop>) -> Any?) {
//            getUserGroops(email, "createdGroops", gotten)
//        }

        /**
         * Returns an ArrayList of Groop objects that have been
         * joined by the user specified in this email
         */
        fun getGroopsJoinedBy(email: String, gotten: (ArrayList<Groop>) -> Any?) {
            return getUserGroops(email, "joinedGroops", gotten)
        }

        /**
         * Used by both of the methods above; compiles an ArrayList of Groop
         * objects that have been either created or joined by the user specified
         * in the given email
         * Then calls the callback function with the created ArrayList
         * The createdOrJoined parameter should always be either "joinedGroops" or "createdGroops"
         *
         * IMPORTANT:  THIS OPERATES ON THE LEVEL OF CALLBACK FUNCTIONS--WILL CALL THE CALLBACK
         * FUNCTION INSTEAD OF RETURNING UPON COMPLETION
         */
        private fun getUserGroops(email: String, createdOrJoined: String, gotten: (ArrayList<Groop>) -> Any?) {
            if (createdOrJoined != "joinedGroops" && createdOrJoined != "createdGroops") {
                //not using the method properly
                throw IllegalArgumentException("Does not work")
            }

            val groops = ArrayList<Groop>()

            //bear with me
            var nulls = 0

            //again, I'm not all that certain how this all is going
            // to work, but here we go
            db.collection(users).document(email).get().addOnSuccessListener { snapshot ->
                //still don't know what's up with how arrays return in firebase
                val listOfGroopDocuments = snapshot.get(createdOrJoined)
                        as ArrayList<DocumentReference>
                Log.d("GettingGroops", listOfGroopDocuments.size.toString())

                //parse each groop and add it to the list
                //must use this style of iteration because we need to access the
                // index to know when to employ the callback
                for (groop in listOfGroopDocuments) {

                    //mostly, we're just going to invoke the parseGroop method
                    groop.get().addOnSuccessListener { doc ->
                        var g:Groop? =null
                        if(doc.contains("name")) {
                            g = parseGroop(doc)//doc.toObject(Groop::class.java) //TODO throws deserialization error
                        }

                        if (g != null) {
                            groops.add(g)
                        }
                        else {
                            ++nulls
                        }

                        //okay, bear with me
                        //we need to discover if this is the last ever iteration
                        // in order to do so, check to see whether the array has
                        // been completely filled up with groops, and/or if null
                        // has been encountered enough times to merit moving on
                        if (groops.size + nulls >= listOfGroopDocuments.size) {
                            //so now we've finished all of the get requests
                            //safe to call the callback function
                            gotten(groops)
                            Log.d("GettingGroops","done parsing groops")
                        }
                    }
                }
            }
        }

        /////////////////////////MISC. GETS
        /**
         * Given a user, represented as an email, returns an arraylist
         * of activity objects including every activity that the user
         * is interested in
         */
        fun getUserActivityList(email: String, query: QuerySnapshot) {
            val activityList = ArrayList<Activity_groop>()

            //go through each activity in the collection
            val docList = query.documents

            for (doc in docList) {
                //construct a new activity from the given fields
                activityList.add(parseActivity(doc))
            }

        }

        ////////////////////////SET DOCUMENT INFO
        /**
         * Updates the info for an activity that one user has expressed
         * interest in.
         * Void method, writes to the user's list of activities
         */
        fun updateActivityInfo(email: String, activity: String, bio: String) {
            //access the user's information
            val map: HashMap<String, String> = HashMap<String, String>()
            map.put("description", bio)
            //either create the document anew if it hasn't been created already, or simply update the description
            db.collection(users).document(email).collection(activities).document(activity).set(map)
        }

        /**
         * Now we get to the money function--actually creates a new user
         * object in the database
         * Not all that impressive; just takes in a user object and goes for it
         */
        fun addUser(user: User) {
            //single line--Firestore should convert everything for us
            db.collection(users).document(user.email).set(user)
        }

        /**
         * Creates a Groop with the specified information
         */
        fun createGroop(groop: Groop,gotten: () -> Any?) {
            //again, Firestore does everything for us
            //document will just have an auto-generated ID

            val doc = db.collection(Paths.groops).document()
            groop.id=doc.id
                doc.set(groop).addOnSuccessListener {
                db.collection("users").document(groop.createdBy!!).get().addOnSuccessListener { snap ->
                    val temp_user = parseUser(snap)
                    temp_user.createdGroops.add(doc)
                    temp_user.joinedGroops.add(doc)
                    db.collection("users").document(groop.createdBy!!).set(temp_user).addOnSuccessListener {
                        gotten()
                    }

                }
            }
        }

        /**
         * Adds an activity to the list thereof stored in the database
         */
        fun addNewActivity(activityName: String) {
            //this time, we have to do a little bit more work,
            // as not much is known about the activity at time of
            // addition
            val activity = HashMap<String, Any>()
            activity["listOfGroops"] = ArrayList<DocumentReference>()
            activity["listOfUsers"] = ArrayList<DocumentReference>()
            db.collection(Paths.activities).document(activityName).set(activity)
        }

        /**
         * Adds a specified user to the specified Groop
         * This version takes a User and Groop object and does
         * it all that way
         */
        fun joinGroop(user: User, groop: Groop, sent: () -> Any? = {}) {
            //make sure that the Groop passed in has an ID field
            if (groop.id == null) {
                return
            }
            //add the user to the groop's list of members
            db.collection(groops).document(groop.id.toString()).get()
                .addOnSuccessListener {snapshot ->
                    //add stuff to the array
                    val members: ArrayList<DocumentReference> = snapshot.get("members")
                            as ArrayList<DocumentReference>

                    val memberDocRef = db.collection(users).document(user.email)
                    val numMems=(snapshot.get("numMembers") as Long).toInt()
                    members.add(memberDocRef)

                    //and now remake the array
                    db.collection(groops).document(groop.id.toString())
                        .update("members", members)
                    db.collection(groops).document(groop.id.toString())
                        .update("numMembers",numMems+1)
                    //also change the Groop object that was passed in,
                    // just for fun
                    groop.members?.add(memberDocRef)
                }

            //and now add the groop to the user's list of joined groops
            db.collection(users).document(user.email).get()
                .addOnSuccessListener {snapshot ->
                    //add stuff to the array
                    val joinedGroops: ArrayList<DocumentReference> = snapshot.get("joinedGroops")
                        as ArrayList<DocumentReference>
                    val groopDocRef = db.collection(groops).document(groop.id.toString())

                    joinedGroops.add(groopDocRef)

                    //and remake the array
                    db.collection(users).document(user.email)
                        .update("joinedGroops", joinedGroops).addOnSuccessListener {
                            sent()
                        }

                    //also change the User object that was passed in,
                    // just for fun
                    user.joinedGroops.add(groopDocRef)
                }
            //badaboom
        }

        /////////////////////////////////MESSAGING

        /**
         * Takes in data about the message as well as a callback method
         * to update the UI
         *
         * IMPORTANT:  THIS OPERATES ON THE LEVEL OF CALLBACK FUNCTIONS--WILL CALL THE CALLBACK
         * FUNCTION INSTEAD OF RETURNING UPON COMPLETION
         */
        fun sendMessageToUser(from: String, to: String, content: String,
                              sent: () -> Any? = {}) {
            val currentDate = Date()

            //sender and receiver messages are identical
            val message = Message(from, currentDate, content, to)
            //add the message first to the receiver's collection
            db.collection(Paths.users).document(to).collection(Paths.messages)
                .document().set(message)
            //and then to the sender's
            db.collection(Paths.users).document(from).collection(Paths.messages)
                .document().set(message)
                    //after the sender's collection is changed, we want
                    // to update the UI with the callback
                    //might also do nothing
                .addOnSuccessListener {
                    sent()
                }
        }

        /**
         * Takes in data about the message as well as a callback method
         * to update the UI.
         * Like the above method, requires the Groop ID to be passed
         * in as a string, except this time the ID is some random assortment
         * of nonsense.
         *
         * IMPORTANT:  THIS OPERATES ON THE LEVEL OF CALLBACK FUNCTIONS--WILL CALL THE CALLBACK
         * FUNCTION INSTEAD OF RETURNING UPON COMPLETION
         */
        fun sendMessageToGroop(from: String, groopID: String,
                               content: String, sent: () -> Any? = {}) {
            val currentDate = Date()

            //don't need a "to" field on this li'l guy at all
            val message = Message(from, currentDate, content)
            //add the message to the groop's collection
            db.collection(Paths.groops).document(groopID).collection(Paths.messages).document().set(message)
                    //update the UI if applicable
                .addOnSuccessListener {
                    sent()
                }
        }

        /**
         * Returns an arraylist of message objects from the collection
         * specified by the passed in query snapshot.  If "otherUser" is
         * passed in as a parameter, will only return messages where that
         * user is either the sender or the recipient
         *
         * Works for either a Groop's message history or an individual's message
         * history.
         */
        fun getMessageHistory(query: QuerySnapshot, otherUser: String? = null)
            : ArrayList<Message> {
            var messages = ArrayList<Message>()

            //first, look at every document in the specified query snapshot
            val docList = query.documents
            for (doc in docList) {
                //construct a new activity from the given fields
                messages.add(parseMessage(doc))
            }

            //if another user was passed in as a parameter, then
            // filters the array based on that
            if (otherUser != null) {
                messages = messages.filter {message ->
                    message.from == otherUser || message.to == otherUser
                } as ArrayList<Message>
            }

            return messages
        }

        fun getUserByEmail(email: String): User? {
            val task = db.collection("users").document(email).get()
            while(!task.isComplete){
            }
            return parseUser(task.result!!)
        }

        fun updateUser(user: User){
            db.collection("users").document(user.email).set(user)
        }


        fun getSortedGroopList(groops: ArrayList<Groop>, loc: GeoPoint) : ArrayList<Groop>{
            groops.sortBy { findDistance(it.location, loc) }
            return groops
        }

        fun leaveGroop(groop: Groop, username: String, sent: () -> Any? = {}){
            val doc = db.collection("groops").document(groop.id!!)
            doc.get().addOnSuccessListener { snap->
                val temp_groop = parseGroop(snap)
                temp_groop.members?.remove(db.collection("users").document(username))
                val cur_num = temp_groop.numMembers-1
                temp_groop.numMembers=cur_num
                doc.set(temp_groop)
            }
            db.collection("users").document(username).get().addOnSuccessListener { snap->
                val temp_user = parseUser(snap)
                var b = false
                lateinit var doc_temp: DocumentReference

                for(doc_iter in temp_user.joinedGroops){
                    Log.d("doc_id", doc.id)
                    Log.d("doc_id", doc_iter.id)
                    if(doc_iter.id==doc.id){
                        user_remove(temp_user.joinedGroops,doc_iter)
                    }
                }

                for(doc_iter2 in temp_user.createdGroops){
                    if(doc_iter2.id==doc.id){
                        user_remove(temp_user.createdGroops,doc_iter2)
                    }
                }
                db.collection("users").document(username).set(temp_user).addOnCompleteListener {
                    sent()
                }
            }
        }

        fun user_remove(arr:ArrayList<DocumentReference>, doc:DocumentReference){
            arr.remove(doc)
        }
        fun deleteGroop(groop: Groop){
            val doc = db.collection("groops").document(groop.id!!)
            for(usrRef in groop.members!!){
                usrRef.get().addOnSuccessListener { snap->
                    val temp_user = parseUser(snap)
                    temp_user.joinedGroops.remove(doc)
                    if(temp_user.email==groop.createdBy) {
                        temp_user.createdGroops.remove(doc)
                    }
                    usrRef.set(temp_user)
                }
            }
            doc.delete()
        }
        fun editGroop(groop:Groop){
            val doc = db.collection(Paths.groops).document(groop.id!!)
                doc.get().addOnSuccessListener { snap->
                val temp_groop = parseGroop(snap)
                    if(temp_groop.capacity>groop.numMembers){
                        Log.d("ANDROID","error: numMembers was larger than reset capacity")
                    }
                    else{
                        temp_groop.capacity=groop.capacity
                        temp_groop.address= groop.address
                        temp_groop.description=groop.description
                        temp_groop.name=groop.name
                        temp_groop.location=groop.location
                        temp_groop.startTime=groop.startTime
                        temp_groop.type=groop.type
                    }
                    doc.set(temp_groop)
            }
            doc.set(groop)
        }

    }
}