package com.example.your_books.my_app.viewmodels

import androidx.lifecycle.ViewModel
import com.example.your_books.my_app.repositories.UserRepo

class UpdateVM:ViewModel (){

    val repo = UserRepo()
    var servicesLiveData = repo.updateResponse
    fun userUpdate(id_client: String, nom:String, prenom:String, ville:String, date:String, telephone:String, email:String,photo_client:String) = repo.userUpdate(id_client, nom, prenom, ville, date, telephone, email, photo_client)

}