package com.example.groop

import android.util.Log
import com.example.groop.Util.DBManager
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {
    //all from here: https://firebase.google.com/docs/cloud-messaging/android/client
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String?) {
        Log.d("token", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //TODO - I haven't got the faintest clue what's going on
        DBManager.sendRegistrationToServer(token!!, "telemonian@gmail.com")
    }
}