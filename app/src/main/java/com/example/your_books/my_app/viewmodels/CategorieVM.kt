package com.example.your_books.my_app.viewmodels

import androidx.lifecycle.ViewModel
import com.example.your_books.my_app.repositories.CategorieRepo

class CategorieVM: ViewModel() {
    val repo = CategorieRepo()
    var servicesLiveData = repo.categorieResponse

    fun categorie() = repo.getcategorie()
}