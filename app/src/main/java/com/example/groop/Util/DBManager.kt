package com.example.groop.Util

import com.google.firebase.firestore.FirebaseFirestore

class DBManager() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object Paths {
        val activities: String = "activities"
        val groops: String = "groops"
        val users: String = "users"
    }

    /**
     * Given a user, represented as an email, returns an arraylist
     * of activity objects including every activity that the user
     * is interested in
     */
    fun getUserActivityList(email: String): ArrayList<Activity> {
        val activityList = ArrayList<Activity>()

        db.collection(users).document(email).collection(activities).get()
            .addOnSuccessListener { query ->
                //go through each activity in the collection
                val docList = query.documents

                for (doc in docList) {
                    //construct a new activity from the given fields
                    activityList.add(
                        Activity(
                            doc.id,
                            doc.get("description") as String?,
                            doc.get("skill") as Int?
                        )
                    )
                }

            }

        //list should now be full of activity objects
        return activityList
    }

    /**
     * Returns a list of Strings representing the names of
     * all the activities in the database
     */
    fun getAllActivities(): ArrayList<String> {
        val activityList = ArrayList<String>()

        db.collection(activities).get().addOnSuccessListener { query ->
            val docList = query.documents
            //activities are indexed by their name, so we just add that name to the list
            for (doc in docList) {
                activityList.add(doc.id)
            }
        }

        return activityList
    }

    /**
     * Returns an arraylist of Groop objects associated with
     * a given activity
     * Be sure to check for subactivities
     */
    fun getGroopsFromActivity(activity: String) : ArrayList<Groop> {
        val groops = ArrayList<Groop>()

        return groops
    }

    /**
     * Updates the info for an activity that one user has expressed
     * interest in.
     * Void method, writes to the user's list of activities
     */
    fun updateActivityInfo(email: String, activity: String, bio: String) {

    }

    /**
     * Returns an ArrayList of groops that have been created by
     * the user specified by the email
     */
    fun getGroopsBy(email: String) : ArrayList<Groop> {
        val groops = ArrayList<Groop>()

        return groops
    }

    /**
     * Returns an ArrayList of groops that have been joined by
     * the user specified by the email
     */
    fun getGroopsJoinedBy(email: String) : ArrayList<Groop> {
        val groops = ArrayList<Groop>()

        return groops
    }

}