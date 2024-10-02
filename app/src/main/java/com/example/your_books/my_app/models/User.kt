package com.example.your_books.my_app.models

data class User(
    val id_client: String ,
    val nom:String,
    val prenom:String,
    val ville:String,
    val datedenaissance:String,
    val numerotelephone:String,
    val email:String,
    val motdepass:String?,
    val photo_client:String
    )
