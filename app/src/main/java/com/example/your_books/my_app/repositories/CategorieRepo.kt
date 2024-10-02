package com.example.your_books.my_app.repositories

import androidx.lifecycle.MutableLiveData
import com.example.your_books.my_app.data.requests.ApiAdd
import com.example.your_books.my_app.models.*
import retrofit2.Call
import retrofit2.Response

class CategorieRepo {
    val categorieResponse = MutableLiveData<WebServiceResult>()

    fun getcategorie():MutableLiveData<WebServiceResult>{
        ApiAdd().getcategorie()
                .enqueue(object : retrofit2.Callback<WebServiceResultData<List<Categorie>>> {
                    override fun onResponse(
                            call: Call<WebServiceResultData<List<Categorie>>>?, response: Response<WebServiceResultData<List<Categorie>>>?) {
                        if (response!!.isSuccessful){

                            categorieResponse.value = WebServiceResultCategorie(response.body().data!!)

                        }else{
                            categorieResponse.value = WebServiceResultFailureCategorie(response.body().message.toString())
                        }
                    }

                    override fun onFailure(call: Call<WebServiceResultData<List<Categorie>>>?, t: Throwable?) {
                        t!!.printStackTrace()
                        categorieResponse.value = WebServiceResultFailureCategorie(message = "error")
                    }
                })
        return categorieResponse
    }
}