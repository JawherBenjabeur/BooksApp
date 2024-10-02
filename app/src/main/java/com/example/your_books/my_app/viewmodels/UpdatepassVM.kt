package com.example.your_books.my_app.viewmodels

import androidx.lifecycle.ViewModel
import com.example.your_books.my_app.repositories.UserRepo

class UpdatepassVM:ViewModel() {

    val repo = UserRepo()
    var servicesLiveData = repo.updatepassResponse
    fun userUpdatepass(id_client:String, password:String) = repo.userUpdatepass(id_client,password)
}