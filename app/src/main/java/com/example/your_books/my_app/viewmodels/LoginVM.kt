package com.example.your_books.my_app.viewmodels

import androidx.lifecycle.ViewModel
import com.example.your_books.my_app.repositories.UserRepo

class LoginVM : ViewModel() {

    val repo = UserRepo()
    var servicesLiveData = repo.loginResponse

    fun userLogin(login: String, password: String) = repo.userLogin(login, password)

}