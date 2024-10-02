package com.example.your_books.my_app.models

import android.content.Context
import android.content.SharedPreferences


class SharedPreferences(context: Context) {
    val Preference_Name ="SharedPreference"

    val preferences=context.getSharedPreferences(Preference_Name,Context.MODE_PRIVATE)

//Enregistrer les données dans le cache de téléphone
    fun setloginShared(id_client: String, nom:String, prenom:String, ville:String, datedenaissance:String, numerotelephone:String, email:String, motdepass:String, photo_client:String ) {

        val editor: SharedPreferences.Editor = preferences.edit()

        editor.putString("id_client",id_client)
        editor.putString("nom",nom)
        editor.putString("prenom",prenom)
        editor.putString("ville",ville)
        editor.putString("datedenaissance",datedenaissance)
        editor.putString("numerotelephone",numerotelephone)
        editor.putString("email",email)
        editor.putString("motdepass",motdepass)
        editor.putString("photo_client",photo_client)
        editor.apply()

    }

// Obtenir les données from preferences
    fun getloginShared(): User {

        val id_client = preferences.getString("id_client","")
        val nom = preferences.getString("nom","")
        val prenom = preferences.getString("prenom","")
        val ville = preferences.getString("ville","")
        val datedenaissance = preferences.getString("datedenaissance","")
        val numerotelephone = preferences.getString("numerotelephone","")
        val email = preferences.getString("email","")
        val motdepass = preferences.getString("motdepass","")
        val photo_client = preferences.getString("photo_client","")

        val user = User(id_client!!, nom!!, prenom!!, ville!!, datedenaissance!!, numerotelephone!!, email!!, motdepass!!, photo_client!!)
        return user
    }

    fun logout() {

        val editor: SharedPreferences.Editor = preferences.edit()

        editor.putString("id_client","")
        editor.putString("nom","")
        editor.putString("prenom","")
        editor.putString("ville","")
        editor.putString("datedenaissance","")
        editor.putString("numerotelephone","")
        editor.putString("email","")
        editor.putString("motdepass","")
        editor.putString("photo_client","")
        editor.clear()
        editor.apply()
    }

    fun setpassword(motdepass:String){

        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString("motdepass",motdepass)
        editor.apply()

    }


}