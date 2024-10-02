package com.example.your_books.my_app.viewmodels

import androidx.lifecycle.ViewModel
import com.example.your_books.my_app.repositories.UserRepo

class SignupVM : ViewModel (){

    val repo = UserRepo()
    var servicesLiveData = repo.signupResponse
    fun userSingup(nom:String, prenom:String, ville:String, date:String, telephone:String, email:String, password: String,photo_client:String) = repo.userSignup(nom, prenom, ville, date, telephone, email, password, photo_client)
}