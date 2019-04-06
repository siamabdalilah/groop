package com.example.groop.Util

import android.content.Context
import android.widget.Toast

fun toast(context: Context, text: String){
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun isEmail(text: String) : Boolean {
    val regex = Regex("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}\$")
    return regex.containsMatchIn(text)
}