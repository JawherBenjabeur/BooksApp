package com.example.your_books.my_app.models

data class CommentSelect (

    val id_commentaire:String,
    val id_livre:String,
    val id_client:String,
    val commentaire:String,
    val nom:String,
    val prenom:String,
    val ville:String,
    val datedenaossance:String,
    val numerotelephone:String,
    val email:String,
    val motdepass:String,
    val photo_client:String
)