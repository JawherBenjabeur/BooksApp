package com.example.your_books.my_app.data.requests

import com.example.your_books.my_app.models.WebServiceResultData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiDelete {

    companion object {

        operator fun  invoke() : ApiDelete {
            return Retrofit.Builder().baseUrl("http://10.192.76.34:8080/PFE/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiDelete::class.java)
        }
    }

    @FormUrlEncoded
    @POST("deletebook.php")
    fun deletebook(@Field("id_livre") id_livre: String): Call<WebServiceResultData<Any>>

}