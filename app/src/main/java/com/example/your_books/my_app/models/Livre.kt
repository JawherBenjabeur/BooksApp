package com.example.your_books.my_app.models

data class Livre (
        val id_livre : String,
        val id_client :String,
        val id_categorie:String,
        val titre:String,
        val auteur:String,
        val editeur:String,
        val dateedition:String,
        val langue:String,
        val resumer:String,
        val photo_livre :String,
        val disponibilite:String,
        val id_rubrique:String
        )