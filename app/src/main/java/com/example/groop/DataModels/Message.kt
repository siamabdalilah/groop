package com.example.groop.DataModels

import com.google.firebase.firestore.DocumentReference
import java.io.Serializable
import java.util.*

/**
 * Here's how this works:
 * fromId is a string of the username of the sender of the message
 * toId is a string of the username of the receiver of the message;
 *  if this is a groop message, toId is null, and the target Groop
 *  can be extrapolated based on where the message is in the collection
 * timeStamp is the time it was sent
 * content is the actual text of the message
 */
data class Message (var from: String = "", var to: String? = null,
                    var timeStamp: Date = Date(), var content: String = "") : Serializable