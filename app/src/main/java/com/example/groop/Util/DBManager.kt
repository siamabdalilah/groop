package com.example.groop.Util

import android.app.Activity
import com.example.groop.DataModels.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.model.value.TimestampValue
import java.lang.IllegalArgumentException

class DBManager {


    companion object Paths {
        private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val activities: String = "activities"
        val groops: String = "groops"
        val users: String = "users"


        //okay boys, here we go
        /////////////////////////PARSERS
        /**
         * Parse an Activity_groop object from a document snapshot
         * containing a representation of an activity
         */
        private fun parseActivity(doc: DocumentSnapshot): Activity_groop {
            return Activity_groop(
                doc.id,
                doc.get("description").toString()
            )
        }

        /**
         * Parse a Groop object from a document snapshot
         * containing a representation of an activity
         */
        private fun parseGroop(doc: DocumentSnapshot): Groop {
            //TODO: not quite sure how firebase handles arrays, actually
            val members = doc.get("members") as ArrayList<DocumentReference>

            return Groop(
                doc.get("capacity") as Int, doc.get("createdBy") as DocumentReference,
                doc.get("description").toString(), doc.get("location") as GeoPoint,
                members, doc.get("name").toString(),
                doc.get("numMembers") as Int, doc.get("startTime") as TimestampValue,
                doc.get("type").toString()
            )
        }

        /**
         * Parse a User object from a document snapshot
         * containing a representation of a user
         */
        private fun parseUser(doc: DocumentSnapshot): User {
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
                }

            return User(
                doc.id, doc.get("name").toString(), doc.get("location") as GeoPoint,
                doc.get("bio").toString(), doc.get("profilePicture").toString(),
                createdGroops, joinedGroops, activityList
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
                val g = doc.toObject(Groop::class.java)
                if (g != null) {
                    groopList.add(g)
                }
            }


            //if the caller has specified that they want the list of groops to be
            // sorted based on a particular point
            if (reference != null) {
                return sortGroops(groopList, reference)
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
                val u = doc.toObject(User::class.java)
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
        fun sortGroops(groops: ArrayList<Groop>, reference: GeoPoint): ArrayList<Groop> {
            //BASE CASE
            if (groops.size < 2) {
                return groops
            }
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
            return (sortGroops(less, reference) + equal + sortGroops(more, reference)) as ArrayList<Groop>
        }

        /////////////////////////GETS LIST OF GROOPS
        /**
         * Returns an arraylist of Groop objects associated with
         * a given activity, looking in the activities collection of the database
         * Be sure to check for subactivities
         *
         * db.collection("activities").document([activity]).get().addOnSuccessListener{...}
         */
        fun getGroopsFromActivity(doc: DocumentSnapshot): ArrayList<Groop> {
            val groops = ArrayList<Groop>()

            //again, I'm not all that certain how this all is going
            // to work, but here we go

            //TODO: still don't know what's up with how arrays return in firebase
            val listOfGroopDocuments = doc.get("listOfGroops")
                    as ArrayList<DocumentReference>
            //parse each groop and add it to the list
            for (groop in listOfGroopDocuments) {
                //mostly, we're just going to invoke the parseGroop method
                groop.get().addOnSuccessListener { snapshot ->
                    groops.add(parseGroop(snapshot))
                }
            }


            return groops
        }

        /**
         * Used by both of the methods below; returns an ArrayList of Groop
         * objects that have been either created or joined by the user specified
         * in the given email
         * The createdOrJoined parameter should always be either "joinedGroops" or "createdGroops"
         */
        private fun getUserGroops(email: String, createdOrJoined: String): ArrayList<Groop> {
            if (createdOrJoined != "joinedGroops" && createdOrJoined != "createdGroops") {
                //not using the method properly
                throw IllegalArgumentException("Does not work")
            }

            val groops = ArrayList<Groop>()

            //again, I'm not all that certain how this all is going
            // to work, but here we go
            db.collection(users).document(email).get().addOnSuccessListener { snapshot ->
                //TODO: still don't know what's up with how arrays return in firebase
                val listOfGroopDocuments = snapshot.get(createdOrJoined) as ArrayList<DocumentReference>
                //parse each groop and add it to the list
                for (groop in listOfGroopDocuments) {
                    //mostly, we're just going to invoke the parseGroop method
                    groop.get().addOnSuccessListener { snapshot ->
                        groops.add(parseGroop(snapshot))
                    }
                }
            }

            return groops
        }

        /**
         * Returns an ArrayList of groops that have been created by
         * the user specified by the email
         */
        fun getGroopsBy(email: String, gotten: (ArrayList<Groop>) -> Any?): ArrayList<Groop> {
            return getUserGroops(email, "createdGroops")
        }

        /**
         * Returns an ArrayList of Groop objects that have been
         * joined by the user specified in this email
         */
        fun getGroopsJoinedBy(email: String, gotten: (ArrayList<Groop>) -> Any?): ArrayList<Groop> {
            return getUserGroops(email, "joinedGroops")
        }

        /////////////////////////MISC. GETS
        /**
         * Given a user, represented as an email, returns an arraylist
         * of activity objects including every activity that the user
         * is interested in
         */
        //getUserActivityList("billiam@gmail.com", (activityList) -> {this.list = activityList})
        fun getUserActivityList(email: String, gotten: (ArrayList<Activity_groop>) -> Any?) {
            val activityList = ArrayList<Activity_groop>()

            val task = db.collection(users).document(email).collection(activities).get()
            task.addOnSuccessListener { query ->
                //go through each activity in the collection
                val docList = query.documents

                for (doc in docList) {
                    //construct a new activity from the given fields
                    activityList.add(parseActivity(doc))
                }

                gotten(activityList)
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
    }
}